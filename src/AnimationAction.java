public class AnimationAction implements Action {

    private final Entity entity;
    private final int repeatCount;

    public AnimationAction(
            Entity entity,
            int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        ((AnimatedEntity)this.entity).nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    new AnimationAction(this.entity, Math.max(this.repeatCount - 1,
                            0)),
                    ((AnimatedEntity)this.entity).getAnimationPeriod());
        }
    }

}
