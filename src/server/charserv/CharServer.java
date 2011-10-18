/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.charserv;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import ne.io.Io;
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
import server.AServerIoLayer;
import server.NEDataPacket;
import server.ServerUserPool;
import server.User;

/**
 *
 * @author bloodrizer
 */

/**
 * Handles basic character authorization

 *
 */
public class CharServer extends AServerIoLayer{
    NioServerSocketChannelFactory nio_factory;
    ArrayList<PlayerData> playerData = new ArrayList<PlayerData>();
    ChannelPipelineFactory factory;

    public CharServer(){
        super("char-server");

        //load shit there
        playerData.add( new PlayerData("Red","True") );
    }

    public void run(){
        System.out.println("Starting local character server on "+Io.CHAR_SERVER_PORT);

        nio_factory = new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool());

        bootstrap = new ServerBootstrap(
            nio_factory
        );

        handler = new CharServerHandler(this);

        factory = new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                pipeline.addLast("handler", handler);

                return pipeline;
            }
        };
        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(factory);

        // Bind and start to accept incoming connections.
        Channel srvChannel = bootstrap.bind(new InetSocketAddress(Io.CHAR_SERVER_PORT));
        allChannels.add(srvChannel);

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

}
