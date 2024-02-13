import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Witch extends MoveableEntity {

    public static final String WITCH_KEY = "witch";

    public Witch(
            String id,
            Point position,
            int animationPeriod,
            List<PImage> images,
            int actionPeriod)
    {
        super(id, position, animationPeriod, images, actionPeriod);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> witchTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Villager.class)));

        if (witchTarget.isPresent()) {
            Point tgtPos = witchTarget.get().getPosition();

            if (this.moveTo(world, witchTarget.get(), scheduler)) {
                Statue statue = new Statue(
                        "statue_" + tgtPos.x + "_" + tgtPos.y,
                        tgtPos,
                        imageStore.getImageList(Statue.STATUE_KEY));

                world.addEntity(statue);
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
    public Point nextPosition(WorldModel world, Point destPos)
    {
//      PathingStrategy ps = new SingleStepPathingStrategy();
        PathingStrategy ps = new AStarPathingStrategy();
        List<Point> path = ps.computePath(
                this.getPosition(),                                                 // start point
                destPos,                                                            // end point
                p -> this.canPassThrough(world, p),                                 // canPassThrough
                Point::adjacent,                                                    // withinReach
                PathingStrategy.CARDINAL_NEIGHBORS                                  // potentialNeighbors
        );

        if (path.size() > 5) {
            return path.get(4);
        } else if (path.size() > 0) {
            return path.get(0);
        }
        return this.getPosition();
    }

    @Override
    public boolean canPassThrough(WorldModel world, Point p) {
        return world.withinBounds(p) && !world.isOccupied(p);
    }

}
