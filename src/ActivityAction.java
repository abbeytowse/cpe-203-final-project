public class ActivityAction implements Action {

    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public ActivityAction(
            Entity entity,
            WorldModel world,
            ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
        if (this.entity instanceof ActiveEntity) {
            ((ActiveEntity) this.entity).executeActivity(this.world, this.imageStore, scheduler);
        } else {
            throw new UnsupportedOperationException(String.format(
                    "executeAction not supported for %s",
                    this.entity.getClass()));
        }
    }

}
