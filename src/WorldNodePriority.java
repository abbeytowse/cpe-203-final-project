import java.util.Comparator;

public class WorldNodePriority implements Comparator<WorldNode> {
    @Override
    public int compare(WorldNode n1, WorldNode n2) {
        return n1.getHeuristic() - n2.getHeuristic();
    }

}
