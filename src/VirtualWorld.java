import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Optional;

import processing.core.*;

public final class VirtualWorld extends PApplet {

    public static final int TIMER_ACTION_PERIOD = 100;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int WORLD_WIDTH_SCALE = 2;
    public static final int WORLD_HEIGHT_SCALE = 2;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static String LOAD_FILE_NAME = "world.sav";

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public static double timeScale = 1.0;
    public long nextTime;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    // Just for debugging and for P5
    // This should be refactored as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            if (entity instanceof Plant) {
                System.out.println(entity.getID() + ": " + entity.getClass() + " : " + ((Plant) entity).getHealth());
            } else {
                System.out.println(entity.getID() + ": " + entity.getClass());
            }
        } else {
            swampEntities(pressed);

            List<Point> affectedArea = pressed.expandArea();
            List<Point> exempt = pressed.bridges();

            swampEvent(affectedArea, exempt);
        }
    }


    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof AnimatedEntity) {
                ((AnimatedEntity)entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    private void swampEntities(Point pressed) {
        swampWitches(pressed);
        swampVillagers(pressed);
    }

    private void swampWitches(Point pressed) {
        Witch witch1 = new Witch(
                createID(Witch.WITCH_KEY, new Point(pressed.x, pressed.y)),// id
                pressed,                                                // position
                50,                                                     // animation period
                this.imageStore.getImageList(Witch.WITCH_KEY),             // images
                2000);                                                  // action period
        this.world.addEntity(witch1);
        witch1.scheduleActions(scheduler, world, imageStore);

        int rand1;
        int rand2;
        do {
            Random randGen = new Random();
            rand1 = randGen.nextInt(4);
            rand2 = randGen.nextInt(4);
        } while (world.isOccupied(new Point(pressed.x - rand1, pressed.y - rand2)));
        Witch witch2 = new Witch(
                createID(Witch.WITCH_KEY, new Point(pressed.x - rand1, pressed.y - rand2)),  // id
                new Point(pressed.x - rand1, pressed.y - rand2),                                                // position
                50,                                                     // animation period
                this.imageStore.getImageList(Witch.WITCH_KEY),           // images
                2000);                                                  // action period
        this.world.addEntity(witch2);
        witch2.scheduleActions(scheduler, world, imageStore);
    }

    private void swampVillagers(Point pressed) {
        int rand1;
        int rand2;
        do {
            Random randGen = new Random();
            rand1 = randGen.nextInt(4);
            rand2 = randGen.nextInt(4);
        } while (world.isOccupied(new Point(pressed.x + rand1, pressed.y + rand2)));
        ActiveEntity villager1 = new Villager(
                createID(Villager.VILLAGER_KEY, new Point(pressed.x + rand1, pressed.y + rand2)),
                new Point(pressed.x + rand1, pressed.y + rand2),
                50,
                this.imageStore.getImageList(Villager.VILLAGER_KEY),
                2000);
            this.world.addEntity(villager1);
            villager1.scheduleActions(scheduler, world, imageStore);

        do {
            Random randGen = new Random();
            rand1 = randGen.nextInt(4);
            rand2 = randGen.nextInt(4);
        } while (world.isOccupied(new Point(pressed.x - rand1, pressed.y - rand2)));
        ActiveEntity villager2 = new Villager(
                createID(Villager.VILLAGER_KEY, new Point(pressed.x - rand1, pressed.y - rand2)),
                new Point(pressed.x - rand1, pressed.y - rand2),
                50,
                this.imageStore.getImageList(Villager.VILLAGER_KEY),
                2000);
            this.world.addEntity(villager2);
            villager2.scheduleActions(scheduler, world, imageStore);
    }

    private void swampEvent(List<Point> affectedArea, List<Point> exempt) {
        for (Point cell : affectedArea) {
            if (!(exempt.contains(cell)) & world.withinBounds(cell)) {
                Background swamp = new Background(
                        createID(Background.SWAMP_KEY, new Point(cell.x, cell.y)),
                        this.imageStore.getImageList(Background.SWAMP_KEY));
                world.setBackgroundCell(cell, swamp);
                if (world.getOccupancyCell(cell) instanceof Plant) {
                    Entity deadTree = new DeadTree(
                            createID(DeadTree.DEAD_TREE_KEY, new Point(cell.x, cell.y)),
                            cell,
                            this.imageStore.getImageList(DeadTree.DEAD_TREE_KEY)
                    );
                    addRemove(cell, deadTree);
                } else if (world.getOccupancyCell(cell) instanceof Stump) {
                    Entity log = new Log(
                            createID(Log.LOG_KEY, new Point(cell.x, cell.y)),
                            cell,
                            this.imageStore.getImageList(Log.LOG_KEY)
                    );
                    addRemove(cell, log);
                } else if (world.getOccupancyCell(cell) instanceof House) {
                    Entity hut = new Hut(
                            createID(Hut.HUT_KEY, new Point(cell.x, cell.y)),
                            cell,
                            this.imageStore.getImageList(Hut.HUT_KEY)
                    );
                    addRemove(cell, hut);
                } else if (world.getOccupancyCell(cell) instanceof Dude) {
                    ActiveEntity villager = new Villager(
                            createID(Villager.VILLAGER_KEY, new Point(cell.x, cell.y)),
                            cell,
                            50,
                            this.imageStore.getImageList(Villager.VILLAGER_KEY),
                            2000
                    );
                    addRemove(cell, villager);
                    villager.scheduleActions(scheduler, world, imageStore);
                }
            }
        }
    }

    private void addRemove(Point cell, Entity entity) {
        world.removeEntityAt(cell);
        this.world.addEntity(entity);
    }

    private String createID(String key, Point point) {
        return key + "_" + point.x + "_" + point.y;
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }

}
