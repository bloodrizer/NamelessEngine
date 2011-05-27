/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.network;

import events.Event;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

/**
 *
 * @author Administrator
 */
@interface NetID {
    String id();
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

    public String get_id(){
        return "0x0000";
    }

    public String[] serialize(){
        return new String[] {get_id()};
    }
}
