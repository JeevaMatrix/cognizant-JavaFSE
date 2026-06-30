// ============================================================
// EXERCISE 2: VERIFYING INTERACTIONS
// ============================================================
// WHAT IS INTERACTION VERIFICATION?
//   Sometimes a method doesn't RETURN a value you can assert on —
//   it just CALLS another method as a side effect (e.g., sends an
//   email, logs an event, calls an API). In these cases, you can't
//   assert on a return value — instead you verify the mock method
//   WAS CALLED.
//
// WHY VERIFY INTERACTIONS?
//   Confirms your code actually talks to its dependencies the way
//   it's supposed to — e.g., "did fetchData() actually call
//   api.getData() at least once?" This catches bugs where logic
//   silently skips a call it should have made.
//
// KEY MOCKITO VERIFICATION METHODS:
//   verify(mock).method()              → called exactly once
//   verify(mock, times(n)).method()    → called exactly n times
//   verify(mock, never()).method()     → never called
//   verify(mock, atLeastOnce()).method() → called 1+ times
// ============================================================

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

// ──────────────────────────────────────────────
//  DEPENDENCY (same as Exercise 1)
// ──────────────────────────────────────────────
interface ExternalApi {
    String getData();
}

// ──────────────────────────────────────────────
//  CLASS UNDER TEST
// ──────────────────────────────────────────────
class MyService {
    private final ExternalApi api;

    public MyService(ExternalApi api) {
        this.api = api;
    }

    public String fetchData() {
        return api.getData();
    }

    // A method that calls the dependency multiple times, to
    // demonstrate times(n) verification later.
    public void fetchDataTwice() {
        api.getData();
        api.getData();
    }
}

// ──────────────────────────────────────────────
//  TEST CLASS
// ──────────────────────────────────────────────
public class MyServiceTest {

    @Test
    public void testVerifyInteraction() {
        // ── ARRANGE ──
        ExternalApi mockApi = mock(ExternalApi.class);
        MyService service = new MyService(mockApi);

        // ── ACT ──
        // We don't care about the return value here — we care
        // WHETHER api.getData() was actually called.
        service.fetchData();

        // ── ASSERT (verification, not assertEquals) ──
        // Confirms mockApi.getData() was called EXACTLY ONCE
        // during service.fetchData().
        verify(mockApi).getData();

        System.out.println("testVerifyInteraction PASSED: getData() was called exactly once.");
    }

    @Test
    public void testVerifyCalledTwice() {
        // ── ARRANGE ──
        ExternalApi mockApi = mock(ExternalApi.class);
        MyService service = new MyService(mockApi);

        // ── ACT ──
        service.fetchDataTwice();

        // ── ASSERT ──
        // Confirms getData() was called EXACTLY 2 times
        verify(mockApi, times(2)).getData();

        System.out.println("testVerifyCalledTwice PASSED: getData() was called exactly 2 times.");
    }

    @Test
    public void testVerifyNeverCalled() {
        // ── ARRANGE ──
        ExternalApi mockApi = mock(ExternalApi.class);
        MyService service = new MyService(mockApi);
        // Notice: we never call any service method here.

        // ── ASSERT ──
        // Confirms getData() was NEVER called, since we never
        // invoked any method on `service`.
        verify(mockApi, never()).getData();

        System.out.println("testVerifyNeverCalled PASSED: getData() was never called.");
    }

    // Manual runner for direct execution
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Mockito Exercise 2 — Verifying Interactions");
        System.out.println("===========================================");

        MyServiceTest test = new MyServiceTest();
        test.testVerifyInteraction();
        test.testVerifyCalledTwice();
        test.testVerifyNeverCalled();

        System.out.println("-------------------------------------------");
        System.out.println("All verification tests passed. Mockito confirmed");
        System.out.println("exactly how many times each mock method was called.");
        System.out.println("===========================================");
    }
}
