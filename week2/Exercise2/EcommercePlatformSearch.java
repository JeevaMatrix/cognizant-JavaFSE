// ============================================================
// BIG O NOTATION — WHAT AND WHY
//
//   Big O describes how an algorithm's time (or space) grows
//   as the input size (n) increases. It tells you the WORST-CASE
//   performance so you can compare algorithms before coding them.
//
//   Common Big O values (best to worst):
//     O(1)       → Constant  — doesn't matter how big n is
//     O(log n)   → Logarithmic — halves the problem each step
//     O(n)       → Linear    — checks every element once
//     O(n log n) → Log-linear — e.g. merge sort
//     O(n²)      → Quadratic — nested loops, very slow for large n
//
// SEARCH ALGORITHM COMPARISON:
//
//   Linear Search:
//     Best case    O(1)   — first element is the target
//     Average case O(n/2) → O(n)  — target is somewhere in the middle
//     Worst case   O(n)   — target is last, or not found at all
//
//   Binary Search (requires SORTED array):
//     Best case    O(1)   — middle element is the target
//     Average case O(log n)
//     Worst case   O(log n) — always halves the search space
//
//   For 1,000,000 products:
//     Linear  → up to 1,000,000 comparisons
//     Binary  → up to 20 comparisons  (log₂ 1,000,000 ≈ 20)
//   → Binary search is far better for large sorted datasets.
// ============================================================

import java.util.Arrays;
import java.util.Comparator;

// ──────────────────────────────────────────────
//  PRODUCT CLASS
// ──────────────────────────────────────────────
class Product {
    int productId;
    String productName;
    String category;

    Product(int productId, String productName, String category) {
        this.productId   = productId;
        this.productName = productName;
        this.category    = category;
    }

    @Override
    public String toString() {
        return "[ID:" + productId + " | " + productName + " | " + category + "]";
    }
}

// ──────────────────────────────────────────────
//  SEARCH ALGORITHMS
// ──────────────────────────────────────────────
class SearchAlgorithms {

    // LINEAR SEARCH — O(n)
    // Checks every product one by one until it finds a match.
    // Works on UNSORTED arrays. Simple but slow for large data.
    public static Product linearSearch(Product[] products, int targetId) {
        int comparisons = 0;
        for (Product p : products) {
            comparisons++;
            if (p.productId == targetId) {
                System.out.println("  [Linear]  Found after " + comparisons + " comparison(s).");
                return p;
            }
        }
        System.out.println("  [Linear]  Not found. Made " + comparisons + " comparison(s).");
        return null;
    }

    // BINARY SEARCH — O(log n)
    // Requires a SORTED array. Works by repeatedly halving the search space:
    //   1. Look at the middle element.
    //   2. If it matches → done.
    //   3. If target < mid → search left half.
    //   4. If target > mid → search right half.
    //   Repeat until found or search space is empty.
    public static Product binarySearch(Product[] sortedProducts, int targetId) {
        int low = 0, high = sortedProducts.length - 1;
        int comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (sortedProducts[mid].productId == targetId) {
                System.out.println("  [Binary]  Found after " + comparisons + " comparison(s).");
                return sortedProducts[mid];
            } else if (sortedProducts[mid].productId < targetId) {
                low = mid + 1;   // target is in the RIGHT half
            } else {
                high = mid - 1;  // target is in the LEFT half
            }
        }
        System.out.println("  [Binary]  Not found. Made " + comparisons + " comparison(s).");
        return null;
    }
}

// ──────────────────────────────────────────────
//  TEST CLASS
// ──────────────────────────────────────────────
class SearchTest {

    public static void runTests(Product[] products, Product[] sortedProducts) {

        System.out.println("\n--- Test 1: Search for a product that EXISTS (ID: 104) ---");
        Product r1 = SearchAlgorithms.linearSearch(products, 104);
        System.out.println("  Linear result  : " + r1);

        Product r2 = SearchAlgorithms.binarySearch(sortedProducts, 104);
        System.out.println("  Binary result  : " + r2);

        System.out.println("\n--- Test 2: Search for LAST product in array (ID: 108) — linear worst case ---");
        Product r3 = SearchAlgorithms.linearSearch(products, 108);
        System.out.println("  Linear result  : " + r3);

        Product r4 = SearchAlgorithms.binarySearch(sortedProducts, 108);
        System.out.println("  Binary result  : " + r4);

        System.out.println("\n--- Test 3: Search for product that does NOT EXIST (ID: 999) ---");
        Product r5 = SearchAlgorithms.linearSearch(products, 999);
        System.out.println("  Linear result  : " + r5);

        Product r6 = SearchAlgorithms.binarySearch(sortedProducts, 999);
        System.out.println("  Binary result  : " + r6);
    }
}

// ──────────────────────────────────────────────
//  MAIN CLASS
// ──────────────────────────────────────────────
public class EcommercePlatformSearch {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("  E-Commerce Platform — Search Demo        ");
        System.out.println("===========================================");

        // Unsorted array — for linear search
        Product[] products = {
            new Product(105, "Wireless Mouse",    "Electronics"),
            new Product(102, "Running Shoes",     "Footwear"),
            new Product(107, "Desk Lamp",         "Furniture"),
            new Product(101, "Laptop Stand",      "Electronics"),
            new Product(104, "Bluetooth Speaker", "Electronics"),
            new Product(103, "Yoga Mat",          "Sports"),
            new Product(106, "Coffee Mug",        "Kitchen"),
            new Product(108, "Notebook Set",      "Stationery"),
        };

        // Sorted copy — binary search REQUIRES sorted order by productId
        Product[] sortedProducts = Arrays.copyOf(products, products.length);
        Arrays.sort(sortedProducts, Comparator.comparingInt(p -> p.productId));

        System.out.println("\nUnsorted array (for linear search):");
        for (Product p : products) System.out.println("  " + p);

        System.out.println("\nSorted array (for binary search):");
        for (Product p : sortedProducts) System.out.println("  " + p);

        SearchTest.runTests(products, sortedProducts);

        System.out.println("\n===========================================");
        System.out.println("  ANALYSIS SUMMARY");
        System.out.println("===========================================");
        System.out.println("  Linear  Search: O(n)     — scans all n products");
        System.out.println("  Binary  Search: O(log n) — halves the array each step");
        System.out.println();
        System.out.println("  For 1,000,000 products:");
        System.out.println("    Linear  → up to 1,000,000 comparisons");
        int binaryMax = (int) Math.ceil(Math.log(1_000_000) / Math.log(2));
        System.out.println("    Binary  → up to " + binaryMax + " comparisons  (log₂ 1,000,000)");
        System.out.println();
        System.out.println("  RECOMMENDATION: Use Binary Search for the e-commerce");
        System.out.println("  platform. Products can be pre-sorted by ID at load time,");
        System.out.println("  and binary search is exponentially faster at scale.");
        System.out.println("===========================================");
    }
}