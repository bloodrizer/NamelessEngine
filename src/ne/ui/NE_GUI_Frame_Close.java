/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;
import events.EMouseClick;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
/**
 *
 * @author Administrator
 */
public class NE_GUI_Frame_Close extends NE_GUI_Element{
    
    @Override
     public void render(){

        NE_GUI_Frame owner = (NE_GUI_Frame)parent;
        owner.render_window_tile(
                owner.t_window_w-1,
                0,
                3);    //close button tile
    }

    @Override
    public void e_on_mouse_click(EMouseClick e){
        parent.visible = false;
    }
}
