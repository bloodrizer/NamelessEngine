/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

import game.ent.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
class EventManager {

    static Collection<EventListener> listeners = new ArrayList<EventListener>();
    public static Collection listeners_sync = Collections.synchronizedCollection(listeners);

    public static void add(EventListener listener){
        listeners_sync.add(listener);
    }

    public static void notify_event(Event event){

        if(event == null){
            return;
        }
        register_event(event);

        for (Iterator iter = listeners_sync.iterator(); iter.hasNext();) {
           EventListener listener = (EventListener) iter.next();
           
           listener.e_on_event(event);
        }
    }

    public static void rollback_event(Event event){
        if(event == null){
            return;
        }

        unregister_event(event);

        for (Iterator iter = listeners_sync.iterator(); iter.hasNext();) {
           EventListener listener = (EventListener) iter.next();

           listener.e_on_event_rollback(event);
        }
    }

    //--------------------------------------------------------------------------
    //              keep a track of game events so we could rollback them
    //--------------------------------------------------------------------------
    //should return unassigned event id
    //todo: check for possible overflow
    public static int eventid = 0;
    public static int get_eventid(){
        eventid++;
        return eventid;
    }

    static Collection<Event> events_stack = new ArrayList<Event>();
    public static Collection events_stack_sync = Collections.synchronizedCollection(events_stack);

    static void register_event(Event event){
        event.set_eventid(get_eventid());
        events_stack_sync.add(event);
    }
    
    static void unregister_event(Event event){
        for (Iterator iter = events_stack_sync.iterator(); iter.hasNext();) {
           Event __event = (Event) iter.next();

           if (__event.get_eventid() == event.get_eventid()){
               events_stack_sync.remove(__event);
               return;
           }
        }
    }
    //--------------------------------------------------------------------------
}
