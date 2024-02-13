import processing.core.PImage;

import java.util.List;

public class Log extends Entity{

    public static final String LOG_KEY = "log";

    public Log(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images);
    }

}
