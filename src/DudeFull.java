import processing.core.PImage;

import java.util.*;

public class DudeFull extends Dude {

    public DudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        super(id, position, animationPeriod, images, actionPeriod, resourceLimit, 0);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(House.class, Hut.class)));
        this.executeActivityHelper(world, imageStore, scheduler, target);
    }

    @Override
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore,
            Optional<Entity> target)
    {
        if (target.get() instanceof House) {
            DudeNotFull dude = new DudeNotFull(this.getID(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.resourceLimit,
                    this.getImages(), 0);
            return this.transformHelper(world, scheduler, imageStore, dude);
        }
        else {
            Villager villager = new Villager(
                    this.getID(),
                    this.getPosition(),
                    50,
                    imageStore.getImageList(Villager.VILLAGER_KEY),
                    2000
            );
            return this.transformHelper(world, scheduler, imageStore, villager);
        }
    }

}
