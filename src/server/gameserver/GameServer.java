/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.gameserver;

import events.EventManager;
import game.GameEnvironment;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import ne.Game;
import ne.io.Io;
import net.sf.ehcache.CacheManager;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import server.AServerIoLayer;
import server.User;
import server.world.ServerWorldModel;
import world.WorldModel;

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

        nio_factory = new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool());

        bootstrap = new ServerBootstrap(
            nio_factory
        );

        // Set up the pipeline factory.

        //this shit not works yet
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            GameServer server = GameServer.this;

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                pipeline.addLast("handler", new GameServerHandler(server));

                return pipeline;
            }
        });

        // Bind and start to accept incoming connections.
        Channel srvChannel = bootstrap.bind(new InetSocketAddress(Io.GAME_SERVER_PORT));
        allChannels.add(srvChannel);
    }

    
    /*
     * Add new user into the WorldModel 
     * and allow him to interact with the game world
     */
    void registerUser(User user) {
        
    }

    GameEnvironment getEnv() {
        return gameEnv;
    }

    @Override
    public void destroy(){
        super.destroy();

        System.out.println("Shutting down ehCache manager");
        cacheManager.shutdown();
    }

    public void update() {
        gameEnv.getWorld().update();
        gameEnv.getEntityManager().update();
    }
}
