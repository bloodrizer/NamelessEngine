/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import events.Event;
import events.IEventListener;
import java.net.ServerSocket;

/**
 *
 * @author Administrator
 */

public class Io implements IEventListener {

    private ServerSocket tcp_sock = null;

    public Io(){
        try {
            tcp_sock = new ServerSocket();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void update(){
        
    }

    private void send_network_event(Event event){
        //System.event
    }


    public void e_on_event(Event event){

    }
    
    public void e_on_event_rollback(Event event){

    }
}
