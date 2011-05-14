/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.Event;

/**
 *
 * @author Administrator
 */
public class NE_GUI_System {
    public static final NE_GUI_Element root = new NE_GUI_Element(); //big invisible container

    public static void render(){
        //glEnable(GL_TEXTURE_2D);
        //glDisable(GL_BLEND);

        root.render();

        //glEnable(GL_BLEND);
    }

    public void clear(){
        root.clear();
    }


    public NE_GUI_System(){

        //init root coord system
        root.x = 0;
        root.y = 0;

        //EventManager.subscribe(this);

        
    }

    public static void e_on_event(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
        root.notify_event(event);
    }

    public static void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
