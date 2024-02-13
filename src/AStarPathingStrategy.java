import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        WorldNodePriority wnp = new WorldNodePriority();    // priority queue comparator
        PriorityQueue<WorldNode> openList = new PriorityQueue<WorldNode>(wnp);  // create openList
        WorldNode startNode = new WorldNode(                // create first WorldNode
                0,
                null,
                WorldNode.manhattanDistance(start, end),
                start);
        openList.add(startNode);                            // add node to openList

        Map<Point, WorldNode> closedList = new HashMap<>(); // create closedList & path list
        List<Point> path = new ArrayList<>();

        while (openList.size() > 0) {
            final WorldNode current = openList.remove();    // remove node at front of queue

            if (!withinReach.test(current.getPosition(), end)) {    // if not within reach ...
                List<WorldNode> nodeList = potentialNeighbors.apply(current.getPosition())
                        .filter(canPassThrough)                             // see if neighbor is valid
                        .filter(n -> !closedList.containsKey(n))            // see if closedList contains point
                        // can call .toList right here if I can't figure out streams
                        .map(n -> new WorldNode(                            // convert stream of points to stream of nodes
                                current.getDistanceFromStart() + 1,                  // distFromStart
                                current,                                        // prevNode
                                WorldNode.manhattanDistance(n, end),            // estDistToEnd
                                n))                                             // position
                        .collect(Collectors.toList());

                for (WorldNode node : nodeList) {
                    // if node is not in openList, add it to the list
                    if(!openList.contains(node)) {
                        openList.add(node);
                    }
                    // elif node is in openList but its g value is better, add it
                    else {
                        if (openList.removeIf(n -> (n.equals(node) && (n.getDistanceFromStart() < node.getDistanceFromStart())))) {
                            openList.add(node);
                        }
                    }
                }

                // add current node to closedList
                closedList.put(current.getPosition(), current);
            } else {
                WorldNode next = current;
                while (next.getPosition() != start) {
                    path.add(next.getPosition());
                    next = next.getPrevNode();
                }
                Collections.reverse(path);
                return path;
            }
        }
        return path;
    }

}
