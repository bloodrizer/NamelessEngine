/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import game.ent.Entity;
import game.ent.EntityManager;
import java.util.Collections;
import org.lwjgl.util.Point;
import player.Player;

/**
 *
 * @author Administrator
 */
public class WorldModel {
    //

    private static java.util.Map<Point,WorldTile> tile_data = Collections.synchronizedMap(new java.util.HashMap<Point,WorldTile>(1000));
    //--------------------------------------------------------------------------
    private static Point util_point     = new Point(0,0);
    private static Point __stack_point  = new Point(0,0);

    //todo: use actual stack there
    private static void push_point(Point point){
        __stack_point.setLocation(point);
    }
    private static void pop_point(Point point){
        point.setLocation(__stack_point);
    }

    private static synchronized WorldTile get_tile(int x, int y){
        push_point(util_point);
        util_point.setLocation(x, y);
        
        WorldTile tile = tile_data.get(util_point);
        pop_point(util_point);

        return tile;
    }
    //--------------------------------------------------------------------------
    private static java.util.Map<Point,WorldChunk> chunk_data = Collections.synchronizedMap(new java.util.HashMap<Point,WorldChunk>(100));
    //private static Point chunk_coord = new Point(0,0);

    private static synchronized WorldChunk get_chunk(int x, int y){
        push_point(util_point);
        util_point.setLocation(x, y);

        WorldChunk chunk = chunk_data.get(util_point);
        pop_point(util_point);

        return chunk;
    }


    private void precache_chunk(int i, int j){
        WorldChunk chunk = WorldModel.get_chunk(i,j);
        //todo: move to chunk?
    }

    //--------------------------------------------------------------------------
    public void init(){
      /*Entity player_ent = new Entity();
      EntityManager.add(player_ent);

      Player.set_ent(player_ent);*/
    }
}
