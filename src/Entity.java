import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(
            String id,
            Point position,
            List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public static PImage getCurrentImage(Entity entity) {
        return entity.getImages().get(entity.getImageIndex());
    }

    public String getID() { return this.id; }

    public Point getPosition() { return this.position; }
    public void setPosition(Point pos) { this.position = pos; }

    public List<PImage> getImages() { return this.images; }

    public int getImageIndex() { return this.imageIndex; }
    public void setImageIndex(int imageIndex) {this.imageIndex = imageIndex;}

}
