/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import game.GameEnvironment;
import net.sf.ehcache.CacheManager;
import server.charserv.CharServer;
import server.gameserver.GameServer;

/**
 *
 * @author bloodrizer
 */
public class NEServerCore {
    CharServer charServer;
    GameServer gameServer;
    
    public NEServerCore(){
        charServer = new CharServer();
        gameServer = new GameServer();
    }
    
    public void run(){
        charServer.run();
        gameServer.run();
    }

    public void destroy(){
        System.out.println("stopping NE Server wrapper");
        charServer.destroy();
        gameServer.destroy();
    }

    public CacheManager getCacheManager(){
        return gameServer.getCacheManager();
    }

    public void update() {
        gameServer.update();
    }

    public GameEnvironment getEnv() {
        return gameServer.getEnv();
    }
}
