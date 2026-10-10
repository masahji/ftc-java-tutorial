import java.util.List;

// A 12 x 12 field of tiles. Some tiles are walls.
//
// A field is built from 12 Strings of 12 characters each:
//   S = where the robot starts     T = the target
//   # = a wall                     . = an open tile
public class Field {
    public static final int SIZE = 12;

    private final boolean[][] wall = new boolean[SIZE][SIZE];
    private Position start;
    private Position target;

    public Field(String... rows) {
        if (rows.length != SIZE) {
            throw new IllegalArgumentException("A field needs " + SIZE + " rows, but got " + rows.length);
        }
        for (int row = 0; row < SIZE; row++) {
            if (rows[row].length() != SIZE) {
                throw new IllegalArgumentException("Row " + row + " needs " + SIZE + " tiles: " + rows[row]);
            }
            for (int col = 0; col < SIZE; col++) {
                char tile = rows[row].charAt(col);
                if (tile == '#') {
                    wall[row][col] = true;
                } else if (tile == 'S') {
                    start = new Position(row, col);
                } else if (tile == 'T') {
                    target = new Position(row, col);
                }
            }
        }
        if (start == null || target == null) {
            throw new IllegalArgumentException("A field needs one S and one T");
        }
    }

    public Position getStart() {
        return start;
    }

    public Position getTarget() {
        return target;
    }

    // True if a robot may stand on this tile: it is on the field and it is
    // not a wall.
    public boolean isOpen(Position position) {
        int row = position.row();
        int col = position.col();
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && !wall[row][col];
    }

    // A picture of the field. If path is not null, the tiles the path visits
    // are drawn as *. The drawing stops early if the path hits a wall.
    public String draw(List<Direction> path) {
        char[][] picture = new char[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                picture[row][col] = wall[row][col] ? '#' : '.';
            }
        }
        if (path != null) {
            Position at = start;
            for (Direction direction : path) {
                at = at.step(direction);
                if (!isOpen(at)) {
                    break;
                }
                picture[at.row()][at.col()] = '*';
            }
        }
        picture[start.row()][start.col()] = 'S';
        picture[target.row()][target.col()] = 'T';

        StringBuilder text = new StringBuilder();
        for (char[] row : picture) {
            for (char tile : row) {
                text.append(' ').append(tile);
            }
            text.append('\n');
        }
        return text.toString();
    }

    @Override
    public String toString() {
        return draw(null);
    }
}
