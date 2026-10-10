import java.util.ArrayList;
import java.util.List;

public class Check12Robot implements Checker.Suite {
    /** A 12 x 12 field. shortest is the fewest steps from S to T, or -1 if T can't be reached. */
    public record FieldMap(String name, int shortest, String... rows) {
    }

    /** What happened when a path was followed: a problem (or null), and the tiles visited. */
    public record Walk(String problem, List<int[]> tiles) {
    }

    public static final List<FieldMap> MAPS = List.of(
            new FieldMap("Open field", 22,
                    "...........T",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "S..........."),
            new FieldMap("The wall", 29,
                    "T...........",
                    "............",
                    "............",
                    "............",
                    "#########...",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "S..........."),
            new FieldMap("The trap", 17,
                    "............",
                    "............",
                    "..#######...",
                    "........#...",
                    "........#...",
                    ".S......#.T.",
                    "........#...",
                    "........#...",
                    "..#######...",
                    "............",
                    "............",
                    "............"),
            new FieldMap("The maze", 44,
                    "S.#.....#...",
                    ".##.###.#.#.",
                    "....#...#.#.",
                    ".####.###.#.",
                    ".#....#...#.",
                    ".#.####.###.",
                    ".#......#...",
                    ".######.#.#.",
                    "......#.#.#.",
                    ".####.#.#.#.",
                    ".#....#...#.",
                    ".#.########T"),
            new FieldMap("Boxed in", -1,
                    "S...........",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    "............",
                    ".........###",
                    ".........#..",
                    ".........#.#",
                    ".........#.T"));

    public void run(Checker c) {
        Student s = c.s;
        c.gate("there is a SmartRobot class", () -> s.cls("SmartRobot"));
        c.gate("SmartRobot implements Robot", () ->
                c.yes(s.cls("Robot").isAssignableFrom(s.cls("SmartRobot")),
                        "SmartRobot does not say  implements Robot  yet."));
        c.gate("new SmartRobot() works", () -> s.make("SmartRobot"));

        for (int i = 0; i < MAPS.size(); i++) {
            FieldMap map = MAPS.get(i);
            int number = i + 1;
            String goal = map.shortest() < 0 ? "returns null because the target can't be reached"
                    : "reaches the target in " + map.shortest() + " steps";
            c.check("Field " + number + " (" + map.name() + "): " + goal, () -> {
                Object field = s.make("Field", new Object[] {map.rows()});
                Object result = s.call(s.make("SmartRobot"), "findPath", field);
                String see = "\nSee it with:  mise run field " + number;
                if (map.shortest() < 0) {
                    c.yes(result == null, "There is no way to the target, so findPath should return null.\n"
                            + "It returned a path with " + (result instanceof List<?> l ? l.size() : "?") + " steps." + see);
                    return;
                }
                c.yes(result != null, "findPath returned null, but this target can be reached." + see);
                c.yes(result instanceof List, "findPath should return a List<Direction>.");
                List<?> path = (List<?>) result;
                Walk walk = walk(map.rows(), path);
                c.yes(walk.problem() == null, walk.problem() + see);
                c.yes(path.size() == map.shortest(), "Your robot gets there, but it takes " + path.size()
                        + " steps. The shortest path is " + map.shortest() + " steps." + see);
            });
        }
    }

    /** Follows a path of UP / DOWN / LEFT / RIGHT steps from S and reports what went wrong, if anything. */
    public static Walk walk(String[] rows, List<?> path) {
        int[] at = find(rows, 'S');
        int[] target = find(rows, 'T');
        List<int[]> tiles = new ArrayList<>();
        int step = 0;
        for (Object direction : path) {
            step++;
            int row = at[0];
            int col = at[1];
            switch (String.valueOf(direction)) {
                case "UP" -> row--;
                case "DOWN" -> row++;
                case "LEFT" -> col--;
                case "RIGHT" -> col++;
                default -> {
                    return new Walk("Step " + step + " is " + direction + ", which is not a Direction.", tiles);
                }
            }
            if (row < 0 || row >= rows.length || col < 0 || col >= rows[row].length()) {
                return new Walk("Step " + step + " (" + direction + ") drives off the field from row "
                        + at[0] + ", col " + at[1] + ".", tiles);
            }
            if (rows[row].charAt(col) == '#') {
                return new Walk("Step " + step + " (" + direction + ") crashes into the wall at row "
                        + row + ", col " + col + ".", tiles);
            }
            at = new int[] {row, col};
            tiles.add(at);
        }
        if (at[0] != target[0] || at[1] != target[1]) {
            return new Walk("The path stops at row " + at[0] + ", col " + at[1] + ", but the target is at row "
                    + target[0] + ", col " + target[1] + ".", tiles);
        }
        return new Walk(null, tiles);
    }

    /** Draws the field, with * on every tile the path visits. */
    public static String draw(String[] rows, List<?> path) {
        char[][] grid = new char[rows.length][];
        for (int row = 0; row < rows.length; row++) {
            grid[row] = rows[row].toCharArray();
        }
        if (path != null) {
            for (int[] tile : walk(rows, path).tiles()) {
                if (grid[tile[0]][tile[1]] == '.') {
                    grid[tile[0]][tile[1]] = '*';
                }
            }
        }
        StringBuilder text = new StringBuilder("                          1 1\n      0 1 2 3 4 5 6 7 8 9 0 1   <- col\n");
        for (int row = 0; row < grid.length; row++) {
            text.append(String.format("  %2d ", row));
            for (char tile : grid[row]) {
                text.append(' ').append(tile);
            }
            text.append('\n');
        }
        return text.append("   ^ row\n").toString();
    }

    private static int[] find(String[] rows, char wanted) {
        for (int row = 0; row < rows.length; row++) {
            int col = rows[row].indexOf(wanted);
            if (col >= 0) {
                return new int[] {row, col};
            }
        }
        throw new IllegalArgumentException("no " + wanted + " on the field");
    }
}
