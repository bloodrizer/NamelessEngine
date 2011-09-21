/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * @author bloodrizer
 */

/**
 * Handles basic character authorization

 *
 */
public class CharServer {
    public void run(){
        ServerBootstrap bootstrap = new ServerBootstrap(
        new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool()));

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new CharServerHandler());
            }
        });

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(8022));
    }
}
