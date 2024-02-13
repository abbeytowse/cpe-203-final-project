import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background {

    public static final String SWAMP_KEY = "swamp";

    private final String id;
    private final List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public static PImage getCurrentImage(Background entity) {
        return entity.getImages().get(entity.getImageIndex());
    }

    public String getID() { return this.id; }

    public List<PImage> getImages() { return this.images; }

    public int getImageIndex() { return this.imageIndex; }


}
