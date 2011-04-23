/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events;

/**
 *
 * @author Administrator
 */
public class NetworkEvent extends Event {

    @Override
    public boolean is_local(){
        return false;
    }

    private boolean dispatched = false;
    public boolean is_dispatched(){
        return dispatched;
    }

    public void dispatch(){
        dispatched = true;
    }
}
