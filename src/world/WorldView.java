/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import java.util.HashMap;
import game.ent.buildings.EntBuilding;
import player.Player;
import game.ent.monsters.EntMonster;
import org.lwjgl.input.Mouse;
import render.Render;
import ne.effects.EffectsSystem;
import org.lwjgl.util.vector.Vector3f;
import world.WorldTile.TerrainType;
import render.EntityRenderer;
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
import render.Tileset;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Administrator
 */
public class WorldView implements IEventListener {

    public static Tileset bg_tileset = new Tileset();

    public static Point highlited_tile = null;

    public static void highlight_tile(Point tile_coord) {
        highlited_tile = tile_coord;
    }

    public WorldView(){
        EventManager.subscribe(this);
    }
    
    //returns z-index of current terrain layer
    public int get_z_index(){
        return Player.get_zindex();
    }


    public void synchronize(WorldModel model){
        
    }

    public static int TILEMAP_W = 100;
    public static int TILEMAP_H = 100;

    public static boolean DRAW_GRID = false;

    public class TextureTransition {
        public boolean[] nb = new boolean[8];  //n, w, e, s, nw, ns, ew, es
    }

    //public static HashMap<Point,TextureTransition>  bg_transition_map = new HashMap<Point,TextureTransition>(1024);

    public void render_background(){

        //bg_transition_map.clear();

        //System.out.println(WorldCluster.origin);

        int x = WorldCluster.origin.getX()*WorldChunk.CHUNK_SIZE;
        int y = WorldCluster.origin.getY()*WorldChunk.CHUNK_SIZE;
        int size = WorldCluster.CLUSTER_SIZE*WorldChunk.CHUNK_SIZE;

        for (int i = x; i<x+size; i++)
        for (int j = y; j<y+size; j++)
            {
                //NOTE: get_cached_chink is now deprecated function, as it can load random chunk data without checking, if it's inside
                //of world cluster
                //world cluster should cache it instead

                //serious debug problems othervise


                int chunk_x = (int)Math.floor((float)i / WorldChunk.CHUNK_SIZE);
                    //if (chunk_x<0){ chunk_x = chunk_x-1; }
                int chunk_y = (int)Math.floor((float)j / WorldChunk.CHUNK_SIZE);
                    //if (chunk_y<0){ chunk_y = chunk_y-1; }



                if (WorldModel.get_cached_chunk(
                        chunk_x,
                        chunk_y) != null){
                    WorldTile tile = WorldModel.get_tile(i,j);

                    if (tile != null){

                        //lil hack for terrain rendering visualization
                        if (tile.terrain_type != TerrainType.TERRAIN_WATER){
                            

                            //get terrain color according to the time
                            //Vector3f g_color_time = WorldTimer.get_sun_color();
                            Vector3f tile_color = get_tile_color(tile);
                            GL11.glColor3f(tile_color.x,tile_color.y,tile_color.z);
                        }else{
                            GL11.glColor3f(1.0f,1.0f,1.0f);
                        }
                        
                        //bg_tileset.render_bg_tile(i, j, tile.get_tile_id());
                        render_bg_tile(i,j,tile);
                    }
                }
                
            }

        /*for (int i = 0; i<TILEMAP_W; i++){
            for (int j = 0; j<TILEMAP_H; j++){
               bg_tileset.render_tile(i,j, 0);
            }
        }*/
    }
    static Vector3f utl_tile_color = new Vector3f();
    public Vector3f get_tile_color(WorldTile tile){
        //float g_color = ((float)tile.get_height() / 255);
        float g_color = 0.5f;
        utl_tile_color.set(
                0.5f + tile.light_level     + WorldTimer.get_light_amt(),
                g_color+ tile.light_level   + WorldTimer.get_light_amt(),
                0.5f+ tile.light_level      + WorldTimer.get_light_amt()
        );

        return utl_tile_color;
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

        WorldTile tile = WorldModel.get_tile(
            entity.origin.getX(),
            entity.origin.getY()
        );


        float r, g, b;
        r = g = b =  0.5f + tile.light_level + WorldTimer.get_light_amt();

        if (entity instanceof EntBuilding && entity.get_combat() != null){

            float hp_rate = 1.0f - (float)entity.get_combat().get_hp() / (float)entity.get_combat().get_max_hp();
            r = r - hp_rate * 0.1f;
            g = g - hp_rate * 0.3f;
            b = b - hp_rate * 0.3f;
        }

        GL11.glColor3f(
            r,
            g,
            b
        );


        EntityRenderer renderer = entity.get_render();
        renderer.render();  //render, lol
       
        

        //bg_tileset.render_sprite( entity.origin.getX(), entity.origin.getY(), 4);
    }

    //--------------------------------------------------------------------------

    public void render(){

        WorldViewCamera.update();

        glLoadIdentity();
    
        WorldViewCamera.setMatrix();
  
        render_background();
        render_entities();
        
        glLoadIdentity();

        update_cursor();
    }

    public void update_cursor(){

        if (Player.is_combat_mode()){
            Render.set_cursor("/render/ico_sword.png");
            return;
        }

        //Render.set_cursor("/render/ico_default.png");
        int x = Mouse.getX();
        int y = Mouse.getY();

        Point tile_coord = WorldView.getTileCoord(x,y);
        WorldTile tile = WorldModel.get_tile(tile_coord.getX(), tile_coord.getY());
        if(tile==null){
            return;
        }
        Entity ent = tile.get_actor();
        

        if (ent != null && ent instanceof EntMonster ){
            Render.set_cursor("/render/ico_sword.png");
        }else{
            Render.set_cursor("/render/ico_default.png");
        }

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
    public static float ISOMETRY_TILE_SCALE = 1.0f;
    /*
     * Sprites are rendering in 1:1 proportion, but
     * tiles are rendering in 1:1.2 proportion, so
     * we use this hack to avoid insane recalculations.
     * See Tileset.java
     */
    public static float TILE_UPSCALE = 1.2f;

    //perform reverse isometric transformation
    //transform screen point into the world representation in isometric space

    public static int local2world_x(float x, float y){

        x = (x / ISOMETRY_TILE_SCALE);
        y = (y / ISOMETRY_Y_SCALE / ISOMETRY_TILE_SCALE);

        float world_x = x*(float)Math.sin(ISOMETRY_ANGLE * Noise.DEG_TO_RAD)
                    +y*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD);

        return (int)world_x;
    }
    public static int local2world_y(float x, float y){
        
        x = (x / ISOMETRY_TILE_SCALE);
        y = (y / ISOMETRY_Y_SCALE / ISOMETRY_TILE_SCALE);

        float world_y = -x*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD)
                    +y*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD);

        return (int)world_y;
    }

    public static Point local2world(Point point){
        float x = point.getX();
        float y = point.getY();
        
        int world_x = local2world_x(x,y);
        int world_y = local2world_y(x,y);
        
        return new Point(world_x, world_y);
    }

    //--------------------------------------------------------------------------
    //                           World 2 Local
    //--------------------------------------------------------------------------
    public static int world2local_x(float x, float y){
        float local_x = x*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD)
                    -y*(float)Math.sin(ISOMETRY_ANGLE * Noise.DEG_TO_RAD);
        
         local_x = local_x * ISOMETRY_TILE_SCALE;

         return (int)local_x;
    }
    public static int world2local_y(float x, float y){
        float local_y = x*(float)Math.sin(ISOMETRY_ANGLE * Noise.DEG_TO_RAD)
                    +y*(float)Math.cos(ISOMETRY_ANGLE * Noise.DEG_TO_RAD);
        
         local_y = local_y * ISOMETRY_Y_SCALE * ISOMETRY_TILE_SCALE;

         return (int)local_y;
    }


    //transform world x,y point into the screen isometric representation (rotate to the 45 angle and scale)
    public static Point world2local(Point point){
        float x = point.getX();
        float y = point.getY();

        int local_x = world2local_x(x,y);
        int local_y = world2local_y(x,y);



        return new Point( local_x, local_y);
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

            
            int local_x = local2world_x(x,y);
            int local_y = local2world_y(x,y);
            //point = local2world(point);

            //--------------------------------------------
            //there is actually a hack there, but it works
            //--------------------------------------------
            int tile_x = local_x/ bg_tileset.TILE_SIZE;
            if (local_x<0){ tile_x = tile_x-1; }
            int tile_y = local_y/ bg_tileset.TILE_SIZE;
            if (local_y<0){ tile_y = tile_y-1; }
            //-----------------end of hack----------------

            Point point = new Point(tile_x,tile_y);


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


    /*
     *  public class TextureTransition {
            boolean[] nb = new boolean[8];  //n, w, e, s, nw, ns, ew, es
        }

        public static HashMap<Point,TextureTransition>  bg_transition_map = new HashMap<Point,TextureTransition>(1024);
     *
     */
    private void render_bg_tile(int i, int j, WorldTile tile) {
        //throw new UnsupportedOperationException("Not yet implemented");
        bg_tileset.render_bg_tile(i, j, tile.get_tile_id());

        /*
         * So far, tileset id acts like texture z-index.
         * Higher texture is allowed to wrap over lower texture, using alpha blending mask
         */
        /*Point _point = new Point(0,0);

        for (int _i = i-1; _i< i+1; _i++){
            for(int _j = j-1; _j < j+1; j++){
                if (WorldCluster.tile_in_cluster(_i,_j)){

                    _point.setLocation(_i, _j);
                    TextureTransition trans = bg_transition_map.get(_point);
                    if (trans == null){
                        trans = new TextureTransition();
                        bg_transition_map.put(_point, trans);
                    }
                    

                }
            }
        }*/
    }

}
