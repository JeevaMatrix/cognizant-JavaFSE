// ============================================================
// EXERCISE 1: LOGGING ERROR MESSAGES AND WARNING LEVELS
// ============================================================
// WHAT IS SLF4J?
//   SLF4J (Simple Logging Facade for Java) is a logging ABSTRACTION
//   layer — your code calls SLF4J's API, and SLF4J forwards the
//   calls to whatever actual logging implementation is plugged in
//   underneath (Logback, Log4j2, java.util.logging, etc).
//
// WHY USE A FACADE INSTEAD OF LOGGING DIRECTLY?
//   You can SWITCH the underlying logging engine later (e.g.
//   Logback → Log4j2) without changing a single line of your
//   application code — only the dependency/config changes.
//
// LOG LEVELS (from least to most severe):
//   TRACE  → extremely detailed step-by-step info (rarely used)
//   DEBUG  → useful for debugging during development
//   INFO   → normal application events ("Server started")
//   WARN   → something unexpected happened, but app can continue
//   ERROR  → something failed; needs attention
//
// MAVEN DEPENDENCIES (pom.xml):
//   <dependency>
//       <groupId>org.slf4j</groupId>
//       <artifactId>slf4j-api</artifactId>
//       <version>1.7.30</version>
//   </dependency>
//   <dependency>
//       <groupId>ch.qos.logback</groupId>
//       <artifactId>logback-classic</artifactId>
//       <version>1.2.3</version>
//   </dependency>
//
//   NOTE: slf4j-api is just the INTERFACE. logback-classic is the
//   ACTUAL implementation that does the printing/writing. Without
//   logback-classic (or another binding) on the classpath, SLF4J
//   logs nothing — it just warns "no SLF4J providers found."
// ============================================================

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExample {

    // Logger is created ONCE per class, as a static final field.
    // LoggerFactory.getLogger(Class) ties this logger's name to the
    // class — so log output shows exactly which class produced it.
    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("  SLF4J Logging — Error & Warning Levels   ");
        System.out.println("===========================================\n");

        // ERROR level — something failed and needs attention
        logger.error("This is an error message");

        // WARN level — something unexpected, but not fatal
        logger.warn("This is a warning message");

        // For comparison — other levels (won't print by default if
        // the logback config's root level is set to "info" or higher)
        logger.info("This is an info message");
        logger.debug("This is a debug message (often hidden in production)");

        System.out.println("\n-------------------------------------------");
        System.out.println("Note: actual formatted output (timestamp, level,");
        System.out.println("logger name) is controlled by Logback's config —");
        System.out.println("not by this Java code. See logback.xml.");
        System.out.println("===========================================");
    }
}

/* ============================================================
   OPTIONAL: minimal logback.xml to place in src/main/resources
   so the above actually prints formatted output when run via Maven.
   ------------------------------------------------------------

   <configuration>
       <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
           <encoder>
               <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
           </encoder>
       </appender>

       <root level="debug">
           <appender-ref ref="console" />
       </root>
   </configuration>

   Sample console output you'd see:
   12:34:56.789 [main] ERROR LoggingExample - This is an error message
   12:34:56.791 [main] WARN  LoggingExample - This is a warning message
   12:34:56.792 [main] INFO  LoggingExample - This is an info message
   12:34:56.793 [main] DEBUG LoggingExample - This is a debug message
   ============================================================ */
