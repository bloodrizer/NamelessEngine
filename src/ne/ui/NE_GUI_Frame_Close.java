/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;
import render.AreaRenderer;
import events.EMouseClick;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
/**
 *
 * @author Administrator
 */
public class NE_GUI_Frame_Close extends NE_GUI_Element{
    AreaRenderer area_renderer = new AreaRenderer();
    {
        area_renderer.set_rect(100, 32, 14, 14);
    }
    
    @Override
     public void render(){

        area_renderer.render(parent.get_x() + parent.w - 20, parent.get_y() + 8);

        /*if (parent instanceof NE_GUI_Frame){
            NE_GUI_Frame owner = (NE_GUI_Frame)parent;
            owner.render_window_tile(
                    owner.t_window_w-1,
                    0,
                    3);    //close button tile
        }else{
             NE_GUI_Frame owner = (NE_GUI_FrameModern) parent;
        }*/
        
    }

    @Override
    public void e_on_mouse_click(EMouseClick e){
        parent.visible = false;
    }
}
