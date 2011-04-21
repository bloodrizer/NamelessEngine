/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

/**
 *
 * @author Administrator
 */
public interface IEventListener {
    public void e_on_event(Event event);

    public void e_on_event_rollback(Event event);
}
