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

    static Collection<Entity> ent_list = new ArrayList<Entity>();
    public static Collection ent_list_sync = Collections.synchronizedCollection(ent_list);

    public static void add(Entity ent){
        ent_list_sync.add(ent);
    }

    public static synchronized void remove_entity(Entity ent){
        ent_list_sync.remove(ent);
    }


}