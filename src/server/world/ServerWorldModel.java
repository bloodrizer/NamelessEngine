/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.world;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.lwjgl.util.Point;
import world.WorldModel;
import world.layers.WorldLayer;


/**
 *
 * @author Administrator
 *
 *
 * Server's WorldModel. Can save and load in-game data
 *
 */

public class ServerWorldModel extends WorldModel{

    public ServerWorldModel(CacheManager cacheManager){

        System.out.println("Creating chunk cache...");

        Cache persistantCache = new Cache(
              new CacheConfiguration("chunkCache", 1024)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
                .overflowToDisk(true)
                .eternal(false)
                .timeToLiveSeconds(60)
                .timeToIdleSeconds(30)
                .diskPersistent(true)
                .diskExpiryThreadIntervalSeconds(120)); //wtf is this?


        cacheManager.addCache(persistantCache);

        //do some sql lite initialization there
        for (int i = 0; i< LAYER_COUNT; i++ ){
            WorldLayer layer = new ServerWorldLayer();
            ((ServerWorldLayer)layer).setCacheManager(cacheManager);
            ((ServerWorldLayer)layer).setModel(ServerWorldModel.this);

            layer.set_zindex(i);
            worldLayers.put(i, layer);
        }
    }
}

//TODO: to be investigated - ehcache, Berkeley DB, DirectMemory/Redis