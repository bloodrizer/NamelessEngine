/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.world;

import game.GameEnvironment;
import game.ent.Entity;
import java.io.Serializable;
import java.util.List;
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



    @Override
    public WorldChunk get_cached_chunk(int chunk_x, int chunk_y){
        push_point(util_point);
        util_point.setLocation(chunk_x, chunk_y);

        
        Cache cache = cacheManager.getCache("chunkCache");
        Element element = cache.get(util_point);
        
        if (element == null){
            return precache_chunk(chunk_x, chunk_y);
        }

        WorldChunk chunk = ((WorldChunk)element.getObjectValue());


        if (!chunk_data.containsKey(util_point)){
            //register chunk objects into the global entity pool
            List<Entity> entList = chunk.getEntList();
            
            for (Entity ent: entList){
                model.getEnvironment().getEntityManager().add(ent, z_index);
            }
        }


        pop_point(util_point);

        return chunk;
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

    private WorldChunk precache_chunk(int x, int y){
        WorldChunk chunk = new WorldChunk(x, y);

        Cache cache = cacheManager.getCache("chunkCache");
        Element element = new Element(new Point(x,y), chunk);
        cache.put(element);

        process_chunk(chunk.origin, z_index);

        return chunk;
    }

}
