/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import render.Render;
import render.Tileset;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Frame extends NE_GUI_Element{

    float r = 0.0f;
    float g = 0.0f;
    float b = 0.0f;

    public NE_GUI_Frame(){
        r = (float)Math.random();
        g = (float)Math.random();
        b = (float)Math.random();
    }

    @Override
    public void render(){
        
        //Render.bind_texture("tileset1.png");
        glColor3f(r,g,b);


        glBegin(GL_QUADS);
            glVertex2f( x,   y);
            glVertex2f( x+w, y);
            glVertex2f( x+w, y+h);
            glVertex2f( x,   y+h);
        glEnd();
        
        super.render();
    }

}
