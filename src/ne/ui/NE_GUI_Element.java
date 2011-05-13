/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.EMouseClick;
import events.EMouseDrag;
import events.EMouseRelease;
import events.Event;
import java.util.ArrayList;
import java.util.Collection;
import render.WindowRender;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Element {
    Collection<NE_GUI_Element> children = new ArrayList<NE_GUI_Element>();

    public synchronized void add(NE_GUI_Element child){
        children.add(child);
        child.set_parent(this);
    }

    public boolean remove(NE_GUI_Element child){
        return children.remove(child);
    }

    public int w = 200;
    public int h = 200;

    public int x = 100;
    public int y = 100;

    private NE_GUI_Element parent = null;
    public void set_parent(NE_GUI_Element parent){
        this.parent = parent;
    }

    public boolean visible = true;
    public boolean solid = true;

    public void render(){
        if (children.isEmpty()){
            return;
        }

        Object[] elem =  children.toArray();
        for(int i = 0; i<elem.length; i++){
            NE_GUI_Element __elem = (NE_GUI_Element)elem[i];
            __elem.render();
        }
    }

    boolean drag_start = false;

    public void notify_event(Event e){
        //invisible controls do not handle events
        if(!visible){
            return;
        }

        //allow children to catch event and dispatch it,
        //before we handle it in parent control

        Object[] elem =  children.toArray();
        for(int i = 0; i<elem.length; i++){
            NE_GUI_Element __elem = (NE_GUI_Element)elem[i];
            __elem.notify_event(e);
        }
        
        
        if (e.is_dispatched()){
            return;
        }

        if(!solid){
            return; //do not check bounding for non-solid controls
        }

        //catch mouse click inside of control area

        if (e instanceof EMouseClick){

            EMouseClick event = (EMouseClick)e;

            int mx = event.origin.getX();
            int my = WindowRender.get_window_h() - event.origin.getY();

            if( mx > x     &&
                mx < x+w   &&
                my > y     &&
                my < y+h
           ){
                System.out.println("event in working area!");
                System.out.println(event.origin);

                e.dispatch();
                e_on_mouse_click(event);
                drag_start = true;
            }
        }

         if (e instanceof EMouseRelease){
             drag_start = false;
         }

        //todo: check if draggable!!!!
        if (e instanceof EMouseDrag){
            EMouseDrag event = (EMouseDrag)e;

            if (drag_start){
                x = x + (int)event.dx;
                y = y - (int)event.dy;
            }

        }
    }

    public void e_on_mouse_click(EMouseClick e){
        //override me!
    }

}
