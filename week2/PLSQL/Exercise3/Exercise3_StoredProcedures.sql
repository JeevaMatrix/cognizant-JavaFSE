/* ============================================================
   EXERCISE 3: STORED PROCEDURES
   ============================================================
   WHAT:  A stored procedure is a named, compiled PL/SQL block
          stored in the database that can be called repeatedly
          with different parameters.
   WHY:   Reusable, centralizes business logic in the DB layer,
          reduces network round-trips, and can be scheduled
          (e.g., monthly interest via a DB job).

   ASSUMED TABLE STRUCTURES (adjust to match your schema):
   ------------------------------------------------------------
   SAVINGS_ACCOUNTS (account_id, customer_id, balance)
   EMPLOYEES        (employee_id, name, department, salary)
   ACCOUNTS         (account_id, customer_id, balance)  -- for transfers
   ============================================================ */


-- Sample schema + seed data (run once to test the scripts below)

CREATE TABLE savings_accounts (
    account_id   NUMBER PRIMARY KEY,
    customer_id  NUMBER,
    balance      NUMBER(12,2)
);

CREATE TABLE employees (
    employee_id  NUMBER PRIMARY KEY,
    name         VARCHAR2(50),
    department   VARCHAR2(50),
    salary       NUMBER(12,2)
);

CREATE TABLE accounts (
    account_id   NUMBER PRIMARY KEY,
    customer_id  NUMBER,
    balance      NUMBER(12,2)
);

INSERT INTO savings_accounts VALUES (1, 101, 5000.00);
INSERT INTO savings_accounts VALUES (2, 102, 12000.00);
INSERT INTO savings_accounts VALUES (3, 103, 7500.00);

INSERT INTO employees VALUES (1, 'Arun Kumar',   'Sales',       40000.00);
INSERT INTO employees VALUES (2, 'Divya Iyer',   'Sales',       42000.00);
INSERT INTO employees VALUES (3, 'Karthik Raj',  'Engineering', 60000.00);

INSERT INTO accounts VALUES (501, 201, 10000.00);
INSERT INTO accounts VALUES (502, 202, 3000.00);

COMMIT;


/* ============================================================
   SCENARIO 1: ProcessMonthlyInterest
   - Apply 1% interest to ALL savings account balances
   ============================================================ */

CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest
IS
    v_interest_rate  NUMBER := 0.01;   -- 1% monthly interest
    v_accounts_updated NUMBER := 0;
BEGIN

    UPDATE savings_accounts
    SET balance = balance + (balance * v_interest_rate);

    v_accounts_updated := SQL%ROWCOUNT;

    DBMS_OUTPUT.PUT_LINE(
        'Monthly interest applied to ' || v_accounts_updated || ' account(s).'
    );

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error in ProcessMonthlyInterest: ' || SQLERRM);
        ROLLBACK;
END ProcessMonthlyInterest;
/

-- ── How to call it ──
BEGIN
    ProcessMonthlyInterest;
END;
/

-- Verify result
SELECT * FROM savings_accounts;


/* ============================================================
   SCENARIO 2: UpdateEmployeeBonus
   - Add a bonus % (parameter) to salary, for a given department
   ============================================================ */

CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_department      IN VARCHAR2,
    p_bonus_percent   IN NUMBER
)
IS
    v_employees_updated NUMBER := 0;
BEGIN

    -- Basic validation
    IF p_bonus_percent < 0 OR p_bonus_percent > 100 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Bonus percent must be between 0 and 100');
    END IF;

    UPDATE employees
    SET salary = salary + (salary * p_bonus_percent / 100)
    WHERE department = p_department;

    v_employees_updated := SQL%ROWCOUNT;

    IF v_employees_updated = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No employees found in department: ' || p_department);
    ELSE
        DBMS_OUTPUT.PUT_LINE(
            v_employees_updated || ' employee(s) in ' || p_department ||
            ' received a ' || p_bonus_percent || '% bonus.'
        );
    END IF;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error in UpdateEmployeeBonus: ' || SQLERRM);
        ROLLBACK;
END UpdateEmployeeBonus;
/

-- ── How to call it ──
BEGIN
    UpdateEmployeeBonus('Sales', 10);   -- 10% bonus to all Sales employees
END;
/

-- Verify result
SELECT * FROM employees;


/* ============================================================
   SCENARIO 3: TransferFunds
   - Move money between two accounts
   - MUST check sufficient balance BEFORE transferring
   - MUST be atomic: both updates succeed, or neither does
   ============================================================ */

CREATE OR REPLACE PROCEDURE TransferFunds (
    p_from_account  IN NUMBER,
    p_to_account    IN NUMBER,
    p_amount        IN NUMBER
)
IS
    v_from_balance  NUMBER;

    -- Custom exception for insufficient funds
    insufficient_funds EXCEPTION;

BEGIN

    -- Step 1: Lock and read the source account balance
    -- FOR UPDATE locks the row so no other transaction can change it
    -- while we're mid-transfer (prevents race conditions).
    SELECT balance INTO v_from_balance
    FROM accounts
    WHERE account_id = p_from_account
    FOR UPDATE;

    -- Step 2: Validate sufficient balance BEFORE doing anything
    IF v_from_balance < p_amount THEN
        RAISE insufficient_funds;
    END IF;

    -- Step 3: Debit source account
    UPDATE accounts
    SET balance = balance - p_amount
    WHERE account_id = p_from_account;

    -- Step 4: Credit destination account
    UPDATE accounts
    SET balance = balance + p_amount
    WHERE account_id = p_to_account;

    DBMS_OUTPUT.PUT_LINE(
        'Transferred $' || p_amount ||
        ' from account ' || p_from_account ||
        ' to account ' || p_to_account
    );

    COMMIT;

EXCEPTION
    WHEN insufficient_funds THEN
        DBMS_OUTPUT.PUT_LINE(
            'Transfer FAILED: Account ' || p_from_account ||
            ' has insufficient balance ($' || v_from_balance ||
            ') for transfer of $' || p_amount
        );
        ROLLBACK;

    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Transfer FAILED: Account not found.');
        ROLLBACK;

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error in TransferFunds: ' || SQLERRM);
        ROLLBACK;
END TransferFunds;
/

-- ── How to call it ──

-- Successful transfer
BEGIN
    TransferFunds(501, 502, 2000);   -- transfer $2000 from acct 501 to 502
END;
/

-- Failed transfer (insufficient balance) — demonstrates rollback
BEGIN
    TransferFunds(502, 501, 999999);
END;
/

-- Verify result
SELECT * FROM accounts;


/* ============================================================
   KEY CONCEPTS USED
   ------------------------------------------------------------
   CREATE OR REPLACE PROCEDURE  - defines a reusable named block
   IN parameter                  - value passed INTO the procedure
   SQL%ROWCOUNT                  - rows affected by last DML
   FOR UPDATE                    - row-level lock to prevent race
                                    conditions during a transfer
   Custom EXCEPTION              - declared with `insufficient_funds
                                    EXCEPTION;`, raised with RAISE,
                                    caught in its own WHEN clause
   COMMIT / ROLLBACK             - COMMIT only on full success;
                                    ROLLBACK on any failure so the
                                    transfer is atomic (all-or-nothing)
   ============================================================ */
