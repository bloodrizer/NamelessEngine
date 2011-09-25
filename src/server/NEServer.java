/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import server.charserv.CharServer;
import server.gameserver.GameServer;

/**
 *
 * @author bloodrizer
 */
public class NEServer {
    CharServer charServer;
    GameServer gameServer;
    
    public NEServer(){
        charServer = new CharServer();
        gameServer = new GameServer();
    }
    
    public void run(){
        charServer.run();
        gameServer.run();
    }

    public void destroy(){
        charServer.destroy();

        System.out.println("stoping game server...");
        gameServer.destroy();
    }
}
