/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import events.EEntitySpawn;
import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.EEntityMove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Administrator
 */
public class EntityManager implements IEventListener{


    static final ArrayList<Entity> ent_list = new ArrayList<Entity>();
    public static Collection ent_list_sync = Collections.synchronizedCollection(ent_list);

    public static void add(Entity ent){
        if (!ent_list.contains(ent)){
            ent_list.add(ent);

            //Collections.sort(ent_list);
        }
    }

    private static final EntityManager instance = new EntityManager();
    static {
        EventManager.subscribe(instance);
    }

    /*
     * This method is called whether entity moves or spawns
     * to be sure that render order is correct
     */
    public static void update(){
        Collections.sort(ent_list);
    }

    //sort this shit based on the isometric order
    public static void isometric_sort(){
        Entity[] list = get_entities();
        for(int i = 0; i< list.length; i++){
            
        }
    }

    public static boolean has_ent(Entity ent){
        return ent_list.contains(ent);
    }

    public static synchronized void remove_entity(Entity ent){
        ent_list.remove(ent);
    }

    public static Entity[] get_entities(){
        return (Entity[]) ent_list.toArray(new Entity[0]);
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

    public void e_on_event(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");

        if (event instanceof EEntitySpawn){
            update();
        }
        if (event instanceof EEntityMove){
            update();
        }
    }

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}