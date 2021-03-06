/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world;

import ne.Input;
import game.ent.enviroment.EntityTree;
import game.ent.EntityDecal;
import render.DebugRenderer;
import ne.Main;
import org.lwjgl.util.vector.Vector3f;
import render.SpriteRenderer;
import client.ClientGameEnvironment;
import client.ClientEventManager;
import world.layers.WorldLayer;
import render.layers.GroundLayerRenderer;
import render.layers.LayerRenderer;
import game.ent.buildings.EntBuilding;
import player.Player;
import game.ent.monsters.EntMonster;
import org.lwjgl.input.Mouse;
import render.Render;
import render.EntityRenderer;
import world.util.Noise;
import org.lwjgl.opengl.GL11;
import events.EMouseRelease;
import ne.Input.MouseInputType;
import events.IEventListener;
import events.EMouseDrag;
import events.Event;
import org.lwjgl.util.Point;
import render.WindowRender;
import game.ent.Entity;
import java.util.Iterator;
import render.TilesetRenderer;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author bloodrizer
 */
public class WorldView implements IEventListener {

    public static Point highlited_tile = null;

    public static void highlight_tile(Point tile_coord) {
        highlited_tile = tile_coord;
    }

    public WorldView(){
        ClientEventManager.eventManager.subscribe(this);
    }
    
    //returns z-index of current terrain layer
    private static int view_z_index = WorldLayer.GROUND_LAYER;
    public static void set_zindex(int z_index){
        if (z_index<0){ view_z_index = 0; }
        else if(z_index > WorldModel.LAYER_COUNT) {
            view_z_index = WorldModel.LAYER_COUNT;
        } else {
        
            view_z_index = z_index;
        }
    }
    
    public static int get_zindex(){
        //return Player.get_zindex();
        return view_z_index;
    }


    public void synchronize(WorldLayer model){
        
    }

    public static int TILEMAP_W = 100;
    public static int TILEMAP_H = 100;

    public static boolean DRAW_GRID = false;

    private WorldLayer getLayer() {
        return ClientGameEnvironment.getWorldLayer(view_z_index);
    }

    private void render_server_entity(Entity entity) {

        if (entity instanceof EntityDecal || entity instanceof EntityTree){
            return;
        }

        if (!WorldCluster.tile_in_cluster(entity.origin.getX(), entity.origin.getY())){
            return;
        }

        DebugRenderer renderer = new DebugRenderer();
        renderer.set_entity(entity);
        renderer.render();
    }

    public class TextureTransition {
        public boolean[] nb = new boolean[8];  //n, w, e, s, nw, ns, ew, es
    }
    
    //public static HashMap<Point,TextureTransition>  bg_transition_map = new HashMap<Point,TextureTransition>(1024);

    public void render_layer(){
        
        LayerRenderer layer_renderer = null;
        
        //get layer renderer based on layer_id
        //TODO: cache it, so we would not create new object every frame

        


        if (get_zindex() == WorldLayer.GROUND_LAYER){
            layer_renderer = new GroundLayerRenderer();
        }
        
        
        //bg_transition_map.clear();

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

                if (getLayer().get_cached_chunk(
                        chunk_x,
                        chunk_y) != null){
                    WorldTile tile = getLayer().get_tile(i,j);

                    
                    //render tile
                    if (layer_renderer != null){
                        layer_renderer.render_tile(tile, i, j);
                    }

                    //tileSprite.render_sprite(i, j, 1);

                }
                
            }
    }

    public void render_entities(){
        
        for (Iterator iter = ClientGameEnvironment.getEntityManager().getList(view_z_index).iterator(); iter.hasNext();) {
           Entity entity = (Entity) iter.next();
           render_entity(entity);
        }

        //DEBUG USE ONLY: SLOW AND UNSTABLE
        if( Input.key_state_alt ){
            for (Entity ent: Main.neServer.getEnv().getEntityManager().getList(view_z_index).toArray(new Entity[0])) {
                render_server_entity(ent);
            }
        }
    }

    public static int getYOffset(WorldTile tile){
        if (tile != null){
            float y_offset = (int)((float)tile.get_height()/32.0f);
            return (int)(y_offset*20);
        }else{
            return 0;
        }
    }

    public void render_entity(Entity entity){
        //todo: use factory render

        //IGenericRender render = Render.get_render(entity);
        //render.render(entity);
        GL11.glColor3f(1.0f,1.0f,1.0f);

        WorldTile tile = getLayer().get_tile(
            entity.origin.getX(),
            entity.origin.getY()
        );


        float r, g, b;
        //r = g = b =  0.5f + tile.light_level + WorldTimer.get_light_amt();
        Vector3f tile_color = GroundLayerRenderer.get_tile_color(tile);
        r = tile_color.getX();
        g = tile_color.getY();
        b = tile_color.getZ();

        if (entity instanceof EntBuilding && entity.get_combat() != null){

            float hp_rate = 1.0f - (float)entity.get_combat().get_hp() / (float)entity.get_combat().get_max_hp();
            r = r - hp_rate * 0.1f;
            g = g - hp_rate * 0.3f;
            b = b - hp_rate * 0.3f;
        }else{
        }

        GL11.glColor3f(
            r,
            g,
            b
        );

        
        final int y_offset = WorldView.getYOffset(tile);


        //draw entity casting shadow
        if (entity.is_blocking()){
            SpriteRenderer shadow = new SpriteRenderer(){
                @Override
                public void render(){
                    GL11.glTexParameteri(
                            GL11.GL_TEXTURE_2D,
                            GL11.GL_TEXTURE_MAG_FILTER,
                            GL11.GL_NEAREST);

                    GL11.glTexParameteri(
                            GL11.GL_TEXTURE_2D,
                            GL11.GL_TEXTURE_MIN_FILTER,
                            GL11.GL_NEAREST);

                    //float shadowSize = ((float)TilesetRenderer.TILE_SIZE - (float)tileset.sprite_w) / (float)TilesetRenderer.TILE_SIZE;

                    float shadowSize = 1.0f;

                    EntityRenderer entRenderer = ent.build_render();
                    if (entRenderer instanceof SpriteRenderer){
                        shadowSize = -(float)(64 - ((SpriteRenderer)entRenderer).tileset.sprite_w ) /
                                (float)TilesetRenderer.TILE_SIZE / 3.0f;
                    }

                    //shadowSize = 3.0f;

                    //float shadowSize = 1.0f;

                    tileset.set_offset(ent.dx, ent.dy);
                    tileset.set_scale(shadowSize);

                    tileset.render_sprite(
                        ent.origin.getX(),
                        ent.origin.getY(),
                        tile_id,
                        0,
                        //(int)(12*shadowSize)
                        //y_offset + 15
                        y_offset + (int)(16*shadowSize)
                    );

                    
                }
            };
            shadow.set_texture("shadow.png");
            shadow.get_tileset().sprite_w = 64;
            shadow.get_tileset().sprite_h = 64;
            
            shadow.get_tileset().TILESET_W = 1;
            shadow.get_tileset().TILESET_H = 1;

            shadow.set_entity(entity);
            shadow.render();
        }

        EntityRenderer renderer = entity.get_render();
        renderer.render();  //render, lol
    }

    //--------------------------------------------------------------------------

    public void render(){

        WorldViewCamera.update();

        glLoadIdentity();
    
        WorldViewCamera.setMatrix();
  
        render_layer();
        render_entities();
        
        glLoadIdentity();

        update_cursor();
    }

    public void update_cursor(){
        
        //Introduce cursor class
        //We may change cursor, if user action is now active

        if (Player.is_combat_mode()){
            Render.set_cursor("/render/ico_sword.png");
            return;
        }

        //Render.set_cursor("/render/ico_default.png");
        int x = Mouse.getX();
        int y = Mouse.getY();

        Point tile_coord = WorldView.getTileCoord(x,y);
        WorldTile tile = getLayer().get_tile(tile_coord.getX(), tile_coord.getY());
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
    
    /**
     *  Cast (mx,my) screen coords, given in the NE coord system
     *  without checking tile height modifiers
     */
    private static Point getTileCoordPlain(int mx, int my){
            int local_x = local2world_x(mx,my);
            int local_y = local2world_y(mx,my);
            //point = local2world(point);

            //--------------------------------------------
            //there is actually a hack there, but it works
            //--------------------------------------------
            int tile_x = local_x/ TilesetRenderer.TILE_SIZE;
            if (local_x<0){ tile_x = tile_x-1; }
            int tile_y = local_y/ TilesetRenderer.TILE_SIZE;
            if (local_y<0){ tile_y = tile_y-1; }
            //-----------------end of hack----------------

            Point point = new Point(tile_x,tile_y);

            return point;
    }

    /**
     * Cast (mx,my) scren coords given in the GL coord system
     * to the world tile coord, assuming every tile has different height
     */
    public static Point getTileCoord(int x, int y) {

        /**Step 1. First aproximation.
         *
         * Get tile as if height was not set at all
         * That help us to get relative area of the tiles we clicked at
         */

        y = WindowRender.get_window_h()-y;  //invert it wtf

            /* NE use following coord system, while gl use reverse y axis
               (0,0) - - (x,0)
             * |
             * |
             * (0,y)
             */

            x = x + (int)WorldViewCamera.camera_x;
            y = y + (int)WorldViewCamera.camera_y;

            
            Point point = getTileCoordPlain(x,y);
            int tile_x = point.getX();
            int tile_y = point.getY();

            /**Step 2.
             * Iterate all tiles at that region
             * Check each tile if our point is in their top region
             */

            for (int i = tile_x-3; i<tile_x+3; i++){
                for(int j = tile_y-3; j<tile_y+3; j++){
                    if (!WorldCluster.tile_in_cluster(i, j)) { continue; }

                    WorldTile tile = ClientGameEnvironment.getWorldLayer(view_z_index).get_tile(i, j);
                    int y_offset = getYOffset(tile);

                    //adjust y value with height midifier
                    //replace 32 with actual magic constant based on tile sprite height
                    int y2 = y - y_offset;

                    Point point2 = getTileCoordPlain(x,y2);
                    if (point2.equals(tile.origin)){
                        return point2;
                    }
                }
            }
            return point;
    }

    /**
     * This method checks first, that point is in the tile sprite area
     * It checks then, if the point is in the upper part of the sprite - isometric tile square
     *
     */
    private static boolean pointInTile(WorldTile tile, int mx, int my){
        return false;
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


    /**
     * Recieves screen coord of the entity based on the tile coord and a screen tile size
     */

    public static int get_tile_x_screen(Point origin){
        return world2local_x(
                (origin.getX())*TilesetRenderer.TILE_SIZE,
                (origin.getY())*TilesetRenderer.TILE_SIZE
        ) - (int)WorldViewCamera.camera_x;
    }

    public static int get_tile_y_screen(Point origin){
        return world2local_y(
                (origin.getX())*TilesetRenderer.TILE_SIZE,
                (origin.getY())*TilesetRenderer.TILE_SIZE
        ) - (int)WorldViewCamera.camera_y;
    }

}
