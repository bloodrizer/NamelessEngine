/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import client.ClientEventManager;
import client.ClientGameEnvironment;
import events.EMouseClick;
import events.network.EPlayerLogon;
import events.Event;
import events.EventManager;
import game.ent.Entity;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;
import player.Player;
import render.TilesetRenderer;
import ui.GameUI;

import events.IEventListener;
import game.GameEnvironment;
import game.ent.EntityPlayer;
import game.ent.controller.PlayerController;
import game.ent.monsters.EntMonster;
import ne.Input;
import ne.Input.MouseInputType;
import ne.Main;
import ne.effects.EffectsSystem;
import ne.io.Io;
import ne.ui.NE_GUI_System;
import render.overlay.DebugOverlay;
import render.overlay.OverlaySystem;
import ui.IUserInterface;
import world.Timer;
import world.WorldModel;
import world.WorldTile;
import world.WorldView;
import world.WorldViewCamera;

/**
 *
 * @author Administrator
 */
public class ModeInGame implements IGameMode, IEventListener {

    //private Tileset tileset = null;

    private TilesetRenderer bg_tileset;
    private WorldView  view;
    private WorldModel model;
    private NE_GUI_System gui;
    private EffectsSystem fx;


    private GameEnvironment clientGameEnvironment;


    private OverlaySystem overlay;
    
    public ModeInGame(){
        ClientEventManager.eventManager.subscribe(this);
    }

    public void run(){
        bg_tileset = new TilesetRenderer();
        view  = new WorldView();
        //model = new WorldModel();

        clientGameEnvironment = new GameEnvironment(){
            @Override
            public EventManager getEventManager(){
                return ClientEventManager.eventManager;
            }
        };
        ClientGameEnvironment.setEnvironment(clientGameEnvironment);

        model = clientGameEnvironment.getWorld();
                //ClientWorld.getWorld();

        overlay = new OverlaySystem();

        fx = new EffectsSystem();

        //gui = new NE_GUI_System();

        Timer.init();   //very-very critical

        //synchronize with server
        //init world

        
    }

    void spawn_player(EPlayerLogon event){
        
        Point location = event.origin;
        
        Entity player_ent = new EntityPlayer();
        player_ent.setName(Player.characterInfo.name);
        
        player_ent.setEnvironment(clientGameEnvironment);
        //player_ent.setName(event.charInfo.name);
        
        //TODO: extract player information from the event
        
        clientGameEnvironment.getEntityManager().add(player_ent, Player.get_zindex());
        player_ent.spawn(12345, location);

        WorldViewCamera.target.setLocation(location);

        player_ent.set_controller(new PlayerController());
        //player_ent.set_combat(new BasicCombat());

        Player.set_ent(player_ent);
    }




    public void update(){
        try {
            Io.update();
        } catch (IOException ex) {
            Logger.getLogger(ModeInGame.class.getName()).log(Level.SEVERE, null, ex);
        }

        Input.update();

        ClientEventManager.update();

        EventManager.update();
        model.update();

        fx.update();

        
        view.render();
        fx.render();
        
        
        get_ui().update();
        get_ui().render();


        //----------show pathfinding route for every entity (debug)------------

        //OverlaySystem.drawLine(10, 10, 250, 250, Color.red);
        DebugOverlay.debugPathfinding();

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
           spawn_player(((EPlayerLogon)event));
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
            WorldTile tile = ClientGameEnvironment.getWorldLayer(Player.get_zindex()).get_tile(tile_origin.getX(), tile_origin.getY());
            
            if(tile == null){
                return;
            }

            Entity ent = tile.get_actor();
            if (ent != null && ent instanceof EntMonster){
                Player.attack(ent);
            }else{
                Player.move(tile_origin);
            }
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
