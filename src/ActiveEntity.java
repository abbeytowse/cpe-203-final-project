import processing.core.PImage;
import java.util.List;

public abstract class ActiveEntity extends AnimatedEntity {

    private int actionPeriod;

    public ActiveEntity(String id, Point position, int animationPeriod, List<PImage> images, int actionPeriod) {
        super(id, position, animationPeriod, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() { return this.actionPeriod; }
    public void setActionPeriod(int newPeriod) { this.actionPeriod = newPeriod; }

    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    @Override
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.actionPeriod);
        super.scheduleActions(scheduler, world, imageStore);
    }

}
