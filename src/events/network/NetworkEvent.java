/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.network;

import events.Event;

/**
 *
 * @author Administrator
 */
@interface NetID {
    int id();
}


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
