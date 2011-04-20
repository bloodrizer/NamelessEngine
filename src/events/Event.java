/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

/**
 *
 * @author Administrator
 */
public class Event {

    private int eventid = 0;
    public void set_eventid(int eventid){
        this.eventid = eventid;
    }
    public int get_eventid(){
        return eventid;
    }

    public void post(){
        EventManager.notify_event(this);
    }

    public void rollback(){
        //override me
    }
}
