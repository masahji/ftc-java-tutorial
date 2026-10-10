import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Lets the checker poke at a student's compiled classes by name, so the
 * checker still runs (and can say something helpful) when a class or method
 * hasn't been written yet.
 */
public class Student {
    final ClassLoader loader;
    final String dir;

    Student(ClassLoader loader, String dir) {
        this.loader = loader;
        this.dir = dir;
    }

    public boolean has(String className) {
        try {
            Class.forName(className, false, loader);
            return true;
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        }
    }

    public Class<?> cls(String className) {
        try {
            return Class.forName(className, true, loader);
        } catch (ClassNotFoundException e) {
            throw new Checker.Fail("I can't find anything named " + className + ".\n"
                    + "Create the file " + dir + "/" + className + ".java");
        }
    }

    /** The student's source file, with comments and string contents removed. */
    public String code(String fileName) throws IOException {
        String source = Files.readString(Path.of(dir, fileName));
        return source
                .replaceAll("(?s)/\\*.*?\\*/", " ")
                .replaceAll("//[^\n]*", " ")
                .replaceAll("\"([^\"\\\\\n]|\\\\.)*\"", "\"\"");
    }

    /** new ClassName(args...) */
    public Object make(String className, Object... args) throws Throwable {
        Class<?> type = cls(className);
        if (Modifier.isAbstract(type.getModifiers())) {
            throw new Checker.Fail(className + " is abstract (or an interface), so I can't create one with new.");
        }
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            Object[] fitted = fit(constructor, args);
            if (fitted != null) {
                constructor.setAccessible(true);
                try {
                    return constructor.newInstance(fitted);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            }
        }
        List<String> found = new ArrayList<>();
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            found.add(className + "(" + params(constructor) + ")");
        }
        throw new Checker.Fail("I tried new " + className + "(" + types(args) + ") but there is no constructor like that.\n"
                + "Constructors I found: " + String.join(", ", found));
    }

    /** target.method(args...) */
    public Object call(Object target, String method, Object... args) throws Throwable {
        if (target == null) {
            throw new Checker.Fail("I wanted to call " + method + "(...) but the object was null.");
        }
        return invoke(target.getClass(), target, method, args);
    }

    /** ClassName.method(args...) */
    public Object callStatic(String className, String method, Object... args) throws Throwable {
        return invoke(cls(className), null, method, args);
    }

    private Object invoke(Class<?> type, Object target, String name, Object[] args) throws Throwable {
        Set<Method> sameName = new LinkedHashSet<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.getName().equals(name) && !m.isBridge()) {
                    sameName.add(m);
                }
            }
        }
        for (Method m : type.getMethods()) {
            if (m.getName().equals(name) && !m.isBridge()) {
                sameName.add(m);
            }
        }
        String wanted = type.getSimpleName() + "." + name + "(" + types(args) + ")";
        for (Method m : sameName) {
            Object[] fitted = fit(m, args);
            if (fitted == null) {
                continue;
            }
            boolean isStatic = Modifier.isStatic(m.getModifiers());
            if (target == null && !isStatic) {
                throw new Checker.Fail(wanted + " needs to be a static method.");
            }
            m.setAccessible(true);
            try {
                return m.invoke(isStatic ? null : target, fitted);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
        if (sameName.isEmpty()) {
            throw new Checker.Fail("I looked for " + wanted + " but " + type.getSimpleName()
                    + " has no method named " + name + ".");
        }
        List<String> found = new ArrayList<>();
        for (Method m : sameName) {
            found.add(name + "(" + params(m) + ")");
        }
        throw new Checker.Fail("I looked for " + wanted + " but the parameters don't match.\n"
                + "I found: " + String.join(", ", found));
    }

    // ---- matching arguments to parameters ---------------------------------

    private static final Map<Class<?>, Class<?>> WRAPPERS = Map.of(
            int.class, Integer.class, long.class, Long.class, double.class, Double.class,
            float.class, Float.class, boolean.class, Boolean.class, char.class, Character.class,
            short.class, Short.class, byte.class, Byte.class);

    /** Returns the arguments converted to fit, or null if they don't fit. */
    private static Object[] fit(Executable executable, Object[] args) {
        Class<?>[] params = executable.getParameterTypes();
        if (params.length != args.length) {
            return null;
        }
        Object[] fitted = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            Class<?> param = WRAPPERS.getOrDefault(params[i], params[i]);
            Object arg = args[i];
            if (arg == null) {
                if (params[i].isPrimitive()) {
                    return null;
                }
            } else if (param.isInstance(arg)) {
                fitted[i] = arg;
            } else if (arg instanceof Number n && (param == Double.class || param == Float.class)) {
                fitted[i] = param == Double.class ? (Object) n.doubleValue() : (Object) n.floatValue();
            } else if (arg instanceof Number n && (param == Integer.class || param == Long.class)
                    && n.doubleValue() == Math.rint(n.doubleValue())) {
                fitted[i] = param == Integer.class ? (Object) n.intValue() : (Object) n.longValue();
            } else {
                return null;
            }
        }
        return fitted;
    }

    private static String params(Executable executable) {
        List<String> names = new ArrayList<>();
        for (Class<?> param : executable.getParameterTypes()) {
            names.add(param.getSimpleName());
        }
        return String.join(", ", names);
    }

    private static String types(Object[] args) {
        List<String> names = new ArrayList<>();
        for (Object arg : args) {
            if (arg == null) {
                names.add("null");
            } else if (arg instanceof Integer) {
                names.add("int");
            } else if (arg instanceof Double) {
                names.add("double");
            } else if (arg instanceof List) {
                names.add("List");
            } else {
                names.add(arg.getClass().getSimpleName());
            }
        }
        return String.join(", ", names);
    }
}
