/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import de.matthiasmann.twl.Widget;
import events.EMouseClick;
import events.network.EPlayerLogon;
import events.Event;
import events.EventManager;
import game.ent.Entity;
import game.ent.EntityManager;
import org.lwjgl.util.Point;
import player.Player;
import render.Tileset;
import ui.GameUI;
import world.WorldModel;

import events.IEventListener;
import game.ent.EntityPlayer;
import game.ent.controller.NpcController;
import ne.Input;
import ne.Input.MouseInputType;
import render.overlay.OverlaySystem;
import world.Timer;
import world.WorldView;
import world.WorldViewCamera;

/**
 *
 * @author Administrator
 */
public class ModeDefault implements IGameMode, IEventListener {

    //private Tileset tileset = null;

    private Tileset bg_tileset;
    private WorldView  view;
    private WorldModel model;


    private OverlaySystem overlay;
    
    public ModeDefault(){      
        EventManager.subscribe(this);
    }

    public void run(){
        bg_tileset = new Tileset();
        view  = new WorldView();
        model = new WorldModel();

        overlay = new OverlaySystem();

        Timer.init();   //very-very critical

        //synchronize with server
        //init world

        //debug only
        EPlayerLogon event = new EPlayerLogon(new Point(5,5));
        event.post();
    }

    void spawn_player(Point location){
        Entity player_ent = new EntityPlayer();
        EntityManager.add(player_ent);
        player_ent.spawn(12345, location);

        WorldViewCamera.target.setLocation(location);

        player_ent.set_controller(new NpcController());

        Player.set_ent(player_ent);
    }




    public void update(){
        Input.update();
        EventManager.update();
        model.update();


        view.render();
        //render text overlay stack
        overlay.render();
        //view.synchronize();
    }

    public Widget get_ui(){
        return new GameUI();
    }

    //--------------------------------------------------------------------------
    public void e_on_event(Event event){

       if (event instanceof EPlayerLogon){
           spawn_player(((EPlayerLogon)event).origin);
       }
       else if(event instanceof EMouseClick){
           e_on_mouse_click(((EMouseClick)event));
       }
       else
       {
           //System.out.println("Unknown message registered:" + event.classname());   //debug
       }
    }

    public void e_on_mouse_click( EMouseClick event){
        Point tile_origin = view.getTileCoord(event.origin);

        //System.out.println(tile_origin);
        if (event.type == MouseInputType.LCLICK) {
            Player.move(tile_origin);
        }
        //todo: use Player.player_ent.controller.set_target(tile_origin);
    }
    //--------------------------------------------------------------------------
    public void e_on_event_rollback(Event event){
        
    }

    private Object EPlayerLogon(Event event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
