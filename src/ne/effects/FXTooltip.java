/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.effects;

import ne.ui.NE_GUI_Element;
import world.Timer;

/**
 *
 * @author Administrator
 */
public class FXTooltip extends FXTextBubble {
    public boolean enabled = true;
    
    NE_GUI_Element gui_elem;
    
    public FXTooltip(NE_GUI_Element gui_elem){
        super(null);    //no message event for tooltip
        life_time = 500;
        
        this.gui_elem = gui_elem;
    }

    public void refresh(){
        spawn_time = Timer.get_time();
    }

    private boolean is_expired() {
        return enabled;
    }
    
    public void disable(){
        enabled = false;
    }
    
    public void render(){
        if (gui_elem == null){
            return;
        }
        
        render_bubble(gui_elem.get_x(), gui_elem.get_y());
    }
    
    

}
