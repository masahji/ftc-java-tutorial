import java.util.List;

// Anything that can plan a route across a field is a Robot.
public interface Robot {

    // Returns the moves that take the robot from the field's start tile to
    // its target tile, in order. Returns null if there is no way to get there.
    List<Direction> findPath(Field field);
}
