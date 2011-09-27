/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import events.network.EChatMessage;
import items.BaseItem;

/**
 * Actor is animated entity. It can move, think, participate in combat and use items
 */
public class EntityActor extends Entity {
    
    public void die(Entity killer){
        
    }

    public void say_message(String text){
        EChatMessage message = new EChatMessage(get_uid(),text);
        message.set_local(true);
        message.setManager(eventManager);
        message.post();
    }

    /*
     * Every actor can have ONE active item at time. It may be torch or weapon or whatever
     */

    BaseItem active_item;

    public BaseItem get_active_item(){
        if(active_item!=null){
            return active_item.getItem();
        }
        return null;
    }

    public void set_active_item(BaseItem item){
        if (item == null){
            active_item = null;
        }else{
            active_item = item.getItem();
        }

        e_on_change_item();
    }

    public void e_on_change_item(){
        
    }
}
