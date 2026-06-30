// ============================================================
// EXERCISE 1: MOCKING AND STUBBING
// ============================================================
// WHAT IS MOCKING?
//   A "mock" is a FAKE object that stands in for a real dependency
//   (like an external API, database, or payment gateway). It looks
//   and behaves like the real class but you control its behavior.
//
// WHY MOCK?
//   You want to test YOUR code's logic in isolation, without
//   actually calling a real external API (slow, unreliable, costs
//   money, or might not even exist yet). Mocking lets you simulate
//   "what if the API returns X" without the API ever running.
//
// WHAT IS STUBBING?
//   Telling the mock what to RETURN when a specific method is
//   called — e.g., "when getData() is called, return 'Mock Data'."
//   This is done with Mockito's `when(...).thenReturn(...)`.
//
// MAVEN DEPENDENCY:
//   <dependency>
//       <groupId>org.mockito</groupId>
//       <artifactId>mockito-core</artifactId>
//       <version>4.11.0</version>
//       <scope>test</scope>
//   </dependency>
// ============================================================

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

// ──────────────────────────────────────────────
//  DEPENDENCY TO BE MOCKED (simulates an external API)
// ──────────────────────────────────────────────
interface ExternalApi {
    String getData();
}

// ──────────────────────────────────────────────
//  CLASS UNDER TEST — depends on ExternalApi
// ──────────────────────────────────────────────
class MyService {
    private final ExternalApi api;

    // Dependency is INJECTED via constructor — this is what makes
    // mocking possible: we can pass in a fake ExternalApi for tests.
    public MyService(ExternalApi api) {
        this.api = api;
    }

    public String fetchData() {
        // Real code might add logging, error handling, formatting, etc.
        return api.getData();
    }
}

// ──────────────────────────────────────────────
//  TEST CLASS
// ──────────────────────────────────────────────
public class MyServiceTest {

    @Test
    public void testExternalApi() {
        // ── ARRANGE ──
        // Step 1: Create a mock object for ExternalApi.
        // Mockito generates a fake implementation at runtime —
        // no real network call ever happens.
        ExternalApi mockApi = mock(ExternalApi.class);

        // Step 2: Stub the method — tell the mock what to return
        // WHEN getData() is called.
        when(mockApi.getData()).thenReturn("Mock Data");

        // Inject the mock into the class under test
        MyService service = new MyService(mockApi);

        // ── ACT ──
        // Step 3: Call the real method we're testing.
        // Internally it calls api.getData() — but since `api` is our
        // mock, it returns "Mock Data" instantly, no real API involved.
        String result = service.fetchData();

        // ── ASSERT ──
        assertEquals("Mock Data", result);
        System.out.println("testExternalApi PASSED: fetchData() returned \"" + result + "\"");
    }

    // Manual runner for direct execution without a test framework runner
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Mockito Exercise 1 — Mocking & Stubbing  ");
        System.out.println("===========================================");

        MyServiceTest test = new MyServiceTest();
        test.testExternalApi();

        System.out.println("-------------------------------------------");
        System.out.println("Note: ExternalApi was NEVER actually called.");
        System.out.println("Mockito intercepted getData() and returned");
        System.out.println("the stubbed value \"Mock Data\" instead.");
        System.out.println("===========================================");
    }
}
