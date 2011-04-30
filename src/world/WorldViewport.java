/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import org.lwjgl.util.Point;
import render.TilesetRender;
import render.WindowRender;

/**
 *
 * @author Administrator
 */
public class WorldViewport {

    public static Point origin = new Point(0,0);
    public static int w = WindowRender.get_window_w() / TilesetRender.TILE_SIZE;
    public static int h = WindowRender.get_window_h() / TilesetRender.TILE_SIZE;


    public static void locate(Point target){
        origin.setLocation(target.getX()-w/2, target.getY()-h/2);
    }

    public static void moveto(Point origin){
        origin.setLocation(origin);
    }

    public Point world2local(Point world){
        world.setLocation(
                world.getX()-origin.getX(),
                world.getY()-origin.getY()
        );
        return world;
    }
    public Point local2world(Point world){
        world.setLocation(
                world.getX()+origin.getX(),
                world.getY()+origin.getY()
        );
        return world;
    }
}
