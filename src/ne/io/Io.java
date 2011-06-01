/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import events.EEntitySpawn;
import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.EPlayerLogon;
import events.network.ESelectCharacter;
import events.network.NetworkEvent;
import game.ent.Entity;
import game.ent.EntityManager;
import game.ent.EntityNPC;
import game.ent.EntityPlayer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import ne.Game;
import ne.Main;
import org.lwjgl.util.Point;
import player.Player;

/**
 *
 * @author Administrator
 */

public class Io implements IEventListener {

    static Io INSTANCE;
    static IoLayer charserv_io;
    static IoLayer gameserv_io;

    public static void init(){
        INSTANCE = new Io();
    }

    public static void update() {
        if(charserv_io!=null){
            //System.out.println("updating char serv layer");
            charserv_io.update();   //update network data
        }
        if(gameserv_io!=null){
            //System.out.println("updating game serv layer");
            gameserv_io.update();
        }
    }

    public Io(){
        EventManager.subscribe(this);
    }
    

    public static void connect(){
        if(charserv_io!=null){
            return;
        }

        System.out.println("starting charserv io");

        charserv_io = new IoLayer("admin.edi.inteliec.eu", 8022){

           @Override
           protected void parse_network_data(String[] data){
 
                if (data[0].equals("0x0027") && data.length == 5){

                    Main.game.set_state(Game.GameModes.InGame);

                    Player.character_id = Integer.parseInt(data[2]);
                    
                    /*
                     *  Our connection is accepted by char server,
                     *  start game and connect to game server
                     */

                    gameserv_io = new IoLayer(
                            data[3],
                            Integer.parseInt(data[4])
                    ){
                        @Override
                        protected void parse_network_data(String[] data){

                            if (data[0].equals("0x0100") && data.length == 3){      //spawn player

                                System.out.println("spawning player");

                                EPlayerLogon event = new EPlayerLogon(new Point(
                                        Integer.parseInt( data[1] ),
                                        Integer.parseInt( data[2] )
                                ));
                                event.post();
                            }

                            if (data[0].equals("0x0200")){  //EntSpawn
                                //4 123 0 3 9
                                Entity player_ent = new EntityNPC();
                                EntityManager.add(player_ent);
                                player_ent.spawn(Integer.parseInt( data[2] )    //uid
                                        , new Point(
                                      Integer.parseInt( data[4] ),
                                      Integer.parseInt( data[5] )
                                ));
                            }
                            if (data[0].equals("0x0280")){  //ENTMove
                                Entity ent = EntityManager.get_entity(Integer.parseInt( data[1]));
                                if (ent!=null){
                                    ent.move_to(new Point(
                                            Integer.parseInt( data[3] ),
                                            Integer.parseInt( data[4] )
                                    ));
                                }else{
                                    System.err.println("EntityMove::invalid id");
                                }
                            }
                        }
                    };



                    gameserv_io.sock_send("0x0050 "+Player.character_id);
                }
                if (data[0].equals("0x0012")){      //player loged in
                    ESelectCharacter event = new ESelectCharacter();
                    event.post();
                }

            }
        };
        //TODO: GUI EVENT THERE
        //System.out.println("Connected successfuly!");

    }

    public static boolean login(String login, String pass){
        
        System.out.println("Loging in");
        charserv_io.sock_send("0x0010 "+login+" "+pass);

        return false;
    }

    //--------------------------------------------------------------------------

    public void e_on_event(Event event){

    }
    
    public void e_on_event_rollback(Event event){

    }
}
