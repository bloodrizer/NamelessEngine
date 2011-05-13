/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.Event;
import events.EventManager;
import org.lwjgl.opengl.GL11;
import events.IEventListener;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
/**
 *
 * @author Administrator
 */
public class NE_GUI_System {
    public static final NE_GUI_Element root = new NE_GUI_Element(); //big invisible container

    public static void render(){
        //glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);

        root.render();

        glEnable(GL_BLEND);
    }


    public NE_GUI_System(){

        //EventManager.subscribe(this);

        NE_GUI_Frame frame = new NE_GUI_Frame();
        root.add(frame);
        frame.set_tw(8);
        frame.set_th(5);
    }

    public static void e_on_event(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
        root.notify_event(event);
    }

    public static void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
