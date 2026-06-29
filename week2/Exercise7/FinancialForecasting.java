// ============================================================
// RECURSION — WHAT AND WHY
//
//   Recursion is when a method calls ITSELF to solve a smaller
//   version of the same problem, until it hits a BASE CASE
//   (the simplest version it can answer directly).
//
//   Every recursive method needs:
//     1. BASE CASE    — the condition where it STOPS calling itself.
//                       Without this, you get infinite recursion → StackOverflowError.
//     2. RECURSIVE CASE — the problem broken into a smaller version,
//                       calling itself with that smaller input.
//
// FUTURE VALUE FORMULA:
//   FV = PV × (1 + r)^n
//   Where:
//     PV = present value (starting amount)
//     r  = growth rate per period (e.g. 0.10 for 10%)
//     n  = number of periods
//
//   Recursive breakdown:
//     futureValue(PV, r, 0)  = PV                          ← base case
//     futureValue(PV, r, n)  = futureValue(PV, r, n-1) × (1 + r)  ← recursive case
//
//   Each call reduces n by 1, until n reaches 0.
//
// TIME COMPLEXITY:
//   O(n) — one recursive call per period. Stack depth = n.
//
// OPTIMIZATION — MEMOIZATION:
//   If the same (PV, r, n) is computed multiple times (e.g., in a
//   branching scenario), cache results in a HashMap. This reduces
//   repeated work from O(2^n) to O(n) in tree-shaped recursion.
//   For this linear recursion, memoization doesn't add much, but
//   it's shown here as good practice for the general concept.
// ============================================================

import java.util.HashMap;

// ──────────────────────────────────────────────
//  FINANCIAL FORECASTING CLASS
// ──────────────────────────────────────────────
class FinancialForecast {

    // PLAIN RECURSION — O(n)
    // Each call multiplies by (1 + rate) once and passes n-1 down.
    // Base case: when n == 0, return the present value unchanged.
    public static double futureValueRecursive(double presentValue, double rate, int periods) {
        // Base case — no more periods to compound
        if (periods == 0) {
            return presentValue;
        }
        // Recursive case — apply one period of growth, then recurse
        return futureValueRecursive(presentValue, rate, periods - 1) * (1 + rate);
    }

    // MEMOIZED RECURSION — avoids recomputing same subproblems
    // Cache key = "pv_rate_periods" → stored result
    // Useful when many different forecasts share intermediate period values.
    private static HashMap<String, Double> memo = new HashMap<>();

    public static double futureValueMemo(double presentValue, double rate, int periods) {
        if (periods == 0) return presentValue;

        String key = presentValue + "_" + rate + "_" + periods;
        if (memo.containsKey(key)) {
            System.out.println("  [Memo] Cache hit for periods=" + periods);
            return memo.get(key);
        }

        double result = futureValueMemo(presentValue, rate, periods - 1) * (1 + rate);
        memo.put(key, result);
        return result;
    }

    // ITERATIVE VERSION — O(n), no stack overhead
    // Shown for comparison: recursion and iteration give identical results.
    public static double futureValueIterative(double presentValue, double rate, int periods) {
        double value = presentValue;
        for (int i = 0; i < periods; i++) {
            value *= (1 + rate);
        }
        return value;
    }
}

// ──────────────────────────────────────────────
//  TEST CLASS
// ──────────────────────────────────────────────
class ForecastTest {

    public static void runTests() {
        double pv   = 10000.0;  // ₹10,000 initial investment
        double rate = 0.10;     // 10% annual growth
        System.out.printf("  Initial investment : ₹%.2f%n", pv);
        System.out.printf("  Annual growth rate : %.0f%%%n", rate * 100);

        System.out.println("\n--- Test 1: 1-year forecast ---");
        double r1 = FinancialForecast.futureValueRecursive(pv, rate, 1);
        System.out.printf("  Recursive  : ₹%.2f%n", r1);

        System.out.println("\n--- Test 2: 5-year forecast ---");
        double r2 = FinancialForecast.futureValueRecursive(pv, rate, 5);
        double r2i = FinancialForecast.futureValueIterative(pv, rate, 5);
        System.out.printf("  Recursive  : ₹%.2f%n", r2);
        System.out.printf("  Iterative  : ₹%.2f  (should match)%n", r2i);
        System.out.println("  PASS: " + (Math.abs(r2 - r2i) < 0.001 ? "Both approaches match." : "MISMATCH!"));

        System.out.println("\n--- Test 3: 10-year forecast (with memoization demo) ---");
        double r3 = FinancialForecast.futureValueMemo(pv, rate, 10);
        System.out.printf("  Memoized   : ₹%.2f%n", r3);

        System.out.println("\n--- Test 4: Year-by-year growth table (recursive) ---");
        System.out.println("  Year | Future Value");
        System.out.println("  -----|-------------");
        for (int y = 0; y <= 10; y++) {
            double fv = FinancialForecast.futureValueRecursive(pv, rate, y);
            System.out.printf("  %-4d | ₹%,.2f%n", y, fv);
        }
    }
}

// ──────────────────────────────────────────────
//  MAIN CLASS
// ──────────────────────────────────────────────
public class FinancialForecasting {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("  Financial Forecasting — Recursion Demo   ");
        System.out.println("===========================================");

        ForecastTest.runTests();

        System.out.println("\n===========================================");
        System.out.println("  ANALYSIS SUMMARY");
        System.out.println("===========================================");
        System.out.println("  Recursive futureValue(PV, r, n):");
        System.out.println("    Time  complexity : O(n) — n recursive calls");
        System.out.println("    Space complexity : O(n) — n frames on the call stack");
        System.out.println();
        System.out.println("  Iterative version:");
        System.out.println("    Time  complexity : O(n) — same");
        System.out.println("    Space complexity : O(1) — no stack frames");
        System.out.println();
        System.out.println("  Memoized recursion:");
        System.out.println("    Useful when same subproblem appears multiple times");
        System.out.println("    (e.g., branching scenarios, Fibonacci-style trees).");
        System.out.println("    For this linear recursion, iterative is more efficient.");
        System.out.println();
        System.out.println("  RECOMMENDATION: Use iterative for simple forecasts.");
        System.out.println("  Use memoized recursion if computing many overlapping");
        System.out.println("  subproblems (e.g., tree of possible growth rates).");
        System.out.println("===========================================");
    }
}