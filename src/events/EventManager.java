package events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import ne.Game;
import ne.ui.NE_GUI_System;

/**
 * EventManager keeps a track of event stack and makes a transitive rollback if event is timed out.
 */
public class EventManager {

    Collection<IEventListener> listeners = new ArrayList<IEventListener>();
    public Collection listeners_sync = Collections.synchronizedCollection(listeners);

    public void subscribe(IEventListener listener){
        if (!listeners_sync.contains(listener)){
            listeners_sync.add(listener);
        }
    }

    public static int EVENT_TIMEOUT = 500000; //in ms

    public void notify_event(Event event){
        
        if(event == null){
            return;
        }


        if (event.is_dispatched()){
            return; //do not allow to handle events, catched by gui overlay
        }

        IEventListener[] __listeners = (IEventListener[]) listeners_sync.toArray(new IEventListener[0] );
        for(int i=0; i<__listeners.length;i++){
            __listeners[i].e_on_event(event);
        }

    }

    public static void rollback_event(Event event){

    }

    public static void update(){
    }
}
