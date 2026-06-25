// ============================================================
// WHAT IS FACTORY METHOD PATTERN?
//   A creational pattern where a factory class decides WHICH
//   object to create based on input — the client never uses
//   `new ConcreteClass()` directly.
//
// WHY FACTORY vs DIRECT INSTANTIATION?
//
//   Direct (tightly coupled — BAD for scaling):
//     Shape s = new Circle();    // must edit client for every new type
//
//   Factory (loosely coupled — GOOD):
//     Shape s = ShapeFactory.getShape("CIRCLE");  // client never changes
//
//   Adding a new shape? Only update ShapeFactory. Nothing else changes.
//   This is the Open/Closed Principle (OCP) in action.
// ============================================================

// ──────────────────────────────────────────────
//  SHAPE INTERFACE
// ──────────────────────────────────────────────
interface Shape {
    void draw();
    double calculateArea();
}

// ──────────────────────────────────────────────
//  CONCRETE SHAPES
// ──────────────────────────────────────────────
class Circle implements Shape {
    private double radius;

    Circle(double radius) { this.radius = radius; }

    @Override
    public void draw() {
        System.out.println("  Drawing Circle  [radius = " + radius + "]");
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle implements Shape {
    private double width, height;

    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("  Drawing Rectangle  [" + width + " x " + height + "]");
    }

    @Override
    public double calculateArea() {
        return width * height;
    }
}

class Triangle implements Shape {
    private double base, height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("  Drawing Triangle  [base = " + base + ", height = " + height + "]");
    }

    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }
}

// ──────────────────────────────────────────────
//  SHAPE FACTORY
// ──────────────────────────────────────────────
class ShapeFactory {

    // Returns the right Shape based on the string type.
    // Client code never needs to know about Circle, Rectangle, or Triangle.
    public static Shape getShape(String shapeType) {
        if (shapeType == null) return null;

        switch (shapeType.toUpperCase()) {
            case "CIRCLE":    return new Circle(7.0);
            case "RECTANGLE": return new Rectangle(5.0, 3.0);
            case "TRIANGLE":  return new Triangle(6.0, 4.0);
            default:
                System.out.println("  [ShapeFactory] Unknown shape type: " + shapeType);
                return null;
        }
    }
}

// ──────────────────────────────────────────────
//  TEST CLASS
// ──────────────────────────────────────────────
class ShapeFactoryTest {

    public static void runTests() {
        String[] shapesToTest = {"CIRCLE", "RECTANGLE", "TRIANGLE"};

        for (String type : shapesToTest) {
            System.out.println("\n>> Requesting: " + type);
            Shape shape = ShapeFactory.getShape(type);  // No 'new XYZ()' here

            if (shape != null) {
                shape.draw();
                System.out.printf("   Area = %.2f square units%n", shape.calculateArea());
                System.out.println("   PASS: " + type + " created and used successfully.");
            }
        }

        // Edge case — unknown type
        System.out.println("\n>> Requesting: HEXAGON (unknown type)");
        Shape unknown = ShapeFactory.getShape("HEXAGON");
        if (unknown == null) {
            System.out.println("   PASS: Factory correctly returned null for unknown type.");
        }
    }
}

// ──────────────────────────────────────────────
//  MAIN CLASS
// ──────────────────────────────────────────────
public class FactoryMethodPatternExample {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("  Factory Method Pattern — Shape Demo      ");
        System.out.println("===========================================");

        ShapeFactoryTest.runTests();

        System.out.println("\n===========================================");
        System.out.println("  Client code never used `new Circle()` etc.");
        System.out.println("  Factory handled all creation — OCP satisfied.");
        System.out.println("===========================================");
    }
}