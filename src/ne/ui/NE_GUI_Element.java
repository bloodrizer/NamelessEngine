/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.EKeyPress;
import events.EMouseClick;
import events.EMouseDrag;
import events.EMouseRelease;
import events.Event;
import java.util.ArrayList;
import java.util.Collection;
import render.WindowRender;
import ui.IUserInterface;

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

    public int get_x(){
        if (parent!=null){
            return this.x + parent.get_x();
        }
            return this.x;
    }

    public int get_y(){
        if (parent!=null){
            return this.y + parent.get_y();
        }
            return this.y;
    }


    protected NE_GUI_Element parent = null;
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

    public boolean dragable = true;
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

        //catch mouse click inside of control area

        if (e instanceof EMouseClick){

            EMouseClick event = (EMouseClick)e;

            //this hack allows other controls to lose focus, even if event was dispatched
            if (event.is_dispatched()){
                e_on_mouse_out_click(event);
                return;
            }

            int mx = event.origin.getX();
            int my = WindowRender.get_window_h() - event.origin.getY();

            /*
             * now we should translate this coords into
             * local coord system, sert by our parent control
             */
            if (parent != null){
                mx = mx - parent.x;
                my = my - parent.y;
            }


            if( mx > x     &&
                mx < x+w   &&
                my > y     &&
                my < y+h
           ){
                if(!solid){
                    return; //do not check bounding for non-solid controls
                }
                
                System.out.println(
                        this.toString()+" event in working area!"
                );
                System.out.println(event.origin);

                e.dispatch();
                e_on_mouse_click(event);
                drag_start = true;
            }else{
                e_on_mouse_out_click(event);
            }
        }

         if (e instanceof EMouseRelease){
             drag_start = false;
         }

        //todo: check if draggable!!!!
        if (e instanceof EMouseDrag){
            EMouseDrag event = (EMouseDrag)e;

            if (drag_start && dragable){

                drag((int)event.dx, (int)event.dy);
            }

        }

        if (e instanceof EKeyPress){
            EKeyPress event = (EKeyPress)e;

            e_on_key_press(event);
        }
    }

    public void drag(int dx, int dy){
        x = x + dx;
        y = y - dy;
    }

    public void e_on_mouse_click(EMouseClick e){
        System.out.println("NE_GUI_Element::click");
    }

    public void e_on_mouse_out_click(EMouseClick e){
        //override me!
    }
    
    public void e_on_key_press(EKeyPress e){
        //override me!
    }

    public void clear() {
        Object[] elem =  children.toArray();
        for(int i = 0; i<elem.length; i++){
            NE_GUI_Element __elem = (NE_GUI_Element)elem[i];
            __elem.clear();
        }

        children.clear();
    }


    private IUserInterface controller;
    public void set_controller(IUserInterface controller){
        this.controller = controller;
    }
}
