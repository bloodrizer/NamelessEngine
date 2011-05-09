/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

/**
 *
 * @author Administrator
*/

public class BaseEntityAction implements IEntityAction{
    Entity ent = null;
    String name = "undefined";

    public void set_name(String name){
        this.name = name;
    }

    public String get_name(){
        return this.name;
    }
    
    public void set_entity(Entity ent) {
        this.ent = ent;
    }

    public void execute() {
        //do nothing
    }

    

    /*public <A> Object produce(Class a) throws InstantiationException, IllegalAccessException{
        A action = (A) a.newInstance();

        return action;
    }*/
}
