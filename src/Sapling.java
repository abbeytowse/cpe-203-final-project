import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Sapling extends Plant {

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time

    public Sapling(
            String id,
            Point position,
            List<PImage> images) {
        super(id, position, SAPLING_ACTION_ANIMATION_PERIOD , images, SAPLING_ACTION_ANIMATION_PERIOD , 0);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        this.health++;
        super.executeActivity(world, imageStore, scheduler);
    }

    @Override
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore
            ) {
        if (super.transform(world, scheduler, imageStore)) {
            return true;

        } else if (this.health >= SAPLING_HEALTH_LIMIT) {
            Tree tree = new Tree("tree_" + this.getID(),
                    this.getPosition(),
                    getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN),
                    getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN),
                    getNumFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN),
                    imageStore.getImageList(Tree.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
    }

}
