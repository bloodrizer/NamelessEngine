/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import de.matthiasmann.twl.Widget;
import events.EPlayerLogon;
import events.Event;
import events.EventManager;
import game.ent.Entity;
import game.ent.EntityManager;
import org.lwjgl.util.Point;
import player.Player;
import render.TilesetRender;
import ui.GameUI;
import world.WorldModel;

import events.IEventListener;
import world.WorldView;

/**
 *
 * @author Administrator
 */
public class ModeDefault implements IGameMode, IEventListener {

    //private Tileset tileset = null;

    private TilesetRender bg_tileset;
    private WorldView view;
    
    public ModeDefault(){      
        EventManager.subscribe(this);
    }

    public void run(){
        bg_tileset = new TilesetRender();
        view = new WorldView();

        //synchronize with server
        //init world

        //debug only
        EPlayerLogon event = new EPlayerLogon(new Point(5,5));
        event.post();
    }

    void spawn_player(Point location){
        Entity player_ent = new Entity();
        EntityManager.add(player_ent);
        player_ent.spawn(12345, location);
        Player.set_ent(player_ent);
    }




    public void update(){
        view.render_background();
        //view.synchronize();
    }

    public Widget get_ui(){
        return new GameUI();
    }

    //--------------------------------------------------------------------------
    public void e_on_event(Event event){
       if (event.classname().equals("events.EPlayerLogon")){
           spawn_player(((EPlayerLogon)event).origin);
       }
       else
       {
           System.out.println("Unknown message registered:" + event.classname());   //debug
       }
    }

    public void e_on_event_rollback(Event event){
        
    }

    private Object EPlayerLogon(Event event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
