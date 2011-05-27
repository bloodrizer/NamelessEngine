/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package events.network;

import events.network.NetworkEvent;

/**
 *
 * @author Administrator
 */
public class ESelectCharacter extends NetworkEvent{

    @Override
    public String get_id(){
        return "0x0026";
    }

    int char_id = 123;
    public ESelectCharacter(){
    }


    @Override
    public String[] serialize(){
        return new String[] {
            get_id(),
            Integer.toString(char_id)
        };
    }
}
