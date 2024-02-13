import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Plant extends ActiveEntity {
    protected int health;

    public Plant(String id, Point position, int animationPeriod, List<PImage> images, int actionPeriod, int health) {
        super(id, position, animationPeriod, images, actionPeriod);
        this.health = health;
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if (!this.transform(world, scheduler, imageStore)) {
            super.executeActivity(world, imageStore, scheduler);
        }
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore
            )
    {
        if (this.health <= 0) {
            Stump stump = new Stump(this.getID(),
                    this.getPosition(),
                    imageStore.getImageList(Stump.STUMP_KEY));
            return transformHelper(world, scheduler, imageStore, stump);
        }
        return false;
    }

    boolean transformHelper(WorldModel world, EventScheduler scheduler, ImageStore imageStore, Entity wood) {
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(wood);
        this.scheduleActions(scheduler, world, imageStore);

        return true;
    }

    public void decrementHealth() { this.health--; }
    public int getHealth() { return this.health; }

}
