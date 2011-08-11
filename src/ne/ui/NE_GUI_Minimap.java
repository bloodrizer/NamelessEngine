/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import world.WorldTile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import render.Render;
import world.WorldChunk;
import world.WorldCluster;
import world.WorldModel;
import world.WorldRegion;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Minimap extends NE_GUI_FrameModern {

    static final boolean fbo_enabled = GLContext.getCapabilities().GL_EXT_framebuffer_object;
    int fbo_id;


    int texture_id;
    
    boolean expired = true;
    
    public NE_GUI_Minimap(){
        super(true);

        if (fbo_enabled){
            fbo_id = EXTFramebufferObject.glGenFramebuffersEXT();
            EXTFramebufferObject.glBindFramebufferEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fbo_id );

            texture_id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_id);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);


            glTexImage2D(GL_TEXTURE_2D, 0,
                    GL_RGBA8, 512, 512, 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, (ByteBuffer)null);

            EXTFramebufferObject.glFramebufferTexture2DEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,
                GL11.GL_TEXTURE_2D, texture_id, 0);


                //EXTFramebufferObject.glBindFramebufferEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fbo_id );

            //------------------------------------------------------------------
            int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);
            if(status != GL_FRAMEBUFFER_COMPLETE_EXT) {
                System.err.println(status);
            }


            update_map();

        }else{
            System.err.println("FBO is not supported, minimap is disabled");
        }
    
        
        set_tw(15);
        set_th(15);
        center();
    }

    /*
     * Prepares FBO for rendering path
     */
    private void render_begin(){

        EXTFramebufferObject.glFramebufferTexture2DEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,
            GL11.GL_TEXTURE_2D, texture_id, 0);

        glPushAttrib(GL_VIEWPORT_BIT | GL_TRANSFORM_BIT | GL_COLOR_BUFFER_BIT | GL_SCISSOR_BIT);
        glDisable(GL_SCISSOR_TEST);
        glViewport(0, 0, 512, 512);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, 512, 512, 0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glDisable(GL_SCISSOR_TEST);
    }

    private void render_end(){
        EXTFramebufferObject.glBindFramebufferEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glPopAttrib();
    }

    @Override
    public void render(){

        super.render();

        if (!fbo_enabled){
            return;
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_id);

        //Render.precache_texture("/render/tileset1.png");

        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        
        GL11.glBegin(GL11.GL_QUADS);        //<-- white quad there
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(get_x(), get_y(), 0);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(get_x()+200, get_y(), 0);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(get_x()+200, get_y()+200, 0);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(get_x(), get_y()+200, 0);
        GL11.glEnd();

    }

    //update vitual map texture

    public void update_map() {
        if (!expired){
            return;
        }

        expired = false;
        
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        
      
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);


        render_begin();

        //------render start
        Render.precache_texture("/render/tileset1.png");

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(10.0f, 10.0f, 0);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(90.0f, 10.0f, 0);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(90.0f, 90.0f, 0);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(10.0f, 90.0f, 0);
        GL11.glEnd();

        generate_minimap();

        //------render end

        render_end();
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
        glBegin(GL_POINTS);
        
        for( int i=0; i<3; i++)
            for (int j=0; j<3; j++){
                //draw region
                for( int chunk_x = i*WorldRegion.REGION_SIZE; chunk_x< (i+1)*WorldRegion.REGION_SIZE; chunk_x++)
                    for( int chunk_y = j*WorldRegion.REGION_SIZE; chunk_y< (j+1)*WorldRegion.REGION_SIZE; chunk_y++){
                        
                        if( WorldCluster.chunk_in_cluster(chunk_x, chunk_y)){
                            //glColor3d(1,0,0);
                            //glVertex2d(-4,4);
                            for (int tile_x = chunk_x * WorldChunk.CHUNK_SIZE; tile_x < (chunk_x+1) * WorldChunk.CHUNK_SIZE ; tile_x++  )
                                for (int tile_y = chunk_y * WorldChunk.CHUNK_SIZE; tile_y < (chunk_y+1) * WorldChunk.CHUNK_SIZE ; tile_y++  ){
                                
                                
                                
                                WorldTile tile = WorldModel.get_tile(chunk_x, chunk_y, WorldModel.GROUND_LAYER);
                                if (tile.is_blocked()){
                                    glColor3d(0,0,0);
                                    glVertex2d(chunk_x,chunk_y);
                                }
                            }//chunk tiles loop
                        }//if chunk_in_cluster()

                }//region chunks loop
        }
        
        glEnd();
    }
}
