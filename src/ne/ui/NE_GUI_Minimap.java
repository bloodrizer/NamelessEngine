/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import client.ClientGameEnvironment;
import org.newdawn.slick.Color;
import render.overlay.OverlaySystem;
import org.newdawn.slick.TrueTypeFont;
import render.Render;
import org.lwjgl.util.Point;
import world.WorldModel;
import render.FBO;
import player.Player;
import events.network.EEntityMove;
import events.Event;
import world.WorldTile;
import org.lwjgl.opengl.GL11;
import world.WorldChunk;
import world.WorldCluster;
import world.layers.WorldLayer;
import world.WorldRegion;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Minimap extends NE_GUI_FrameModern {

    //static final boolean fbo_enabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
    FBO fbo;
    TrueTypeFont village_labels = OverlaySystem.precache_font(12);
    
    public static boolean expired = true;
    
    public NE_GUI_Minimap(){
        super(true);

        set_tw(15);
        set_th(15);
        center();
    }

    public final static int MINIMAP_SIZE = 420;

    @Override
    public void render(){

        super.render();

        if(!visible){
            return;
        }

        if (!FBO.fbo_enabled || fbo == null){
            return;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.fbo_texture_id);

        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        
        GL11.glBegin(GL11.GL_QUADS);        //<-- white quad there
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(get_x()+30, get_y()+30, 0);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(get_x()+MINIMAP_SIZE+30, get_y()+30, 0);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(get_x()+MINIMAP_SIZE+30, get_y()+450, 0);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(get_x()+30, get_y()+450, 0);
        GL11.glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);

        Point regionOrigin = new Point(0,0);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                regionOrigin.setLocation(i, j);
                WorldRegion region = WorldModel.worldRegions.get_cached(regionOrigin);
                if (region.village != null) {

                    Render.bind_texture("/render/gfx/map/village.png");
                    GL11.glColor3f(1.0f, 1.0f, 1.0f);

                    int tile_size_scaled = (int)((float)MINIMAP_SIZE / 512.0f * 34);

                    int icon_x = get_x() + 30 + (int) (((float) i + 0.5f) * WorldRegion.REGION_SIZE * tile_size_scaled) - 8;
                    int icon_y = get_y() + 30 + (int) (((float) j + 0.5f) * WorldRegion.REGION_SIZE * tile_size_scaled) - 8;
                    int icon_h = 16;
                    int icon_w = 16;

                    GL11.glBegin(GL11.GL_QUADS);        //<-- white quad there


                    GL11.glTexCoord2f(0.0f, 0.0f);
                    GL11.glVertex3f(icon_x, icon_y, 0);
                    GL11.glTexCoord2f(1.0f, 0.0f);
                    GL11.glVertex3f(icon_x + icon_w, icon_y, 0);
                    GL11.glTexCoord2f(1.0f, 1.0f);
                    GL11.glVertex3f(icon_x + icon_w, icon_y + icon_h, 0);
                    GL11.glTexCoord2f(0.0f, 1.0f);
                    GL11.glVertex3f(icon_x, icon_y + icon_h, 0);
                    GL11.glEnd();


                    village_labels.drawString(icon_x + 18, icon_y, region.village.getName(), Color.black);
                }
            }
        }

    }

    //update vitual map texture

    public final void update_map() {
        //We don't update this shit if minimap is not opened
        if (!expired){
            return;
        }
        
        if (!FBO.fbo_enabled){
            return;
        }

        expired = false;
        
        fbo = new FBO();
        fbo.render_begin();

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        //------render start

        System.err.println("Redrawing minimap");
        generate_minimap();

        //------render end

        fbo.render_end();
    }

    private void generate_minimap() {

        /*
         * Map generation algorythm:
         * 
         * 0. To be discussed - check if player is in x:0,y:0 supercluster
         * 
         * 1. Render region supercluster (3x3 regions)
         * Draw 9 big squares of 5 tiles, representing 9 regions
         * 
         * If current region is player's region, mark it
         * Iterate throw the region chunks. If current chunk is in player's cluster,
         * render it on map -
         * 
         * 2. Render chunk:
         * Iterate through the chunk tiles. One chunk should be same size as map tile.
         * Set pixel colors according to the terrain data
         */
        
        //render supercluster
        
        
        for( int i=0; i<3; i++)
            for (int j=0; j<3; j++){
                
                int region_x = i * WorldRegion.REGION_SIZE * 34;
                int region_y = j * WorldRegion.REGION_SIZE * 34;
                int region_w = 34*WorldRegion.REGION_SIZE;
                int region_h = 34*WorldRegion.REGION_SIZE;

                glPolygonMode ( GL_FRONT_AND_BACK, GL_LINE ) ;
                glBegin(GL11.GL_QUADS);        //<-- draw region quad
                
                glColor4f(0.0f,0.0f,0.0f,1.0f);

                glVertex3f(region_x, region_y, 0);
                glVertex3f(region_x + region_w, region_y, 0);
                glVertex3f(region_x + region_w, region_y + region_h, 0);
                glVertex3f(region_x, region_y + region_h, 0);
                glEnd();
                glPolygonMode ( GL_FRONT_AND_BACK, GL_FILL ) ;
                

                //draw region
                for( int chunk_x = i*WorldRegion.REGION_SIZE; chunk_x< (i+1)*WorldRegion.REGION_SIZE; chunk_x++)
                    for( int chunk_y = j*WorldRegion.REGION_SIZE; chunk_y< (j+1)*WorldRegion.REGION_SIZE; chunk_y++){

                        glPointSize(4);
                        glBegin(GL_POINTS);
                        glColor3d(0.8f,0.8f,0.8f);
                        glVertex2d(34*chunk_x,34*chunk_y);
                        glEnd();

                        if(Player.get_ent() == null){
                            return;
                        }
                        //Point player_chunk = WorldChunk.get_chunk_coord(Player.get_ent().origin);
                        
                        if( WorldCluster.chunk_in_cluster(chunk_x, chunk_y)){
                        //if( player_chunk.getX() == chunk_x && player_chunk.getY() == chunk_y){

                            //System.out.println("chunk ["+chunk_x+","+chunk_y+"] is in player cluster");

                            glPointSize(4);
                            glBegin(GL_POINTS);
                            glColor4d(0.8f,0.2f,0.2f,1.0f);
                            glVertex2d(WorldChunk.CHUNK_SIZE*chunk_x,WorldChunk.CHUNK_SIZE*chunk_y);
                            glEnd();

                            //glColor3d(1,0,0);
                            //glVertex2d(-4,4);
                            for (int tile_x = chunk_x * WorldChunk.CHUNK_SIZE; tile_x < (chunk_x+1) * WorldChunk.CHUNK_SIZE ; tile_x++  )
                                for (int tile_y = chunk_y * WorldChunk.CHUNK_SIZE; tile_y < (chunk_y+1) * WorldChunk.CHUNK_SIZE ; tile_y++  ){

                                    

                                WorldTile tile = ClientGameEnvironment.getWorldLayer( Player.get_zindex()
                                        ).get_tile(tile_x, tile_y);
                                if (tile != null && tile.is_blocked()){

                                    //System.out.println("plotting point @["+tile_x+","+tile_y+"]");
                                    
                                    glPointSize(4);
                                    glBegin(GL_POINTS);

                                    if (tile.get_actor() != null){
                                        glColor4d(0.2f,0.2f,0.2f,1.0f);
                                    }else{
                                        glColor4d(0.5f,0.5f,0.5f,1.0f);
                                    }
                                    glVertex2d(tile_x,tile_y);
                                    glEnd();

                                }
                            }//chunk tiles loop
                        }//if chunk_in_cluster()

                }//region chunks loop
        }

    }

    @Override
    public void notify_event(Event e){
        //((NE_GUI_Element)this).notify_event(e);
        super.notify_event(e);

        if (e instanceof EEntityMove){
            EEntityMove move_event = (EEntityMove)e;
            if (move_event.entity.is_blocking()){
                if (visible){
                    expired = true;
                    update_map();
                }
            }
        }
    }
}
