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
import server.NEDataPacket;
import server.ServerUserPool;
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
        
        if (eventType.equals("EPlayerLogin")){
            /*
             * Player reqested to connect character server
             * 
             * 1. Check if he provided correct login/password
             * 
             * 2. If user login is valid, authorize him and 
             *    provide a list of player characters
             */
                        
            //register this player in the connection pool
            ServerUserPool.registerUser(ioChannel, "Red");
            
            handler.sendMsg("EPlayerAuthorize", ioChannel);
        }
        if (eventType.equals("events.network.ESelectCharacter")){
            
            /*
             * Player selected his player character.
             * 1. We should store this data in the charserver somehow
             * 2. We should provide player with host and port of the game server
             */
            String gameServerHost = "localhost";
            int gameServerPort = Io.GAME_SERVER_PORT;
            
            User user = ServerUserPool.getUser(ioChannel);
            int user_id = user.getId();
            
            handler.sendMsg("EPlayerLogon "+gameServerHost+" "+gameServerPort+" "+user_id, ioChannel);
        }
    }

    void registerUser(User user) {
        //do nothing?
    }
}
