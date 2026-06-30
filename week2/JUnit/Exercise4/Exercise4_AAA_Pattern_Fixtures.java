// ============================================================
// EXERCISE 4: ARRANGE-ACT-ASSERT (AAA) PATTERN,
//             TEST FIXTURES, SETUP AND TEARDOWN METHODS
// ============================================================
// WHAT IS THE AAA PATTERN?
//   A way to STRUCTURE every test method into 3 clear sections:
//     1. ARRANGE — set up the objects and data you need
//     2. ACT     — call the method/code you're actually testing
//     3. ASSERT  — check the result is what you expected
//   This makes tests easy to read and easy to debug when they fail.
//
// WHAT IS A TEST FIXTURE?
//   The fixed, known starting state needed before EVERY test runs
//   (e.g., a fresh BankAccount object with a known balance).
//   Without a fixture, tests could interfere with each other.
//
// SETUP AND TEARDOWN (JUnit 4):
//   @Before  → runs BEFORE every single @Test method (sets up the fixture)
//   @After   → runs AFTER every single @Test method (cleans up)
//   This guarantees every test starts from a clean, identical state —
//   so test order never matters and tests don't leak state into each other.
// ============================================================

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// ──────────────────────────────────────────────
//  CLASS UNDER TEST
// ──────────────────────────────────────────────
class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}

// ──────────────────────────────────────────────
//  TEST CLASS — uses AAA pattern + @Before/@After
// ──────────────────────────────────────────────
public class BankAccountTest {

    // This field holds the fixture — recreated fresh before EVERY test
    private BankAccount account;

    // @Before runs before EACH @Test method below.
    // This is the "fixture setup" — guarantees a fresh, known state.
    @Before
    public void setUp() {
        account = new BankAccount(1000.00);   // every test starts with $1000
        System.out.println("[setUp] Fresh BankAccount created with balance $1000.00");
    }

    // @After runs after EACH @Test method — used for cleanup
    // (closing files, releasing connections, resetting static state, etc.)
    @After
    public void tearDown() {
        System.out.println("[tearDown] Test finished, account reference cleared.\n");
        account = null;
    }

    @Test
    public void testDeposit() {
        // ── ARRANGE ──
        double depositAmount = 500.00;
        double expectedBalance = 1500.00;

        // ── ACT ──
        account.deposit(depositAmount);

        // ── ASSERT ──
        assertEquals(expectedBalance, account.getBalance(), 0.001);
        System.out.println("testDeposit PASSED: balance is now $" + account.getBalance());
    }

    @Test
    public void testWithdraw() {
        // ── ARRANGE ──
        double withdrawAmount = 300.00;
        double expectedBalance = 700.00;

        // ── ACT ──
        account.withdraw(withdrawAmount);

        // ── ASSERT ──
        assertEquals(expectedBalance, account.getBalance(), 0.001);
        System.out.println("testWithdraw PASSED: balance is now $" + account.getBalance());
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        // ── ARRANGE ──
        double withdrawAmount = 5000.00;   // more than the $1000 balance
        boolean exceptionThrown = false;

        // ── ACT ──
        try {
            account.withdraw(withdrawAmount);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        // ── ASSERT ──
        assertTrue("Should have thrown IllegalArgumentException", exceptionThrown);
        System.out.println("testWithdrawInsufficientFunds PASSED: exception correctly thrown");
    }

    // Manual runner — simulates JUnit calling setUp() → test → tearDown()
    // for EACH test, exactly as the real JUnit runner does internally.
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  AAA Pattern + Setup/Teardown Demo        ");
        System.out.println("===========================================\n");

        BankAccountTest test = new BankAccountTest();

        test.setUp();
        test.testDeposit();
        test.tearDown();

        test.setUp();
        test.testWithdraw();
        test.tearDown();

        test.setUp();
        test.testWithdrawInsufficientFunds();
        test.tearDown();

        System.out.println("===========================================");
        System.out.println("  All 3 tests passed, each with a fresh    ");
        System.out.println("  fixture thanks to setUp()/tearDown().    ");
        System.out.println("===========================================");
    }
}
