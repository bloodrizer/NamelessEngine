/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ne;

import events.EventManager;
import game.GameEnvironment;
import junit.framework.Assert;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import world.WorldChunk;

/**
 *
 * @author bloodrizer
 */
public class TestChunkCache {
    
    CacheManager cacheManager;
    Cache persistantCache;
    GameEnvironment testEnv;
    
    
    private void initCache(){
        cacheManager = new CacheManager();
            persistantCache = new Cache(
              new CacheConfiguration("persistantCache", 1024)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
                .overflowToDisk(true)
                .eternal(false)
                .timeToLiveSeconds(0)
                .timeToIdleSeconds(0)
                .diskPersistent(true)
                .diskExpiryThreadIntervalSeconds(0)); //wtf is this?


        cacheManager.addCache(persistantCache);
    }
    
    public TestChunkCache() {
        testEnv = new GameEnvironment(){
            @Override
            public EventManager getEventManager(){
                return new EventManager();
            }
        };
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        cacheManager.shutdown();
    }
    
    @Test
    public void persistantTest(){
        initCache();
        
        WorldChunk chunk = testEnv.getWorldLayer(0).get_cached_chunk(0,0);
        Element element = new Element(1, chunk);
        persistantCache.put(element);
        
        
        persistantCache.flush();
        cacheManager.shutdown();

        initCache();
        
                
        int diskStore = persistantCache.getDiskStoreSize();
        Assert.assertEquals(diskStore, 1);

        Element persistantElem = persistantCache.get(1);
        WorldChunk persChunk = (WorldChunk)persistantElem.getValue();
        
    }

}
