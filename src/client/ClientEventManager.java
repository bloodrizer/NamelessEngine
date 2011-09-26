/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import events.Event;
import events.EventManager;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */

/*
 * It handels local client-side events and shit
 *
 *
 */

public class ClientEventManager {
    public static ArrayList<Event> scheduledEvents = new ArrayList<Event>();

    public static EventManager eventManager = new EventManager();

    public static void addEvent(Event event){
        scheduledEvents.add(event);
    }

    public static void update(){
        for (Event event: scheduledEvents){
            event.post();
        }

        scheduledEvents.clear();
    }
}
