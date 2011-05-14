/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import org.lwjgl.input.Keyboard;
import events.EKeyPress;
import events.EMouseClick;
import render.overlay.OverlaySystem;
import org.newdawn.slick.Color;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
/**
 *
 * @author Administrator
 */
public class NE_GUI_Input extends NE_GUI_Element{

    public String text = "test";

    public NE_GUI_Input(){
        h = OverlaySystem.FONT_SIZE+4;
    }

    @Override
    public void render(){

        int x = this.x + parent.x;
        int y = this.y + parent.y;

        glDisable(GL_TEXTURE_2D);
        
        glColor4f(0.9f,0.9f,0.9f,1.0f);
        glBegin(GL_QUADS);
                glVertex2f( x,   y);
                glVertex2f( x+w, y);
                glVertex2f( x+w, y+h);
                glVertex2f( x,   y+h);
        glEnd();

        glEnable(GL_TEXTURE_2D);

        if (!focused){
            OverlaySystem.ttf.drawString(x+2,y, text, Color.black);
        }else{
            OverlaySystem.ttf.drawString(x+2,y, text+"|", Color.black);
        }
    }

    boolean focused = false;

    @Override
    public void e_on_mouse_click(EMouseClick e){
        //override me!
        focused = true;
    }

    @Override
    public void e_on_mouse_out_click(EMouseClick e){
        //override me!
        focused = false;
    }

    @Override
    public void e_on_key_press(EKeyPress e){
        if(!focused){
            return;
        }
        if (e.key == Keyboard.KEY_BACK){
            if (text.length()>0){
                text = text.substring(0,text.length()-1);
            }
        }else{
            text = text + e.chr;
        }
    }
}
