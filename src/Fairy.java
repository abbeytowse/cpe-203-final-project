import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Fairy extends MoveableEntity{

    public Fairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        super(id, position, animationPeriod, images, actionPeriod);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Stump.class, Statue.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                if (fairyTarget.get() instanceof Stump) {
                    Sapling sapling = new Sapling("sapling_" + this.getID(), tgtPos,
                            imageStore.getImageList(Sapling.SAPLING_KEY));

                    world.addEntity(sapling);
                    sapling.scheduleActions(scheduler, world, imageStore);
                }
                else {
                    ActiveEntity villager = new Villager(
                            "villager-" + tgtPos.x+ "-" + tgtPos.y,
                            tgtPos,
                            50,
                            imageStore.getImageList("villager"),
                            2000
                    );
                    world.addEntity(villager);
                    villager.scheduleActions(scheduler, world, imageStore);
                }
            }
        }

        super.executeActivity(world, imageStore, scheduler);
    }

    @Override
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        boolean bool = super.moveTo(world, target, scheduler);
        if (bool) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
        }
        return bool;
    }

    @Override
    public boolean canPassThrough(WorldModel world, Point p) {
        return world.withinBounds(p) && !world.isOccupied(p);
    }

}
