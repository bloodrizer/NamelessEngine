/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.Event;
import events.EventManager;
import org.lwjgl.opengl.GL11;
import events.IEventListener;
/**
 *
 * @author Administrator
 */
public class NE_GUI_System implements IEventListener {
    public static final NE_GUI_Element root = new NE_GUI_Element();

    public static void render(){
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        root.render();
    }


    public NE_GUI_System(){

        EventManager.subscribe(this);

        NE_GUI_Frame frame = new NE_GUI_Frame();
        root.add(frame);
    }

    public void e_on_event(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
        root.notify_event(event);
    }

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
