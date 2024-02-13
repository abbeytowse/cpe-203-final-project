import processing.core.PImage;

import java.util.List;

public class Statue extends Entity {

    public static final String STATUE_KEY = "statue";

    public Statue(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images);
    }

}
