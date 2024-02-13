import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Villager extends Dude {

    public static final String VILLAGER_KEY = "villager";

    public Villager(
            String id,
            Point position,
            int animationPeriod,
            List<PImage> images,
            int actionPeriod)
    {
        super(id, position, animationPeriod, images, actionPeriod, 0, 0);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(House.class)));
        this.executeActivityHelper(world, imageStore, scheduler, target);
    }

    @Override
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore,
            Optional<Entity> target)
    {
        DudeNotFull dude = new DudeNotFull(this.getID(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.resourceLimit,
                imageStore.getImageList("dude"), 0);

        return this.transformHelper(world, scheduler, imageStore, dude);
    }

}
