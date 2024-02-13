import processing.core.PImage;


import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class Dude extends MoveableEntity {

    protected int resourceLimit;
    protected int resourceCount;

    public Dude(
            String id,
            Point position,
            int animationPeriod,
            List<PImage> images,
            int actionPeriod,
            int resourceLimit,
            int resourceCount) {
        super(id, position, animationPeriod, images, actionPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public void executeActivityHelper(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler,
            Optional<Entity> target)
    {
        if (!target.isPresent() || !this.moveTo(world, target.get(), scheduler) || !this.transform(world, scheduler, imageStore, target))
        {
            super.executeActivity(world, imageStore, scheduler);
        }
    }

    public boolean canPassThrough(WorldModel world, Point p) {
        return world.withinBounds(p) && (!world.isOccupied(p) || (world.isOccupied(p) && world.getOccupancyCell(p) instanceof Stump));
    }

    // this is a tiny bit sus because only 1 of the transforms needs target
    abstract boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore, Optional<Entity> target);

    boolean transformHelper(WorldModel world, EventScheduler scheduler, ImageStore imageStore, Dude dude) {
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);

        return true;
    }

}
