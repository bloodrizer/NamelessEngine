/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;



/**
 *
 * @author bloodrizer
 */
public class NettyClient {
    public static void connect(){
        System.out.println("Connecting to the character server...");
        
        String host = "localhost";
        int port = 8022;
        
        NettyClientLayer charServIo = new NettyClientLayer(host,port) {};
        charServIo.setPipelineFactory(new CharClientPipelineFactory());
    }
}
