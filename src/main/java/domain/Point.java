package domain;

/**
 * Point represents the (x, y) coordinates of a 2D point, or simply a pair of values.
 * Used for handling graph data.
 */
public class Point {

    /**
     * The first value of the value pair.
     */
    private final double x;
    /**
     * The second value of the value pair.
     */
    private final double y;

    /**
     * Create a new Point with finalised x and y values.
     *
     * @param x first value
     * @param y second value
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Get x or the first value of the value pair.
     *
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Get y or the second value of the value pair.
     *
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Calculates the squared distance to another Point.
     *
     * @param p other Point
     * @return squared distance
     */
    public double distanceSquaredTo(Point p){
        return Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2);
    }

    /**
     * Calculates the distance to another Point.
     *
     * @param p other Point
     * @return distance
     */
    public double distanceTo(Point p){
        return Math.sqrt(distanceSquaredTo(p));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
