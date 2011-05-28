/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import actions.IAction;

/**
 *
 * @author Administrator
*/

public class BaseEntityAction implements IAction<Entity>{
    protected Entity owner = null;
    protected String name = "undefined";

    public void set_name(String name){
        this.name = name;
    }

    public String get_name(){
        return this.name;
    }

    public void execute() {
        //do nothing
    }

    public void set_owner(Entity owner) {
        this.owner = owner;
    }

    public Entity get_owner() {
        return owner;
    }
}
