/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import events.Event;
import events.EventManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import ne.io.Io;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;



/**
 *
 * @author bloodrizer
 */
public class NettyClient {
    
    static final String host = "localhost";
    static final int port = Io.CHAR_SERVER_PORT;
        
    final static NettyClientLayer charServClient = new NettyClientLayer(host,port) {{
        packetFilter.add("events.network.ESelectCharacter");
    }};


    public static void connect(){
        //return;
        
        System.out.println("Connecting to the character server...");
        

        EventManager.subscribe(charServClient);


        charServClient.setPipelineFactory(new CharClientPipelineFactory(charServClient.bootstrap));

        try {
            charServClient.connect(
            );
         System.out.println("connected successfuly");
                            
         System.out.println("sending message");
         charServClient.sendMsg("EPlayerLogin Red True");    
            
            
        } catch (Exception ex) {
            Logger.getLogger(NettyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public static void gameServConnect(String host, int port){
        
        System.out.println("Starting gameServListening thread @"+host+":"+port+" (kinda)");


        Thread chrSrvThread = new Thread(new GameServConnectionThread(host, port));
        chrSrvThread.setDaemon(true);
        chrSrvThread.start();
        

    }

    static NettyClientLayer gameServClient;
    
    private static class GameServConnectionThread implements Runnable{
        
        String host;
        int port;
        
        public GameServConnectionThread(String host, int port){
            this.host = host;
            this.port = port;
        }

        public void run() {
            gameServClient = new NettyClientLayer(host,port) {{
                    //packetFilter.add("events.network.ESelectCharacter");
            }};
            EventManager.subscribe(gameServClient);


            gameServClient.setPipelineFactory(new GameClientPipelineFactory(gameServClient.bootstrap));
            try {
                gameServClient.connect();
            } catch (Exception ex) {
                Logger.getLogger(NettyClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void destroy(){
        charServClient.destroy();
        gameServClient.destroy();
    }
}
