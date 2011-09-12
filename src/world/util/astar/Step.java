/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.util.astar;

import org.lwjgl.util.Point;

/** A single step within the path
 *
 * @author Kevin Glass
 */
public class Step {

    /** The x coordinate at the given step */
    int x;
    /** The y coordinate at the given step */
    int y;
    
    private Point pointAdapter = new Point(0,0);

    @Override
    public String toString() {
        return "@["
                + Integer.toString(x)
                + ","
                + Integer.toString(y)
                + "]";
    }

    /**
     * Create a new step
     *
     * @param x The x coordinate of the new step
     * @param y The y coordinate of the new step
     */
    public Step(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Point getPoint(){
        pointAdapter.setLocation(x, y);
        return pointAdapter;
    }

    /**
     * Get the x coordinate of the new step
     *
     * @return The x coodindate of the new step
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate of the new step
     *
     * @return The y coodindate of the new step
     */
    public int getY() {
        return y;
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        return x * y;
    }

    /**
     * @see Object#equals(Object)
     */
    public boolean equals(Object other) {
        if (other instanceof Step) {
            Step o = (Step) other;

            return (o.x == x) && (o.y == y);
        }

        return false;
    }
}