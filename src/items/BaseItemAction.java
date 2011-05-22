/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package items;

import actions.IAction;

public class BaseItemAction implements IAction<BaseItem>{
    BaseItem owner = null;
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

    public void set_owner(BaseItem owner) {
        this.owner = owner;
    }

    public BaseItem get_owner() {
        return owner;
    }

}

