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
    private long timestamp = 0;

    //todo: move to network event?
    public void set_timestamp(long timestamp){
        this.timestamp = timestamp;
    }

    public long get_timestamp(){
        return timestamp;
    }

    public long get_age(long timestamp){
        return timestamp - this.timestamp;
    }
    //todo end
    
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

    public String classname(){
        return this.getClass().getName();
    }
    //todo: move to local event?
    public boolean is_local(){
        return true;
    }

}
