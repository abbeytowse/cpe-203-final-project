import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class MoveableEntity extends ActiveEntity {

    public MoveableEntity(String id, Point position, int animationPeriod, List<PImage> images, int actionPeriod) {
        super(id, position, animationPeriod, images, actionPeriod);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

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

        if (path.size() > 0) {
            return path.get(0);
        }
        return this.getPosition();
    }

    abstract boolean canPassThrough(WorldModel world, Point p);

}
