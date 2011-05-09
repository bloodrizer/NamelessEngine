/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class EntActionList {
    ArrayList<IEntityAction> action_list = new ArrayList<IEntityAction>(5);

    Entity owner = null;
    public void set_owner( Entity owner){
        this.owner = owner;
    }

    public void add_action(IEntityAction action, String name){
        action_list.add(action);
        action.set_name(name);
        action.set_entity(owner);
    }

    public IEntityAction[] get_action_list(){
        return (IEntityAction[]) action_list.toArray(new IEntityAction[0]);
    }
}
