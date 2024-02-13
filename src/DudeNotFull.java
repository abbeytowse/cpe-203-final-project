import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude{

    public DudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images,
            int resourceCount)
    {
        super(id, position, animationPeriod, images, actionPeriod, resourceLimit, resourceCount);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));
        this.executeActivityHelper(world, imageStore, scheduler, target);
    }

    @Override
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore,
            Optional<Entity> target)
    {
        if (this.resourceCount >= this.resourceLimit) {
            DudeFull dude = new DudeFull(this.getID(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.resourceLimit,
                    this.getImages());

            return this.transformHelper(world, scheduler, imageStore, dude);
        }

        return false;
    }

    @Override
    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        boolean bool = super.moveTo(world, target, scheduler);
        if (bool) {
            this.resourceCount += 1;
            ((Plant)target).decrementHealth();
        }
        return bool;
    }

}
