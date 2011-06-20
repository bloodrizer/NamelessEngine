/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import events.network.EChatMessage;

/**
 *
 * @author Administrator
 */
public class EntityActor extends Entity {
    
    public void die(Entity killer){
        
    }

    public void say_message(String text){
        EChatMessage message = new EChatMessage(get_uid(),text);
        message.set_local(true);
        message.post();
    }
}
