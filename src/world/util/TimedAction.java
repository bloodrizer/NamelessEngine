/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package world.util;

import events.Event;
import events.IEventListener;

/**
 *
 * @author bloodrizer
 * 
 * 
 * 
 * 
 * This shit is base class for every timed action that may be triggered by a player
 */
public abstract class TimedAction {
    
    abstract void perform_action();

}
