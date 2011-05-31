/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Administrator
 */
public class EntityManager {

    static final Collection<Entity> ent_list = new ArrayList<Entity>();
    public static Collection ent_list_sync = Collections.synchronizedCollection(ent_list);

    public static void add(Entity ent){
        ent_list_sync.add(ent);
    }

    public static synchronized void remove_entity(Entity ent){
        ent_list_sync.remove(ent);
    }

    public static Entity[] get_entities(){
        return (Entity[]) ent_list_sync.toArray(new Entity[0]);
    }

    public static Entity get_entity(int entity_id){
        Entity[] list = get_entities();
        for(int i = 0; i< list.length; i++){
            if (list[i].get_uid() == entity_id){
                return list[i];
            }
        }
        return null;
    }

}