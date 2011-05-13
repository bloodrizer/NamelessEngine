package events;

import events.network.NetworkEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import ne.ui.NE_GUI_System;
import world.Timer;

/**
 * EventManager keeps a track of event stack and makes a transitive rollback if event is timed out.
 */
public class EventManager {

    static Collection<IEventListener> listeners = new ArrayList<IEventListener>();
    public static Collection listeners_sync = Collections.synchronizedCollection(listeners);

    public static void subscribe(IEventListener listener){
        if (!listeners_sync.contains(listener)){
            listeners_sync.add(listener);
        }
    }

    public static int EVENT_TIMEOUT = 500000; //in ms

    public static void notify_event(Event event){
        
        if(event == null){
            return;
        }

        NE_GUI_System.e_on_event(event);

        /*
         *  Note, that event manager does not notify
         *  GUI System as regular listener.
         *  It makes explicit call to ensure that
         *  message is registered by GUI overlay first
         *  and dispatched if nececary
         */

        if (event.is_dispatched()){
            return; //do not allow to handle events, catched by gui overlay
        }

        
        register_event(event);

        synchronized(listeners_sync) {
            for (Iterator iter = listeners_sync.iterator(); iter.hasNext();) {
               IEventListener listener = (IEventListener) iter.next();

               listener.e_on_event(event);
            }
        }
    }

    public static void rollback_event(Event event){
        if(event == null){
            return;
        }


        /*System.out.println("rolling back event"+
                        event.toString()
        );*/

        unregister_event(event);
        synchronized(listeners_sync) {
            for (Iterator iter = listeners_sync.iterator(); iter.hasNext();) {
               IEventListener listener = (IEventListener) iter.next();

               listener.e_on_event_rollback(event);
            }
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
    public static List<Event> events_stack_sync = Collections.synchronizedList(new ArrayList<Event>());

    static void register_event(Event event){

        //prevent from registering non-network events
        if (event.is_local()){
            return;
        }

        event.set_timestamp(Timer.get_time());  //sign event with current Timer timestamp

        event.set_eventid(get_eventid());
        //System.out.println("adding network event "+event.toString());
        //System.out.println(event.classname());
        events_stack_sync.add(event);
    }
    
    static void unregister_event(Event event){
        if (event.is_local()){
            return;
        }
        events_stack_sync.remove(event);
      
    }
    //--------------------------------------------------------------------------

    public static void update(){
        garbage_collect();
    }
    //iterate through event list and rollback if event is expired
    static void garbage_collect(){

        Object[] event_tmp = events_stack_sync.toArray();

        for(int i=events_stack_sync.size()-1; i>=0; i--){
            NetworkEvent __event = (NetworkEvent)event_tmp[i];
            if (!__event.is_dispatched()){

                if (__event.get_age(Timer.get_time()) > EVENT_TIMEOUT){
                    //rollback_event(__event);
                    rollback_event_chain(i,event_tmp);
                }
            }else{
                /*System.out.println("cleaning up event #"+
                        Integer.toString(__event.get_eventid())
                );*/
                events_stack_sync.remove(__event);
            }
        }
    }

    public static void rollback_event_chain(int offset, Object[] event_tmp) {

        /*System.out.println("rolling back event chain @ #"+
                        Integer.toString(offset)
        );*/

        for( int i=event_tmp.length-1; i>=offset; i--){
            rollback_event((NetworkEvent)event_tmp[i]);
        }
    }

}
