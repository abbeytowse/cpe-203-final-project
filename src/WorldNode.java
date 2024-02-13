import static java.lang.Math.abs;

public class WorldNode {

    private int distanceFromStart;
    private WorldNode prevNode;
    private int estimatedDistanceToEnd;
    private Point position;
    private int heuristic;

    public WorldNode(int distFromStart, WorldNode prevNode, int distToEnd, Point pos) {
        this.distanceFromStart = distFromStart;
        this.prevNode = prevNode;
        this.estimatedDistanceToEnd = distToEnd;
        this.position = pos;
        this.heuristic = distanceFromStart + estimatedDistanceToEnd;
    }

    public Point getPosition() { return this.position; }
    public int getDistanceFromStart() { return this.distanceFromStart; }
    public int getHeuristic() { return this.heuristic; }
    public WorldNode getPrevNode() { return this.prevNode; }

    static int manhattanDistance(Point start, Point end) {
        return abs(end.getX() - start.getX()) + abs(end.getY() - start.getY());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) { return false; }
        if (this == other) { return true; }
        if (!(other instanceof WorldNode)) { return false; }

        WorldNode otherNode = (WorldNode) other;
        return this.position.equals(otherNode.position);
    }

}
