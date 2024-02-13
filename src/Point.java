import java.util.ArrayList;
import java.util.List;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point
{
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point)other).x == this.x
                && ((Point)other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public boolean adjacent(Point other) {
        return (this.x == other.x && Math.abs(this.y - other.y) == 1) || (this.y == other.y
                && Math.abs(this.x - other.x) == 1);
    }

    public int distanceSquared(Point other) {
        int deltaX = this.x - other.x;
        int deltaY = this.y - other.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public List<Point> bridges() {
        List<Point> exempt = new ArrayList<>();
        exempt.add(new Point(4,8));
        exempt.add(new Point(4, 9));
        exempt.add(new Point(37, 19));
        return exempt;
    }

    public List<Point> expandArea() {
        List<Point> points = new ArrayList<Point>();
        points.add(this);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                points.add(new Point(this.x - i, this.y - j));
                points.add(new Point(this.x + i, this.y + j));
                points.add(new Point(this.x + i, this.y - j));
                points.add(new Point(this.x - i, this.y + j));
            }
        }
        for (int i = 5; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                points.add(new Point(this.x - i, this.y - j));
                points.add(new Point(this.x + i, this.y + j));
                points.add(new Point(this.x + i, this.y - j));
                points.add(new Point(this.x - i, this.y + j));
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 4; j < 6; j++) {
                points.add(new Point(this.x - i, this.y - j));
                points.add(new Point(this.x + i, this.y + j));
                points.add(new Point(this.x + i, this.y - j));
                points.add(new Point(this.x - i, this.y + j));
            }
        }
        return points;
    }

}
