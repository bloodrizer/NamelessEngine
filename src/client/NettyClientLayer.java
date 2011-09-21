/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 *
 * @author bloodrizer
 */
public abstract class NettyClientLayer {
    
    ClientBootstrap bootstrap;
    String host;
    int port;
    
    public NettyClientLayer(String host, int port) {
        
    
        bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        
        this.host = host;
        this.port = port;

        
    }
    
    public void connect(){
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

        // Wait until the connection attempt succeeds or fails.
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }

        //perform i/o routine

        channel.close().awaitUninterruptibly();

        bootstrap.releaseExternalResources();
    }
    
    public void setPipelineFactory(ChannelPipelineFactory factory){
        bootstrap.setPipelineFactory(factory);
    }
}
