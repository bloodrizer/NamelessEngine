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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import world.Timer;

/**
 *
 * @author Administrator
 */
public class EventManager {

    static Collection<IEventListener> listeners = new ArrayList<IEventListener>();
    public static Collection listeners_sync = Collections.synchronizedCollection(listeners);

    public static void subscribe(IEventListener listener){
        listeners_sync.add(listener);
    }

    public static int EVENT_TIMEOUT = 5000; //in ms

    public static void notify_event(Event event){

        if(event == null){
            return;
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
    //грубо говоря, это стэк событий, в котором по истечении какого-то таймаута их надо откатывать, но транзитивно
    static Collection<Event> events_stack = new ArrayList<Event>();
    public static List<Event> events_stack_sync = Collections.synchronizedList(new ArrayList<Event>());

    static void register_event(Event event){

        //prevent from registering non-network events
        if (event.is_local()){
            //System.out.println("local event, skipping");
            return;
        }

        event.set_timestamp(Timer.get_time());  //sign event with current Timer timestamp

        event.set_eventid(get_eventid());
        System.out.println("adding network event #"+Integer.toString(event.get_eventid()));
        System.out.println(event.classname());
        events_stack_sync.add(event);
    }
    
    static void unregister_event(Event event){
        //????????????????????
        if (event.is_local()){
            return;
        }

        events_stack_sync.remove(event);
        //????????????????????
        /*synchronized(events_stack_sync){

            for (Iterator iter = events_stack_sync.iterator(); iter.hasNext();) {
               Event __event = (Event) iter.next();

               if (__event.get_eventid() == event.get_eventid()){
                   events_stack_sync.remove(__event);
                   return;
               }
            }
        }*/
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
                System.out.println("cleaning up event #"+
                        Integer.toString(__event.get_eventid())
                );
                events_stack_sync.remove(__event);
            }
        }
    }

    public static void rollback_event_chain(int offset, Object[] event_tmp) {
        for( int i=event_tmp.length-1; i>=offset; i--){
            rollback_event((NetworkEvent)event_tmp[i]);
        }
    }

}
