import java.util.ArrayList;
import java.util.List;

// A robot that is not very smart. It drives straight toward the target:
// first up or down until it is on the right row, then left or right until
// it is on the right column. It never checks for walls.
//
// It is here as an example of a class that implements the Robot interface.
public class BeelineRobot implements Robot {

    @Override
    public List<Direction> findPath(Field field) {
        List<Direction> path = new ArrayList<>();
        Position at = field.getStart();
        Position target = field.getTarget();

        while (at.row() != target.row()) {
            Direction direction = at.row() < target.row() ? Direction.DOWN : Direction.UP;
            path.add(direction);
            at = at.step(direction);
        }
        while (at.col() != target.col()) {
            Direction direction = at.col() < target.col() ? Direction.RIGHT : Direction.LEFT;
            path.add(direction);
            at = at.step(direction);
        }
        return path;
    }
}
