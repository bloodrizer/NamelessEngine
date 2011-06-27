/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ent;

import actions.IAction;
import events.EEntitySpawn;
import events.network.EEntityMove;
import game.ai.AI;
import game.combat.Combat;
import game.ent.Entity;
import game.ent.controller.IEntityController;
import items.ItemContainer;
import java.lang.Object;
import java.util.ArrayList;
import org.lwjgl.util.Point;
import render.DebugRenderer;
import render.EntityRenderer;
import world.Timer;
import world.WorldChunk;
import world.WorldTile;

/**
 *
 * @author Administrator
 */
public class Entity implements Comparable {

    public Point origin;

    public WorldTile tile;  //the tile entity is currently assigned to
    /*
     * Combat handles all in-game combat mechanic, as stats, damage infliction and damage taking
     */
    protected Combat combat;

    protected AI ai;

    /*
     * This is an entity offset in tile coord system
     * It's used to allow smooth entity movement from tile to tile - for a player using NPC controller or for a npc using lerp
     */

    public float light_amt = 0.0f;
    
    public float dx = 0.0f;
    public float dy = 0.0f;

    private int uid = 0;
    private long next_think;

    public IEntityController controller;

    private WorldChunk chunk = null;

    private boolean blocking = false;

    /*
     * This flag indicates that this entity is no longer required by WorldModel
     * This entity will be cleared with next gc cycle.
     */
    protected boolean garbage = false;
    public boolean is_garbage(){
        return garbage;
    }
    public void trash(){
        garbage = true;
    }

    public boolean is_blocking(){
        return blocking;
    }
    public void set_blocking(boolean blocking){
        this.blocking = blocking;
    }

    //helper functions for code beautification
    public int x(){
        return origin.getX();
    }

    public int y(){
        return origin.getY();
    }

    public void set_combat(Combat combat){
        this.combat = combat;
        combat.set_owner(this);
    }
    public void set_ai(AI ai){
        this.ai = ai;
        ai.set_owner(this);
    }

    public Combat get_combat(){
        return this.combat;
    }

    public int compareTo(Object ent) {
        if (!(ent instanceof Entity)){
            throw new ClassCastException("Entity instance expected.");
        }

        int hc_this = this.origin.getY()*10000 + this.origin.getX();

        if(ent == null || ((Entity)ent).origin == null){
            return hc_this;
        }

        //hashcode generation allows up to 32 entities stored in the tile

        int hc_ent = ((Entity)ent).origin.getY()*10000 + ((Entity)ent).origin.getX()*32;

        //non-decal ents use upper half of z-stack
        if (!(ent instanceof EntityDecal)){
            hc_ent += 16;
        }

        //TODO: implement z-ordering algorythm for multiple decals if any presents
        //like floor tiles renders prior to item ents

        return hc_this - hc_ent;
    }

    public void set_uid(int uid) {
        this.uid = uid;
    }

    public enum Orientation {
        ORIENT_N,
        ORIENT_W,
        ORIENT_S,
        ORIENT_E
    }
    public Orientation orientation = Orientation.ORIENT_N;

    public int get_uid(){
        return this.uid;
    }

    //--------------------------------------------------------------------------
    public void spawn(int uid, Point origin){
        EntityManager.add(this);

        this.uid = uid;
        this.origin = origin;


        EEntitySpawn spawn_event = new EEntitySpawn(this,origin);
        spawn_event.post();

        set_next_think(Timer.get_time());
    }

    //this is a debug version of spawn method, that use temporary timestamp-based uid
    public void spawn(Point origin){
        int __uid = (int)Timer.get_time();
        spawn(__uid, origin);
    }

    //--------------------------------------------------------------------------
    public void set_chunk(WorldChunk chunk){
        this.chunk = chunk;
    }
    
    public WorldChunk get_chunk(){
        return this.chunk;
    }

    public boolean in_chunk(WorldChunk chunk){
       if (chunk == null) {
           System.out.println("Entity::in_chunk() - ent chunk is null");
           return false;
       }
       //note: for some strange reason there are different object pointers that are compared
       return (this.chunk.origin.equals(chunk.origin));
    }

    public void think(){
        if (ai != null){
            ai.think();
        }
    }

    public void update(){
        if (controller != null){
            controller.think();
        }
        if (ai != null){
            ai.update();
        }
    }

    public long frame_time_ms = 0;
    public long next_frame;

    public boolean is_next_frame(long current_time_ms){
        if (current_time_ms > next_frame){
            next_frame = current_time_ms;
            return true;
        }
        return false;
    }
        
    public void next_frame(){
        next_frame = next_frame+frame_time_ms;

        EntityRenderer renderer = get_render();
        if (renderer!=null){
            renderer.next_frame();
        }
    }

    public void set_next_frame(long frame_time_ms) {
        this.frame_time_ms = frame_time_ms;
        
    }

    public void set_controller(IEntityController controller){
        this.controller = controller;
        controller.attach(this);
    }


    public void move_to(Point dest){
        EEntityMove event = new EEntityMove(this, dest);
        event.post();
    }


    public void sleep(long sleep_time_ms){
        set_next_think(next_think+sleep_time_ms);
    }

    public void set_next_think(long time_ms){
        next_think = time_ms;
    }

    public boolean is_awake(long current_time_ms){
        if (current_time_ms > next_think){
            next_think = current_time_ms;   //synchronize think time for correct sleep work
            return true;
        }
        return false;
    }
   

    public static String toString(Entity ent){
        return "[uid:'"+ent.uid+"' @("+
                Integer.toString(ent.origin.getX())+
                ","+
                Integer.toString(ent.origin.getY())+
                ")]";
    }

    @Override
    public String toString(){
        return toString(this);
    }

    public boolean isPlayerEnt(){
        return false;
    }

    //--------------------------------------------------------------------------
    //                      A bit of rendering shit
    //--------------------------------------------------------------------------

    protected EntityRenderer render = null;

    public EntityRenderer build_render(){
        return new DebugRenderer();
    }

    public final EntityRenderer get_render(){
        if (render == null ){
            render = build_render();
            render.set_entity(this);    //inject entity data
        }

        return render;
    }

    //--------------------------------------------------------------------------
    //                      A bit of actions shit
    //--------------------------------------------------------------------------
    public ArrayList get_action_list(){
        return new ArrayList<Entity>(0);
    }

    //--------------------------------------------------------------------------
    //  inventory
    //--------------------------------------------------------------------------
    public ItemContainer container = new ItemContainer();
}
