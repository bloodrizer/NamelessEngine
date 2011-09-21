/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.logging.Level;
import java.util.logging.Logger;
import ne.io.Io;



/**
 *
 * @author bloodrizer
 */
public class NettyClient {
    public static void connect(){
        System.out.println("Connecting to the character server...");
        
        String host = "localhost";
        int port = Io.CHAR_SERVER_PORT;
        
        NettyClientLayer charServClient = new NettyClientLayer(host,port) {};
        charServClient.setPipelineFactory(new CharClientPipelineFactory(charServClient.bootstrap));

        try {
            charServClient.connect();

            System.out.println("sending message");
            charServClient.sendMsg("EPlayerLogin Red True");
            
        } catch (Exception ex) {
            Logger.getLogger(NettyClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
}
