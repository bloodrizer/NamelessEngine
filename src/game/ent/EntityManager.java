/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import client.ClientEventManager;
import events.EEntitySpawn;
import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.EEntityMove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class EntityManager implements IEventListener{
    
    //TODO: assign to the world layer


    /*static final ArrayList<Entity> ent_list = new ArrayList<Entity>();
    public static Collection ent_list_sync = Collections.synchronizedCollection(ent_list);*/

    public static HashMap<Integer, ArrayList<Entity>> layer_ent_list = new HashMap<Integer, ArrayList<Entity>>(100);

    public static void add(Entity ent, int layer_id){
        ent.setLayerId(layer_id);      
        ArrayList<Entity> entList = getList(layer_id);

        if (!entList.contains(ent)){
            entList.add(ent);
        }
    }
    
    public static ArrayList<Entity> getList(int layer_id){
         ArrayList<Entity> entList = layer_ent_list.get(layer_id);

        if (entList == null){
            entList = new ArrayList<Entity>();
            layer_ent_list.put(layer_id, entList);
        }
        
        return entList;
    }

    private static final EntityManager instance = new EntityManager();
    static {
        ClientEventManager.eventManager.subscribe(instance);
    }

    /*
     * This method is called whether entity moves or spawns
     * to be sure that render order is correct
     */
    public static void update(){
        for(ArrayList<Entity> list: layer_ent_list.values()){
            Collections.sort(list);
        }
    }

    public static boolean has_ent(Entity ent, int layer_id){
        return getList(layer_id).contains(ent);
    }

    public static void remove_entity(Entity ent, int layer_id){
        getList(layer_id).remove(ent);
    }
    
    public static void remove_entity(Entity ent){
        for (int layer_id: layer_ent_list.keySet()){
            remove_entity(ent, layer_id);
        }
    }

    public static Entity[] getEntities(int layer_id){
        return (Entity[]) getList(layer_id).toArray(new Entity[0]);
    }

    public static Entity get_entity(int entity_id, int layer_id){
        for (Entity ent: getList(layer_id)){
            if (ent.get_uid() == entity_id){
                return ent;
            }
        }
        return null;
    }

    /*
     * Search for entity in whole list
     */
    public static Entity get_entity(int uid) {
        for (int layer_id: layer_ent_list.keySet()){
            Entity ent = get_entity(uid, layer_id);
            if (ent!=null){
                return ent;
            }
        }
        return null;
    }

    public void e_on_event(Event event) {
        if (event instanceof EEntitySpawn){
            update();
        }
        if (event instanceof EEntityMove){
            update();
        }
    }

    public void e_on_event_rollback(Event event) {
    }

}