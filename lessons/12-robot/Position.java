// One tile on the field. Row 0 is the top row, col 0 is the left column.
//
// A record is a short way to write a class that just holds data. Java
// creates the constructor, the methods row() and col(), toString, and also
// equals and hashCode. Thanks to those last two, two Positions with the same
// row and col count as equal, so Positions work as HashMap keys and in a
// HashSet.
public record Position(int row, int col) {

    // The Position one tile away in the given direction.
    // This does not change this Position. It returns a new one.
    public Position step(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }
}
