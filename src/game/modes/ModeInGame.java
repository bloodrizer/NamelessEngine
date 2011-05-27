/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

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
import items.BaseItem;
import ne.Input;
import ne.Input.MouseInputType;
import ne.io.Io;
import ne.ui.NE_GUI_System;
import org.lwjgl.opengl.GL11;
import render.overlay.OverlaySystem;
import ui.IUserInterface;
import world.Timer;
import world.WorldView;
import world.WorldViewCamera;

/**
 *
 * @author Administrator
 */
public class ModeInGame implements IGameMode, IEventListener {

    //private Tileset tileset = null;

    private Tileset bg_tileset;
    private WorldView  view;
    private WorldModel model;
    private NE_GUI_System gui;


    private OverlaySystem overlay;
    
    public ModeInGame(){
        EventManager.subscribe(this);
    }

    public void run(){
        bg_tileset = new Tileset();
        view  = new WorldView();
        model = new WorldModel();

        overlay = new OverlaySystem();

        //gui = new NE_GUI_System();

        Timer.init();   //very-very critical

        //synchronize with server
        //init world

        
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

        Io.update();

        Input.update();
        EventManager.update();
        model.update();


        view.render();

        get_ui().get_nge_ui().render();


        overlay.render();

    }

    IUserInterface ui = null;
    public IUserInterface get_ui(){
        //return new GameUI();
        if (ui == null){
            ui = new GameUI();
        }
        return ui;
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
