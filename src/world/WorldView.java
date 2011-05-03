/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import world.util.Noise;
import org.lwjgl.opengl.GL11;
import events.EMouseRelease;
import ne.Input.MouseInputType;
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

    public static TilesetRender bg_tileset = new TilesetRender();

    public WorldView(){
        EventManager.subscribe(this);
    }


    public void synchronize(WorldModel model){
        
    }

    public static int TILEMAP_W = 100;
    public static int TILEMAP_H = 100;





    public void render_background(){

        //System.out.println(WorldCluster.origin);

        int x = WorldCluster.origin.getX()*WorldChunk.CHUNK_SIZE;
        int y = WorldCluster.origin.getY()*WorldChunk.CHUNK_SIZE;
        int size = WorldCluster.CLUSTER_SIZE*WorldChunk.CHUNK_SIZE;

        for (int i = x; i<x+size; i++)
        for (int j = y; j<y+size; j++)
            {
                if (WorldModel.get_cached_chunk(
                        i/WorldChunk.CHUNK_SIZE,
                        j/WorldChunk.CHUNK_SIZE) != null){
                    WorldTile tile = WorldModel.get_tile(i,j);

                    if (tile != null){

                        //lil hack for terrain rendering visualization

                        float g_color = ((float)tile.get_height() / 255)*2;        
                        GL11.glColor3f(1.0f,g_color,1.0f);
                        
                        bg_tileset.render_bg_tile(i, j, tile.get_tile_id());
                    }
                }
                
            }

        /*for (int i = 0; i<TILEMAP_W; i++){
            for (int j = 0; j<TILEMAP_H; j++){
               bg_tileset.render_tile(i,j, 0);
            }
        }*/
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
        GL11.glColor3f(1.0f,1.0f,1.0f);

        bg_tileset.render_sprite( entity.origin.getX(), entity.origin.getY(), 4);
    }

    //--------------------------------------------------------------------------

    public void render(){

        WorldViewCamera.update();

        glLoadIdentity();
    
        WorldViewCamera.setMatrix();
  
        render_background();
        render_entities();
        
        glLoadIdentity();
        
    }



    public static Point getTileCoord(Point window_coord){


        int x = window_coord.getX();
        int y = window_coord.getY();
        return getTileCoord(x,y);


    }
    
    public static boolean ISOMETRY_MODE = true;
    public static float ISOMETRY_ANGLE = 45.0f;

    //public static float ISOMETRY_Y_SCALE = 0.6f;
    //public static float ISOMETRY_TILE_SCALE = 1.2f;
    public static float ISOMETRY_Y_SCALE = 0.6f;
    public static float ISOMETRY_TILE_SCALE = 1.2f;

    //perform reverse isometric transformation
    //transform screen point into the world representation in isometric space

    public static Point local2world(Point point){
        float x = point.getX();
        float y = point.getY();

        x = (x / ISOMETRY_TILE_SCALE);
        y = (y / ISOMETRY_Y_SCALE / ISOMETRY_TILE_SCALE);

        float world_x = x*(float)Math.sin(ISOMETRY_ANGLE * Noise.DEG_TO_RAD)
                    +y*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD);
        float world_y = -x*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD)
                    +y*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD);

        return new Point((int)world_x, (int)world_y);
    }

    //transform world x,y point into the screen isometric representation (rotate to the 45 angle and scale)
    public static Point world2local(Point point){
        float x = point.getX();
        float y = point.getY();

        float local_x = x*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD)
                    -y*(float)Math.sin(ISOMETRY_ANGLE * Noise.DEG_TO_RAD);
        float local_y = x*(float)Math.sin(ISOMETRY_ANGLE * Noise.DEG_TO_RAD)
                    +y*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD);

        local_y = local_y * ISOMETRY_Y_SCALE * ISOMETRY_TILE_SCALE;
        local_x = local_x * ISOMETRY_TILE_SCALE;


        return new Point((int)local_x, (int)local_y);
    }
    

    public static Point getTileCoord(int x, int y) {

        y = WindowRender.get_window_h()-y;  //invert it wtf

        if (!ISOMETRY_MODE){

            float world_x = x + WorldViewCamera.camera_x;
            float world_y = y + WorldViewCamera.camera_y;



            x = (int) world_x / bg_tileset.TILE_SIZE;
            y = (int) world_y / bg_tileset.TILE_SIZE;

            return new Point(x-1,y-1);

        }else{

            //System.out.println(new Point(x,y));

            x = x + (int)WorldViewCamera.camera_x;
            y = y + (int)WorldViewCamera.camera_y;

            Point point = new Point(x,y);

            point = local2world(point);


            point.setLocation(
                    point.getX()/ bg_tileset.TILE_SIZE,
                    point.getY()/ bg_tileset.TILE_SIZE
            );


            return point;
            
        }
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

               WorldViewCamera.follow_target = false;
            //camera_x += drag_event.dx*1.5;
            //camera_y += drag_event.dy*1.5;
               WorldViewCamera.move(drag_event.dx*1.5f,-drag_event.dy*1.5f);
           }


           

       }else if(event instanceof  EMouseRelease){
           EMouseRelease drag_event = (EMouseRelease)event;
           if (drag_event.type == MouseInputType.RCLICK){
                //camera_x = 0.0f;
                //camera_y = 0.0f;
               //WorldViewCamera.set(0.0f,0.0f);
               WorldViewCamera.follow_target = true;
           }
       }
    }
    //--------------------------------------------------------------------------
    public void e_on_event_rollback(Event event){
      
    }

}
