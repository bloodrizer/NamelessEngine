/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import events.Event;
import events.EventManager;
import java.util.ArrayList;
import ne.Game;
import ne.ui.NE_GUI_System;

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

    public static EventManager eventManager = new EventManager(){
        
        @Override
        public void notify_event(Event event){  
            
            NE_GUI_System ui =  Game.get_game_mode().get_ui().get_nge_ui();
            if(ui!=null){
                ui.e_on_event(event);
            }

            /*
             *  Note, that event manager does not notify
             *  GUI System as regular listener.
             *  It makes explicit call to ensure that
             *  message is registered by GUI overlay first
             *  and dispatched if nececary
             */
            
            super.notify_event(event);
        }
        
    };

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
