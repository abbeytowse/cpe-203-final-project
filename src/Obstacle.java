import processing.core.PImage;
import java.util.List;

public class Obstacle extends AnimatedEntity {

    public Obstacle(
            String id,
            Point position,
            int animationPeriod,
            List<PImage> images)
    {
        super(id, position, animationPeriod, images);
    }

}
