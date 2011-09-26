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

        factory = new ChannelPipelineFactory() {

            CharServer server = CharServer.this;

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                pipeline.addLast("handler", new CharServerHandler(server));

                return pipeline;
            }
        };
        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(factory);

        // Bind and start to accept incoming connections.
        Channel srvChannel = bootstrap.bind(new InetSocketAddress(Io.CHAR_SERVER_PORT));
        allChannels.add(srvChannel);

    }

}
