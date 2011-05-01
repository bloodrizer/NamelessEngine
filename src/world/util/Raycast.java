/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package world.util;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.util.Point;
import render.WindowRender;

/**
 *
 * @author Administrator
 */
public class Raycast {

   static public Point getMousePosition(int mouseX, int mouseY)
    {
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        FloatBuffer winZ = BufferUtils.createFloatBuffer(1);

        float winX, winY;
        FloatBuffer position = BufferUtils.createFloatBuffer(3);

        GL11.glGetFloat( GL11.GL_MODELVIEW_MATRIX, modelview );
        GL11.glGetFloat( GL11.GL_PROJECTION_MATRIX, projection );
        GL11.glGetInteger( GL11.GL_VIEWPORT, viewport );

        winX = (float)mouseX;
        winY = (float)viewport.get(3) - (float)mouseY;

        GL11.glReadPixels(
                WindowRender.get_window_w(),
                WindowRender.get_window_h(),
        1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, winZ);

        GLU.gluUnProject(winX, winY, winZ.get(), modelview, projection, viewport, position);

        System.out.println(position);

        return new Point((int)position.get(),(int)position.get());
    }
}