import processing.core.PImage;

import java.util.List;

public class DeadTree extends Entity {

    public static final String DEAD_TREE_KEY = "dead_tree";

    public DeadTree(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images);
    }

}
