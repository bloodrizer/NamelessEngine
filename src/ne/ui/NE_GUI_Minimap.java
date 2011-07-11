/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import render.Render;

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




            /*glBlendFuncSeparate(
                GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA,
                GL_ZERO, GL_ONE_MINUS_SRC_ALPHA);*/
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);


            render_begin();

            //------render start
            //GL11.glColor3f(0.9f, 0.1f, 0.1f);
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

            //------render end

            render_end();
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

    public void update_map() {

    }
}
