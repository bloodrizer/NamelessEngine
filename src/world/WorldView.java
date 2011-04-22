/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import org.lwjgl.util.Point;
import render.WindowRender;
import game.ent.Entity;
import java.util.Iterator;
import game.ent.EntityManager;
import render.TilesetRender;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 *
 * @author Administrator
 */
public class WorldView {

    public TilesetRender bg_tileset = new TilesetRender();

    public void synchronize(WorldModel model){
        
    }

    public static int TILEMAP_W = 100;
    public static int TILEMAP_H = 100;

    public void render_background(){

        for (int i = 0; i<TILEMAP_W; i++){
            for (int j = 0; j<TILEMAP_H; j++){
               bg_tileset.render_tile(i,j, 0);
            }
        }
    }

    public void render_entities(){
        
        for (Iterator iter = EntityManager.ent_list_sync.iterator(); iter.hasNext();) {
           Entity entity = (Entity) iter.next();
           render_entity(entity);
        }
    }

    public void render_entity(Entity entity){
        //todo: use factory render

        //IGenericRender render = Render.get_render(entity);
        //render.render(entity);


        bg_tileset.render_tile(entity.origin.getX(),entity.origin.getY(), 4);
    }

    //--------------------------------------------------------------------------

    public void render(){
        
        render_background();
        render_entities();

        //glTranslatef(-0.5f, -0.5f, 0);
    }

    public Point getTileCoord(Point window_coord){
        int x = window_coord.getX();
        int y = window_coord.getY();

        y = WindowRender.get_window_h()-y;  //invert it wtf

        x = x / bg_tileset.TILE_SIZE;
        y = y / bg_tileset.TILE_SIZE;

        return new Point(x,y);
    }
}
