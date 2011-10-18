/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.gameserver;

import events.EventManager;
import game.GameEnvironment;
import game.ent.Entity;
import game.ent.EntityNPC;
import game.ent.controller.NpcController;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import ne.io.Io;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.lwjgl.util.Point;
import server.AServerIoLayer;
import server.NEDataPacket;
import server.ServerUserPool;
import server.User;
import server.world.ServerWorldModel;
import world.WorldChunk;
import world.WorldModel;
import world.layers.WorldLayer;

/**
 *
 * @author Administrator
 */
public class GameServer extends AServerIoLayer{
    NioServerSocketChannelFactory nio_factory;


    GameEnvironment gameEnv;
    EventManager eventManager;

    CacheManager cacheManager;

    public GameServer(){
        super("game-server");

        System.out.println("Starting ehCache manager");
        cacheManager = new CacheManager();
        
        System.out.println("Loading server environment...");

        eventManager = new EventManager();

        gameEnv = new GameEnvironment() {
            {
                clientWorld = new ServerWorldModel(cacheManager);
                clientWorld.setEnvironment(this);
            }

            @Override
            public EventManager getEventManager() {
                return eventManager;
            }

            @Override
            public WorldModel getWorld() {
                return clientWorld;
            }
        };

        WorldModel model = gameEnv.getWorld();
        model.setName("server-world");

    }

    public CacheManager getCacheManager(){
        return cacheManager;
    }

    public void run(){
        System.out.println("Starting local game server on "+Io.GAME_SERVER_PORT);
        
        handler = new GameServerHandler(this);

        nio_factory = new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool());

        bootstrap = new ServerBootstrap(
            nio_factory
        );

        // Set up the pipeline factory.

        //this shit not works yet
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                pipeline.addLast("handler", handler);

                return pipeline;
            }
        });

        // Bind and start to accept incoming connections.
        Channel srvChannel = bootstrap.bind(new InetSocketAddress(Io.GAME_SERVER_PORT));
        allChannels.add(srvChannel);
    }



    public GameEnvironment getEnv() {
        return gameEnv;
    }

    @Override
    public void destroy(){
        super.destroy();
        
        Cache cache = cacheManager.getCache("chunkCache");
        System.out.println("Saving chunk data");
        for (Point key: gameEnv.getWorldLayer(0).chunk_data.keySet()){
            WorldChunk chunk = gameEnv.getWorldLayer(0).chunk_data.get(key);
            Element element = new Element(key, chunk);

            cache.put(element);
        }


        System.out.println("Flushing down cache");
        cache.flush();

        System.out.println("Shutting down ehCache manager");
        cacheManager.shutdown();
    }

    @Override
    public void update() {
        super.update();
        
        gameEnv.getWorld().update();
        gameEnv.getEntityManager().update();
    }

    @Override
    protected void handlePacket(NEDataPacket packet) {
        String[] data = packet.getData();
        Channel ioChannel = packet.getChannel();

        //throw new UnsupportedOperationException("Not yet implemented");
        if (data.length == 0){
            return;
        }
        String eventType = data[0];

        System.err.println("handling event '"+eventType+"'");

        if (eventType.equals("events.network.EEntitySetPath")){
            int x = Integer.parseInt(data[1]);
            int y = Integer.parseInt(data[2]);

            User user = ServerUserPool.getUser(ioChannel);
            moveUser(user, x, y);
        }
    }

    void registerUser(User user) {
        //do nothing?
    }


    private void moveUser(User user, int x, int y) {
        Entity ent = user.getEntity();

        if(ent == null){
            throw new RuntimeException("trying to move NULL user entity");
        }

        Point destCoord = new Point(x,y);

        ((NpcController) ent.controller).set_destination(destCoord);

        //ent.move_to(new Point(x, y));
        Point chunkCoord = WorldChunk.get_chunk_coord(destCoord);
        worldUpdateLazyLoad(chunkCoord.getX(),chunkCoord.getY());
    }

     /**
     * Spawns player character binded to the connection channel
     */
    public void spawnPlayerCharacter(User user) {

        //This shit loads resources for whatever reason.
        //TODO: solve this

        GameEnvironment env = getEnv();

        Entity mplayer_ent = new EntityNPC();
        mplayer_ent.setEnvironment(env);
        mplayer_ent.set_controller(new NpcController(env));

        //TODO: load layer
        mplayer_ent.setLayerId(0);
        mplayer_ent.setEnvironment(env);

        //EntityManager.add(mplayer_ent);       ?
        mplayer_ent.spawn(user.getId(), new Point(10,10));

        user.setEntity(mplayer_ent);
        worldUpdateLazyLoad(0,0);
    }



    /**
     * Preloads 3x3 chunk cluster so pathfinding could work correctly near the chunk border
     */
    private void worldUpdateLazyLoad(int x, int y){
        WorldLayer serverGroundLayer = getEnv().getWorldLayer(WorldLayer.GROUND_LAYER);
        for (int i = x-1; i<= x+1; i++){
            for (int j = y-1; j<= y+1; j++){
                serverGroundLayer.get_cached_chunk(i, j);
            }
        }
    }
}
