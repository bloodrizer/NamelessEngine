/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.Event;
import java.util.ArrayList;
import java.util.Collection;

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

    public int x = 5;
    public int y = 5;

    private NE_GUI_Element parent = null;
    public void set_parent(NE_GUI_Element parent){
        this.parent = parent;
    }

    public boolean visible = true;

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

    public void notify_event(Event e){

        Object[] elem =  children.toArray();
        for(int i = 0; i<elem.length; i++){
            NE_GUI_Element __elem = (NE_GUI_Element)elem[i];
            __elem.notify_event(e);
        }
        //override me!
    }

}
