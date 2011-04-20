/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package render;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Administrator
 */
public class Render {

    static int WINDOW_W = 800;
    static int WINDOW_H = 600;

    public static void create() throws LWJGLException {
 
            Display.setDisplayMode(new DisplayMode(WINDOW_W, WINDOW_H));
            Display.create();
            Display.setTitle("The Nameless Engine");
            Display.setVSyncEnabled(true);

            Render.initGL(WINDOW_W, WINDOW_H);
        
    }

    public static void initGL(int w, int h){

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GL11.glViewport(0, 0, w, h);
        GL11.glOrtho(0.0f, w, h, 0.0f, -1.0f, 1.0f);

        GL11.glClearColor(0, 0, 0, 1);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //GL11.glShadeModel(GL11.GL_SMOOTH);
    }

    public static void destroy(){
        Display.destroy();
    }
}
