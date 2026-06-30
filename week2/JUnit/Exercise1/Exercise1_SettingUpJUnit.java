// ============================================================
// EXERCISE 1: SETTING UP JUNIT
// ============================================================
// WHAT: JUnit is a unit testing framework for Java. A "unit test"
//       checks that ONE small piece of code (usually a method)
//       behaves correctly, in isolation, automatically.
//
// WHY:  Manually testing by running main() and eyeballing output
//       doesn't scale. JUnit lets you write a test ONCE and rerun
//       it automatically every time you change code — catching
//       bugs immediately instead of in production.
//
// MAVEN SETUP (pom.xml) — JUnit 4:
//   <dependency>
//       <groupId>junit</groupId>
//       <artifactId>junit</artifactId>
//       <version>4.13.2</version>
//       <scope>test</scope>
//   </dependency>
//
// PROJECT STRUCTURE (Maven convention):
//   src/main/java/...     ← your actual application code
//   src/test/java/...     ← your test code (mirrors main package)
//
// This file shows BOTH: a simple class to test, and its first
// JUnit test class, to prove the setup works end-to-end.
// ============================================================

import org.junit.Test;
import static org.junit.Assert.assertEquals;

// ──────────────────────────────────────────────
//  CLASS UNDER TEST (would normally live in src/main/java)
// ──────────────────────────────────────────────
class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }
}

// ──────────────────────────────────────────────
//  FIRST JUNIT TEST CLASS (would normally live in src/test/java)
// ──────────────────────────────────────────────
public class CalculatorSetupTest {

    // @Test marks this method as a test case JUnit will run automatically.
    // The method name describes WHAT is being tested.
    @Test
    public void testAdd() {
        Calculator calc = new Calculator();
        int result = calc.add(2, 3);

        // assertEquals(expected, actual) — fails the test if they don't match
        assertEquals(5, result);

        System.out.println("testAdd PASSED: 2 + 3 = " + result);
    }

    @Test
    public void testSubtract() {
        Calculator calc = new Calculator();
        int result = calc.subtract(10, 4);

        assertEquals(6, result);

        System.out.println("testSubtract PASSED: 10 - 4 = " + result);
    }

    // A plain main() method so you can run this file directly without
    // a build tool, just to SEE the test logic execute. In a real Maven/
    // Gradle project, JUnit's test runner calls @Test methods for you —
    // you would NOT normally call them from main().
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  JUnit Setup Verification (manual run)    ");
        System.out.println("===========================================");
        System.out.println("Note: In a real Maven project, run via");
        System.out.println("  mvn test");
        System.out.println("and JUnit's runner auto-discovers @Test methods.");
        System.out.println("-------------------------------------------");

        CalculatorSetupTest test = new CalculatorSetupTest();
        test.testAdd();
        test.testSubtract();

        System.out.println("-------------------------------------------");
        System.out.println("All tests passed manually. JUnit setup is correct.");
        System.out.println("===========================================");
    }
}
