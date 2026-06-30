import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ──────────────────────────────────────────────
//  SINGLETON CLASS
// ──────────────────────────────────────────────
class Logger {

    // volatile: prevents partial initialization seen by other threads
    private static volatile Logger instance = null;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Private constructor — no one outside can do `new Logger()`
    private Logger() {
        System.out.println("[Logger] Instance created. hashCode: "
                + System.identityHashCode(this));
    }

    // Global access point with Double-Checked Locking
    public static Logger getInstance() {
        if (instance == null) {                  // 1st check — fast path (no lock)
            synchronized (Logger.class) {        // lock only when needed
                if (instance == null) {          // 2nd check — safe path (inside lock)
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + timestamp + "] LOG: " + message);
    }
}

// ──────────────────────────────────────────────
//  TEST CLASS
// ──────────────────────────────────────────────
class LoggerTest {

    public static void runTests() {
        System.out.println("\n--- Test 1: Same instance returned every time ---");
        Logger a = Logger.getInstance();
        Logger b = Logger.getInstance();

        System.out.println("Instance A hashCode: " + System.identityHashCode(a));
        System.out.println("Instance B hashCode: " + System.identityHashCode(b));
        System.out.println("a == b (same object?): " + (a == b));

        if (a == b) {
            System.out.println("PASS: Singleton verified — only one instance exists.");
        } else {
            System.out.println("FAIL: Two different instances were created!");
        }

        System.out.println("\n--- Test 2: log() works correctly via both references ---");
        a.log("Logged via reference A");
        b.log("Logged via reference B");
        System.out.println("PASS: Both references log successfully.");

        System.out.println("\n--- Test 3: Third call still returns same instance ---");
        Logger c = Logger.getInstance();
        System.out.println("Instance C hashCode: " + System.identityHashCode(c));
        System.out.println("a == c (same object?): " + (a == c));
        if (a == c) {
            System.out.println("PASS: Third getInstance() still returns the same object.");
        }
    }
}

// ──────────────────────────────────────────────
//  MAIN CLASS
// ──────────────────────────────────────────────
public class SingletonPatternExample {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("   Singleton Pattern — Logger Demo         ");
        System.out.println("===========================================");

        // Run all tests
        LoggerTest.runTests();

        System.out.println("\n===========================================");
        System.out.println("  All tests done. Logger was instantiated  ");
        System.out.println("  only ONCE — Singleton pattern confirmed. ");
        System.out.println("===========================================");
    }
}