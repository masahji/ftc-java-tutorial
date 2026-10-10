import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * The program behind the mise tasks: shows lessons, runs your code, checks it.
 *
 *   java tools/Tutor.java list
 *   java tools/Tutor.java lesson 3
 *   java tools/Tutor.java try 3
 *   java tools/Tutor.java check 3
 *   java tools/Tutor.java field 2
 */
public class Tutor {
    record Lesson(int number, String title, String dir, String file, String edit, String mainClass, Checker.Suite suite) {
    }

    static final String SHAPES = "lessons/04-07-shapes";

    static final List<Lesson> LESSONS = List.of(
            new Lesson(1, "Hello, World and static methods", "lessons/01-hello", "LESSON.md", "Hello.java", "Hello", new Check01Hello()),
            new Lesson(2, "Warm-up: methods, ifs and loops", "lessons/02-warmup", "LESSON.md", "Warmup.java", "Main", new Check02Warmup()),
            new Lesson(3, "Classes and objects", "lessons/03-classes", "LESSON.md", "Motor.java", "Main", new Check03Classes()),
            new Lesson(4, "Inheritance", SHAPES, "LESSON-04.md", "Rectangle.java, Square.java", "Main", new Check04Inheritance()),
            new Lesson(5, "Abstract classes and polymorphism", SHAPES, "LESSON-05.md", "Shape.java, Triangle.java, Shapes.java", "Main", new Check05Abstract()),
            new Lesson(6, "Interfaces", SHAPES, "LESSON-06.md", "Measurable.java, Shape.java, Tile.java, Measure.java", "Main", new Check06Interfaces()),
            new Lesson(7, "Color and method chaining", SHAPES, "LESSON-07.md", "Color.java, Shape.java", "Main", new Check07Chaining()),
            new Lesson(8, "Lists and simple algorithms", "lessons/08-lists", "LESSON.md", "ListAlgorithms.java", "Main", new Check08Lists()),
            new Lesson(9, "HashMap: counting things", "lessons/09-hashmaps", "LESSON.md", "Frequency.java", "Main", new Check09HashMaps()),
            new Lesson(10, "Recursion", "lessons/10-recursion", "LESSON.md", "Recursion.java", "Main", new Check10Recursion()),
            new Lesson(11, "Towers of Hanoi", "lessons/11-hanoi", "LESSON.md", "Hanoi.java", "Main", new Check11Hanoi()),
            new Lesson(12, "Challenge: the smart robot", "lessons/12-robot", "LESSON.md", "SmartRobot.java", "Main", new Check12Robot()));

    static final Path PROGRESS = Path.of(".progress");
    static final String RULE = "------------------------------------------------------------------------";
    static final boolean COLOR = System.getenv("NO_COLOR") == null
            && System.console() != null && System.console().isTerminal();

    public static void main(String[] args) throws Exception {
        String command = args.length > 0 ? args[0] : "list";
        String arg = args.length > 1 ? args[1].trim() : "";
        switch (command) {
            case "list" -> list();
            case "lesson" -> show(pick(arg));
            case "try" -> tryIt(pick(arg));
            case "check" -> check(pick(arg));
            case "field" -> field(arg);
            default -> System.out.println("Unknown command: " + command);
        }
        System.out.flush();
        System.exit(0);
    }

    // ---- commands ----------------------------------------------------------

    static void list() {
        Set<Integer> done = done();
        System.out.println();
        System.out.println("Java Basics");
        System.out.println();
        for (Lesson lesson : LESSONS) {
            String mark = done.contains(lesson.number()) ? "[done]" : "[    ]";
            System.out.printf("  %s  %2d  %s%n", mark, lesson.number(), lesson.title());
        }
        System.out.println();
        if (done.size() == LESSONS.size()) {
            System.out.println("You finished every lesson. Nice work!");
        } else {
            int next = next().number();
            System.out.println("Read your next lesson:  mise run lesson " + next);
            System.out.println("Check your work:        mise run check " + next);
        }
        System.out.println();
    }

    static void show(Lesson lesson) throws IOException {
        System.out.println();
        System.out.println(RULE);
        System.out.println("Lesson " + lesson.number() + ": " + lesson.title());
        System.out.println(RULE);
        System.out.println("Folder:        " + lesson.dir() + "/");
        System.out.println("Files to edit: " + lesson.edit());
        System.out.println();
        System.out.println(Files.readString(Path.of(lesson.dir(), lesson.file())).strip());
        System.out.println();
        System.out.println(RULE);
        System.out.println("Folder:          " + lesson.dir() + "/");
        System.out.println("Files to edit:   " + lesson.edit());
        System.out.println("Run your code:   mise run try " + lesson.number());
        System.out.println("Check your work: mise run check " + lesson.number());
        System.out.println(RULE);
        System.out.println();
    }

    static void tryIt(Lesson lesson) throws Exception {
        Student student = compile(lesson);
        if (student == null) {
            return;
        }
        if (!student.has(lesson.mainClass())) {
            System.out.println("There is no " + lesson.mainClass() + ".java in " + lesson.dir() + "/ to run.");
            return;
        }
        try {
            Method main = student.cls(lesson.mainClass()).getDeclaredMethod("main", String[].class);
            main.setAccessible(true);
            main.invoke(null, (Object) new String[0]);
        } catch (NoSuchMethodException e) {
            System.out.println(lesson.mainClass() + " needs a method:  public static void main(String[] args)");
        } catch (InvocationTargetException e) {
            System.out.flush();
            e.getCause().printStackTrace();
        }
    }

    static void check(Lesson lesson) throws Exception {
        System.out.println();
        System.out.println("Checking lesson " + lesson.number() + ": " + lesson.title());
        System.out.println();
        Student student = compile(lesson);
        if (student == null) {
            return;
        }
        Checker checker = new Checker(student, System.out, COLOR);
        try {
            lesson.suite().run(checker);
        } catch (Checker.Stop e) {
            // a required check failed; the reason was already printed
        } catch (Throwable e) {
            checker.check("the checker ran into something unexpected", () -> {
                throw e;
            });
        }
        System.out.println();
        if (checker.failed == 0 && checker.passed > 0) {
            markDone(lesson.number());
            System.out.println(checker.paint("32", "All " + checker.passed + " checks passed. Lesson "
                    + lesson.number() + " complete!"));
            if (lesson.number() < LESSONS.size()) {
                System.out.println("Next up:  mise run lesson " + (lesson.number() + 1));
            } else {
                System.out.println("That was the last one. You finished the whole course!");
            }
        } else {
            System.out.println(checker.passed + " passed, " + checker.failed + " failed.");
            System.out.println("Files to edit:  " + lesson.edit() + "  (in " + lesson.dir() + "/)");
            System.out.println("Fix the first [FAIL], then run:  mise run check " + lesson.number());
        }
        System.out.println();
    }

    /** Prints one of the challenge fields, with your robot's path if you have one. */
    static void field(String arg) throws Exception {
        List<Check12Robot.FieldMap> maps = Check12Robot.MAPS;
        int number = arg.isEmpty() ? 1 : parse(arg);
        if (number < 1 || number > maps.size()) {
            System.out.println("Pick a field from 1 to " + maps.size() + ", like:  mise run field 2");
            return;
        }
        Check12Robot.FieldMap map = maps.get(number - 1);
        System.out.println();
        System.out.println("Field " + number + " of " + maps.size() + ": " + map.name());
        System.out.println("  S = start   T = target   # = wall   . = open tile   * = your robot's path");
        System.out.println();

        Lesson lesson = LESSONS.get(11);
        List<?> path = null;
        String note = "No SmartRobot found in " + lesson.dir() + "/.";
        Student student = compile(lesson);
        if (student != null && student.has("SmartRobot")) {
            try {
                Object field = student.make("Field", new Object[] {map.rows()});
                Object result = student.call(student.make("SmartRobot"), "findPath", field);
                if (result instanceof List<?> list) {
                    path = list;
                    Check12Robot.Walk walk = Check12Robot.walk(map.rows(), list);
                    note = "Your robot took " + list.size() + " steps. "
                            + (walk.problem() != null ? walk.problem()
                            : list.size() == map.shortest() ? "That is the shortest possible!"
                            : "The shortest possible is " + map.shortest() + ".");
                } else {
                    note = "Your robot returned " + result + " (no path)."
                            + (map.shortest() < 0 ? " Correct: this target can't be reached." : "");
                }
            } catch (Throwable e) {
                note = "Your robot crashed: " + (e instanceof Checker.Fail ? e.getMessage() : e.toString());
            }
        }
        System.out.println(Check12Robot.draw(map.rows(), path));
        if (map.shortest() >= 0) {
            System.out.println("Shortest possible path: " + map.shortest() + " steps");
        } else {
            System.out.println("There is no way to reach this target.");
        }
        System.out.println(note);
        System.out.println();
    }

    // ---- compiling ---------------------------------------------------------

    /** Compiles every .java file in the lesson folder. Returns null if javac found errors. */
    static Student compile(Lesson lesson) throws IOException {
        Path source = Path.of(lesson.dir());
        Path out = Path.of("out", source.getFileName().toString());
        deleteTree(out);
        Files.createDirectories(out);

        List<String> args = new ArrayList<>(List.of("-d", out.toString(), "-encoding", "UTF-8", "-nowarn", "-g"));
        try (Stream<Path> files = Files.list(source)) {
            files.filter(file -> file.toString().endsWith(".java")).sorted().forEach(file -> args.add(file.toString()));
        }
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        if (javac == null) {
            System.out.println("I can't find the Java compiler. Run:  mise install");
            return null;
        }
        System.err.flush();
        if (javac.run(null, null, null, args.toArray(String[]::new)) != 0) {
            System.err.flush();
            System.out.println();
            System.out.println("Java could not compile your code, so nothing was run.");
            System.out.println("Start with the FIRST error above. It names the file and the line number.");
            System.out.println("Fix it, then run the same command again.");
            System.out.println();
            return null;
        }
        URL[] classpath = {out.toUri().toURL()};
        return new Student(new URLClassLoader(classpath, ClassLoader.getPlatformClassLoader()), lesson.dir());
    }

    static void deleteTree(Path root) throws IOException {
        if (!Files.exists(root)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(root)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(path);
            }
        }
    }

    // ---- picking a lesson and remembering progress -------------------------

    static Lesson pick(String arg) {
        if (arg.isEmpty()) {
            return next();
        }
        int number = parse(arg);
        if (number < 1 || number > LESSONS.size()) {
            System.out.println("There is no lesson \"" + arg + "\". Lessons go from 1 to " + LESSONS.size() + ".");
            System.out.println("See them all with:  mise run lessons");
            System.exit(0);
        }
        return LESSONS.get(number - 1);
    }

    static int parse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** The first lesson that hasn't been passed yet. */
    static Lesson next() {
        Set<Integer> done = done();
        for (Lesson lesson : LESSONS) {
            if (!done.contains(lesson.number())) {
                return lesson;
            }
        }
        return LESSONS.get(LESSONS.size() - 1);
    }

    static Set<Integer> done() {
        Set<Integer> done = new TreeSet<>();
        try {
            for (String line : Files.readAllLines(PROGRESS)) {
                int number = parse(line.trim());
                if (number > 0) {
                    done.add(number);
                }
            }
        } catch (IOException e) {
            // no progress file yet
        }
        return done;
    }

    static void markDone(int number) throws IOException {
        Set<Integer> done = done();
        done.add(number);
        List<String> lines = new ArrayList<>();
        for (int n : done) {
            lines.add(String.valueOf(n));
        }
        Files.write(PROGRESS, lines);
    }
}
