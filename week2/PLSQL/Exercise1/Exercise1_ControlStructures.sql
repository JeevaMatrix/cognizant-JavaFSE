/* ============================================================
   EXERCISE 1: CONTROL STRUCTURES
   ============================================================
   WHAT:  Control structures (IF, LOOP, FOR) let PL/SQL blocks
          make decisions and repeat actions over rows of data.
   WHY:   Banking logic almost always needs "for each customer,
          check a condition, then act" — exactly what loops + IF do.

   ASSUMED TABLE STRUCTURES (adjust names to match your schema):
   ------------------------------------------------------------
   CUSTOMERS (customer_id, name, age, balance, is_vip)
   LOANS     (loan_id, customer_id, interest_rate, due_date)
   ============================================================ */


-- Sample schema + seed data (run once to test the scripts below)
-- Comment this section out if your tables already exist.

CREATE TABLE customers (
    customer_id  NUMBER PRIMARY KEY,
    name         VARCHAR2(50),
    age          NUMBER,
    balance      NUMBER(12,2),
    is_vip       CHAR(1) DEFAULT 'N'      -- 'Y' or 'N'
);

CREATE TABLE loans (
    loan_id        NUMBER PRIMARY KEY,
    customer_id    NUMBER REFERENCES customers(customer_id),
    interest_rate  NUMBER(5,2),
    due_date       DATE
);

INSERT INTO customers VALUES (1, 'Ravi Kumar',    65, 15000.00, 'N');
INSERT INTO customers VALUES (2, 'Priya Sharma',  45, 12000.00, 'N');
INSERT INTO customers VALUES (3, 'Anand Verma',   72,  8000.00, 'N');
INSERT INTO customers VALUES (4, 'Sneha Reddy',   30,  5000.00, 'N');

INSERT INTO loans VALUES (101, 1, 8.50, SYSDATE + 10);
INSERT INTO loans VALUES (102, 2, 9.00, SYSDATE + 45);
INSERT INTO loans VALUES (103, 3, 7.75, SYSDATE + 20);
INSERT INTO loans VALUES (104, 4, 8.00, SYSDATE + 5);

COMMIT;


/* ============================================================
   SCENARIO 1: Senior citizen loan interest discount
   - Loop through all customers
   - IF age > 60 → reduce their loan interest rate by 1%
   ============================================================ */

DECLARE
    -- Explicit cursor: holds the result set we want to loop over
    CURSOR cur_customers IS
        SELECT customer_id, name, age FROM customers;

    v_rows_updated NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 1: Senior Citizen Loan Discount ---');

    -- FOR loop over a cursor — Oracle auto-opens/fetches/closes it
    FOR cust_rec IN cur_customers LOOP

        IF cust_rec.age > 60 THEN
            UPDATE loans
            SET interest_rate = interest_rate - 1.0
            WHERE customer_id = cust_rec.customer_id;

            v_rows_updated := v_rows_updated + SQL%ROWCOUNT;

            DBMS_OUTPUT.PUT_LINE(
                'Applied 1% discount for ' || cust_rec.name ||
                ' (age ' || cust_rec.age || ')'
            );
        END IF;

    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Total loan rows updated: ' || v_rows_updated);
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        ROLLBACK;
END;
/


/* ============================================================
   SCENARIO 2: VIP status promotion
   - Loop through all customers
   - IF balance > 10000 → set is_vip = 'Y'
   ============================================================ */

DECLARE
    CURSOR cur_customers IS
        SELECT customer_id, name, balance FROM customers;

    v_vip_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 2: VIP Status Promotion ---');

    FOR cust_rec IN cur_customers LOOP

        IF cust_rec.balance > 10000 THEN
            UPDATE customers
            SET is_vip = 'Y'
            WHERE customer_id = cust_rec.customer_id;

            v_vip_count := v_vip_count + 1;

            DBMS_OUTPUT.PUT_LINE(
                cust_rec.name || ' promoted to VIP (balance: $' ||
                cust_rec.balance || ')'
            );
        END IF;

    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Total customers promoted to VIP: ' || v_vip_count);
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        ROLLBACK;
END;
/


/* ============================================================
   SCENARIO 3: Loan due reminders (next 30 days)
   - Fetch all loans due within 30 days
   - Print a reminder message for each
   ============================================================ */

DECLARE
    CURSOR cur_due_loans IS
        SELECT l.loan_id, c.name, l.due_date
        FROM loans l
        JOIN customers c ON c.customer_id = l.customer_id
        WHERE l.due_date BETWEEN SYSDATE AND SYSDATE + 30
        ORDER BY l.due_date;

    v_reminder_count NUMBER := 0;

BEGIN
    DBMS_OUTPUT.PUT_LINE('--- Scenario 3: Loan Due Reminders (next 30 days) ---');

    FOR loan_rec IN cur_due_loans LOOP

        DBMS_OUTPUT.PUT_LINE(
            'REMINDER: Loan #' || loan_rec.loan_id ||
            ' for ' || loan_rec.name ||
            ' is due on ' || TO_CHAR(loan_rec.due_date, 'DD-MON-YYYY')
        );

        v_reminder_count := v_reminder_count + 1;

    END LOOP;

    IF v_reminder_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No loans due in the next 30 days.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Total reminders sent: ' || v_reminder_count);
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/


/* ============================================================
   KEY CONCEPTS USED
   ------------------------------------------------------------
   CURSOR        - a named pointer to a query's result set, so you
                   can loop through rows one at a time.
   FOR ... LOOP  - implicitly opens, fetches each row into a record,
                   and closes the cursor automatically. No manual
                   OPEN/FETCH/CLOSE needed.
   IF / THEN     - the decision-making structure (age > 60, balance > 10000)
   SQL%ROWCOUNT  - tells you how many rows the last DML statement affected
   EXCEPTION     - catches runtime errors so one bad row doesn't crash
                   the whole batch; always ROLLBACK on error for DML blocks
   ============================================================ */
