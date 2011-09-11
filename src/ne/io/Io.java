/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import events.EPlayerAuthorise;
import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.EChatMessage;
import events.network.EPlayerLogon;
import events.network.ESelectCharacter;
import game.ent.buildings.BuildManager;
import game.ent.Entity;
import game.ent.EntityManager;
import game.ent.EntityNPC;
import game.ent.buildings.EntBuilding;
import game.ent.controller.NpcController;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Properties;
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
    static IoLayer chatserver_io;
    public static int PROTO_VER = 1050;
    public static String CLIENT_VER = "1.0.5";

    public static void init(){
        INSTANCE = new Io();
    }

    public static void update() throws IOException {
        if(charserv_io!=null){
            charserv_io.update();   //update network data
        }
        if(gameserv_io!=null){
            gameserv_io.update();
        }
        if(chatserver_io!=null){
            chatserver_io.update();
        }
    }

    public Io(){
        EventManager.subscribe(this);
    }

    public static void reset(){
        charserv_io = null;
        gameserv_io = null;
        chatserver_io = null;
    }
    

    public static void connect() throws SocketTimeoutException, IOException{
        if(charserv_io!=null){
            return;
        }

        System.out.println("starting charserv io");

        Properties p = new Properties();
        String server_url = null;
        try {
            p.load(new FileInputStream("client.ini"));
            server_url = p.getProperty("server_url");
        }
        catch (IOException ex) {
            //that is not a real problem, you know
            server_url=":";
        }

        System.out.println("server url:"+server_url);
        String[] server_params = server_url.split(":");

        if(server_params.length != 2){
            server_params = new String[] {"admin.edi.inteliec.eu","8022"};
        }

        charserv_io = new IoLayer(server_params[0], Integer.parseInt(server_params[1])){
            {
                 String[] whitelist = {
                    "0x0026",
                    "0x0013"

                 };
                 set_whitelist(whitelist);
            }

           @Override
           protected void parse_network_data(String[] data) throws IOException{
 
                if (data[0].equals("0x0027")){

                    Main.game.set_state(Game.GameModes.InGame);

                    Player.character_id = Integer.parseInt(data[2]);
                    //Player.get_ent().set_uid(Player.character_id);
                    
                    /*
                     *  Our connection is accepted by char server,
                     *  start game and connect to game server
                     */

                    chatserver_io = new IoLayer(
                        data[5],
                        Integer.parseInt(data[6])
                    ){

                        {

                            String[] whitelist = {
                                "0x0026"
                            };
                            set_whitelist(whitelist);
                        }

                        @Override
                        protected void parse_network_data(String[] data){
                        }
                    };

                    gameserv_io = new GameServerLayer(
                        data[3],
                        Integer.parseInt(data[4])
                    );



                    gameserv_io.sock_send("0x0050 "+Player.character_id);
                }
                if (data[0].equals("0x0012")){      //player loged in
                    /*ESelectCharacter event = new ESelectCharacter();
                    event.post();*/
                    EPlayerAuthorise event = new EPlayerAuthorise();
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
