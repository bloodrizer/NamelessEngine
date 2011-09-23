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
    public static void connect(){
        System.out.println("Connecting to the character server...");
        
        String host = "localhost";
        int port = Io.CHAR_SERVER_PORT;
        
        final NettyClientLayer charServClient = new NettyClientLayer(host,port) {{
            packetFilter.add("events.network.ESelectCharacter");
        }};
        EventManager.subscribe(charServClient);


        charServClient.setPipelineFactory(new CharClientPipelineFactory(charServClient.bootstrap));

        try {
            charServClient.connect(
                    /*new ChannelFutureListener() {
                        public void operationComplete(ChannelFuture future) {
                           
                        }
                    }*/
            );
         System.out.println("connected successfuly");
                            
         System.out.println("sending message");
         charServClient.sendMsg("EPlayerLogin Red True");    
            
            
        } catch (Exception ex) {
            Logger.getLogger(NettyClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    public static void charServConnect(String host, int port){
        Thread chrSrvThread = new Thread(new СharServConnectionThread(host, port));
        chrSrvThread.setDaemon(true);
        chrSrvThread.start();
    }
    
    private static class СharServConnectionThread implements Runnable{
        
        String host;
        int port;
        
        public СharServConnectionThread(String host, int port){
            this.host = host;
            this.port = port;
        }

        public void run() {
            NettyClientLayer gameServClient = new NettyClientLayer(host,port) {{
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
}
