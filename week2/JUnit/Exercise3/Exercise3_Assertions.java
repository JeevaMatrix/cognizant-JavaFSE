// ============================================================
// EXERCISE 3: ASSERTIONS IN JUNIT
// ============================================================
// WHAT: Assertions are statements that check if a condition is
//       TRUE. If it isn't, the test fails immediately and JUnit
//       reports exactly which assertion failed.
//
// WHY:  Assertions are the actual "verification" step of a test —
//       without them, a test method just runs code but never
//       confirms the result is correct.
//
// COMMON JUNIT 4 ASSERTIONS (all in org.junit.Assert):
//   assertEquals(expected, actual)  → values must match
//   assertTrue(condition)           → condition must be true
//   assertFalse(condition)          → condition must be false
//   assertNull(object)              → object must be null
//   assertNotNull(object)           → object must NOT be null
//   assertArrayEquals(a, b)         → arrays must match element-by-element
//   assertSame(obj1, obj2)          → must be the SAME object reference
//   assertNotEquals(a, b)           → values must NOT match
// ============================================================

import org.junit.Test;
import static org.junit.Assert.*;

public class AssertionsTest {

    @Test
    public void testAssertEquals() {
        // Checks that two values are equal
        assertEquals("2 + 3 should equal 5", 5, 2 + 3);
        System.out.println("testAssertEquals PASSED");
    }

    @Test
    public void testAssertTrue() {
        // Checks that a boolean expression is true
        assertTrue("5 should be greater than 3", 5 > 3);
        System.out.println("testAssertTrue PASSED");
    }

    @Test
    public void testAssertFalse() {
        // Checks that a boolean expression is false
        assertFalse("5 should NOT be less than 3", 5 < 3);
        System.out.println("testAssertFalse PASSED");
    }

    @Test
    public void testAssertNull() {
        // Checks that an object reference is null
        String value = null;
        assertNull("value should be null", value);
        System.out.println("testAssertNull PASSED");
    }

    @Test
    public void testAssertNotNull() {
        // Checks that an object reference is NOT null
        Object obj = new Object();
        assertNotNull("obj should not be null", obj);
        System.out.println("testAssertNotNull PASSED");
    }

    @Test
    public void testAssertArrayEquals() {
        // Checks two arrays have the same elements in the same order
        int[] expected = {1, 2, 3};
        int[] actual   = {1, 2, 3};
        assertArrayEquals("Arrays should match", expected, actual);
        System.out.println("testAssertArrayEquals PASSED");
    }

    @Test
    public void testAssertSame() {
        // Checks both references point to the EXACT SAME object (not just equal values)
        String a = "hello";
        String b = a;
        assertSame("a and b should be the same reference", a, b);
        System.out.println("testAssertSame PASSED");
    }

    @Test
    public void testAssertNotEquals() {
        // Checks two values are NOT equal
        assertNotEquals("5 should not equal 10", 5, 10);
        System.out.println("testAssertNotEquals PASSED");
    }

    // Manual runner — calls each test directly so you can see output
    // without needing Maven/Gradle. In a real project, `mvn test` does this.
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  JUnit Assertions Demo (manual run)       ");
        System.out.println("===========================================");

        AssertionsTest test = new AssertionsTest();
        test.testAssertEquals();
        test.testAssertTrue();
        test.testAssertFalse();
        test.testAssertNull();
        test.testAssertNotNull();
        test.testAssertArrayEquals();
        test.testAssertSame();
        test.testAssertNotEquals();

        System.out.println("-------------------------------------------");
        System.out.println("All 8 assertion tests passed.");
        System.out.println("===========================================");
    }
}
