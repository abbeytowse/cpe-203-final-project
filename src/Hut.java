import processing.core.PImage;

import java.util.List;

public class Hut extends Entity {

    public static final String HUT_KEY = "hut";

    public Hut(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images);
    }

}
