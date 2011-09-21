/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author bloodrizer
 */
public class NEServer {
    CharServer charServer;
    
    public NEServer(){
        charServer = new CharServer();
    }
    
    public void run(){
        charServer.run();
    }
}
