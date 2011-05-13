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

    private boolean synchronised = false;
    public boolean is_synchronised(){
        return synchronised;
    }

    public void synchronise(){
        synchronised = true;
    }
}
