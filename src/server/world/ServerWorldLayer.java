/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.world;

import game.GameEnvironment;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.lwjgl.util.Point;
import world.WorldChunk;
import world.WorldModel;
import world.layers.WorldLayer;

/**
 *
 * @author Administrator
 */
public class ServerWorldLayer extends WorldLayer {

    CacheManager cacheManager = null;
    private WorldModel model;


    @Override
    public WorldChunk get_cached_chunk(int chunk_x, int chunk_y){
        push_point(util_point);
        util_point.setLocation(chunk_x, chunk_y);

        
        Cache cache = cacheManager.getCache("chunkCache");
        Element element = cache.get(util_point);
        Object objChunk = element.getObjectValue();


        if (chunk_data.containsKey(util_point)){
            
        }


        pop_point(util_point);

        return (WorldChunk)objChunk;
    }

    @Override
    protected void build_chunk(Point origin, int z_index) {
        //TODO: implement server-side chunk generation
        //TODO: implement server-side chunk generation
        //2. If not, perform generation cycle on it and create necesery entities
        //2. If not, perform generation cycle on it and create necesery entities
        //3. Then load entities list from database
    }

    @Override
    public synchronized void chunk_gc() {
        //check if this chunk was not used for a long amt of time,
        //far from any player,  or we running low on memory
    }

    public void unloadChunk() {
        //save chunk data (region standings or smth. like this)
        //1. save chunk entities
        //2. unload them
        //3. unload the chunk
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setModel(WorldModel model){
        this.model = model;
    }
}
