/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import events.EMouseRelease;
import namelessengine.Input.MouseInputType;
import events.IEventListener;
import events.EventManager;
import events.EMouseDrag;
import events.Event;
import org.lwjgl.util.Point;
import render.WindowRender;
import game.ent.Entity;
import java.util.Iterator;
import game.ent.EntityManager;
import render.TilesetRender;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Administrator
 */
public class WorldView implements IEventListener {

    public TilesetRender bg_tileset = new TilesetRender();

    public WorldView(){
        EventManager.subscribe(this);
    }


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

        glLoadIdentity();
        glTranslatef(camera_x, -camera_y, 0);
        //camera.setMatrix();
  
        render_background();
        render_entities();
        

        
    }

    public Point getTileCoord(Point window_coord){
        int x = window_coord.getX();
        int y = window_coord.getY();

        y = WindowRender.get_window_h()-y;  //invert it wtf

        x = x / bg_tileset.TILE_SIZE;
        y = y / bg_tileset.TILE_SIZE;

        return new Point(x,y);
    }

    //todo: refact me
    float camera_x = 0;
    float camera_y = 0;

    //----------------------------EVENTS SHIT-----------------------------------
    public void e_on_event(Event event){
       
       /*System.out.println("WorldView - camera @ "+Float.toString(camera_x)+
                   ","+Float.toString(camera_y));*/

       if (event instanceof EMouseDrag){

           EMouseDrag drag_event = (EMouseDrag)event;
           if (drag_event.type == MouseInputType.RCLICK){
            camera_x += drag_event.dx;
            camera_y += drag_event.dy;
           }


           

       }else if(event instanceof  EMouseRelease){
           EMouseRelease drag_event = (EMouseRelease)event;
           if (drag_event.type == MouseInputType.RCLICK){
            camera_x = 0.0f;
            camera_y = 0.0f;
           }
       }
    }
    //--------------------------------------------------------------------------
    public void e_on_event_rollback(Event event){
      
    }

}
