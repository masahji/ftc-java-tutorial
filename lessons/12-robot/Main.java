import java.util.List;

// Runs a robot on a field and draws where it went.
// Run it with:  mise run try 12
// Change anything you like in here. The checker does not look at this file.
public class Main {
    public static void main(String[] args) {
        Field field = new Field(
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
                "S...........");

        // Once you have written SmartRobot, change this line to use it.
        // Notice that nothing else in this file has to change. Why not?
        Robot robot = new BeelineRobot();

        List<Direction> path = robot.findPath(field);

        System.out.println("The field:");
        System.out.println(field);
        if (path == null) {
            System.out.println("The robot says there is no way to the target.");
            return;
        }
        System.out.println("The robot's " + path.size() + " moves: " + path);
        System.out.println(field.draw(path));

        // Follow the path and report how it went.
        Position at = field.getStart();
        for (Direction direction : path) {
            at = at.step(direction);
            if (!field.isOpen(at)) {
                System.out.println("CRASH! The robot hit a wall (or left the field) at " + at);
                return;
            }
        }
        if (at.equals(field.getTarget())) {
            System.out.println("The robot reached the target.");
        } else {
            System.out.println("The robot stopped at " + at + ", which is not the target.");
        }
    }
}
