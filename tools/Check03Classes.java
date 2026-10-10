import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Check03Classes implements Checker.Suite {
    public void run(Checker c) {
        Student s = c.s;
        c.gate("there is a Motor class", () -> s.cls("Motor"));
        c.gate("new Motor(\"leftDrive\") works", () -> s.make("Motor", "leftDrive"));

        c.section("Fields and constructor");
        c.check("getName() returns the name given to the constructor", () -> {
            c.eq("new Motor(\"leftDrive\").getName()", "leftDrive", s.call(s.make("Motor", "leftDrive"), "getName"));
            c.eq("new Motor(\"arm\").getName()", "arm", s.call(s.make("Motor", "arm"), "getName"));
        });
        c.check("a new motor starts with power 0.0", () ->
                c.eq("getPower() on a brand new motor", 0.0, s.call(s.make("Motor", "arm"), "getPower")));
        c.check("every field is private", () -> {
            Field[] fields = s.cls("Motor").getDeclaredFields();
            c.yes(fields.length > 0, "Motor has no fields. Where does it remember its name and power?");
            for (Field field : fields) {
                c.yes(Modifier.isPrivate(field.getModifiers()),
                        "The field " + field.getName() + " is not private.");
            }
        });
        c.check("no field is static", () -> {
            for (Field field : s.cls("Motor").getDeclaredFields()) {
                c.yes(!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()),
                        "The field " + field.getName() + " is static, so every Motor would share one copy of it.");
            }
        });

        c.section("setPower");
        c.check("setPower(0.5) then getPower() gives 0.5", () -> {
            Object motor = s.make("Motor", "arm");
            s.call(motor, "setPower", 0.5);
            c.eq("getPower() after setPower(0.5)", 0.5, s.call(motor, "getPower"));
        });
        c.check("setPower(-0.25) then getPower() gives -0.25", () -> {
            Object motor = s.make("Motor", "arm");
            s.call(motor, "setPower", -0.25);
            c.eq("getPower() after setPower(-0.25)", -0.25, s.call(motor, "getPower"));
        });
        c.check("power above 1.0 is limited to 1.0", () -> {
            Object motor = s.make("Motor", "arm");
            s.call(motor, "setPower", 3.0);
            c.eq("getPower() after setPower(3.0)", 1.0, s.call(motor, "getPower"));
        });
        c.check("power below -1.0 is limited to -1.0", () -> {
            Object motor = s.make("Motor", "arm");
            s.call(motor, "setPower", -7.5);
            c.eq("getPower() after setPower(-7.5)", -1.0, s.call(motor, "getPower"));
        });
        c.check("exactly 1.0 and -1.0 are allowed", () -> {
            Object motor = s.make("Motor", "arm");
            s.call(motor, "setPower", 1.0);
            c.eq("getPower() after setPower(1.0)", 1.0, s.call(motor, "getPower"));
            s.call(motor, "setPower", -1.0);
            c.eq("getPower() after setPower(-1.0)", -1.0, s.call(motor, "getPower"));
        });

        c.section("isRunning and stop");
        c.check("a new motor is not running", () ->
                c.eq("isRunning() on a brand new motor", false, s.call(s.make("Motor", "arm"), "isRunning")));
        c.check("a motor with forward power is running", () -> {
            Object motor = s.make("Motor", "arm");
            s.call(motor, "setPower", 0.3);
            c.eq("isRunning() after setPower(0.3)", true, s.call(motor, "isRunning"));
        });
        c.check("a motor going backward is running too", () -> {
            Object motor = s.make("Motor", "arm");
            s.call(motor, "setPower", -0.3);
            c.eq("isRunning() after setPower(-0.3)", true, s.call(motor, "isRunning"));
        });
        c.check("stop() sets the power back to 0.0", () -> {
            Object motor = s.make("Motor", "arm");
            s.call(motor, "setPower", 0.8);
            s.call(motor, "stop");
            c.eq("getPower() after stop()", 0.0, s.call(motor, "getPower"));
            c.eq("isRunning() after stop()", false, s.call(motor, "isRunning"));
        });

        c.section("Objects are separate");
        c.check("two motors keep their own name and power", () -> {
            Object left = s.make("Motor", "left");
            Object right = s.make("Motor", "right");
            s.call(left, "setPower", 0.5);
            s.call(right, "setPower", -1.0);
            c.eq("left.getName()", "left", s.call(left, "getName"));
            c.eq("left.getPower() after right.setPower(-1.0)", 0.5, s.call(left, "getPower"));
            c.eq("right.getPower()", -1.0, s.call(right, "getPower"));
        });
    }
}
