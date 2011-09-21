/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import ne.Game;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 *
 * @author bloodrizer
 */


/*
 * Basic class for every client to server connection, e.g. : charserv, mapserv, chatserv, etc.
 *
 */
public abstract class NettyClientLayer {
    
    ClientBootstrap bootstrap;
    String host;
    int port;

    //the thransport channel we use to write into/read from
    Channel ioChannel;
    Thread  ioThread;
    
    public NettyClientLayer(String host, int port) {
        
    
        bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
        
        this.host = host;
        this.port = port;

        
    }
    
    public void connect() throws Exception{
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

        // Wait until the connection attempt succeeds or fails.
        ioChannel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            //future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();

            throw new Exception("Failed to get IO Channel on "+host+":"+port, future.getCause());
            //return;
        }
        System.out.println("connected successfuly");

        //perform i/o routine

        ioThread = new Thread(new IOThread());
        ioThread.setDaemon(true);
        ioThread.start();

    }
    
    public void setPipelineFactory(ChannelPipelineFactory factory){
        bootstrap.setPipelineFactory(factory);
    }

    public void sendMsg(String message){
        if (ioChannel==null){
            //System.err.println(host+":"+port+"> Unable to send message, channel is not ready");
            //return;

            throw new RuntimeException(host+":"+port+"> Unable to send message, channel is not ready");
        }
        ioChannel.write(message + "\r\n");
    }

    private class IOThread implements Runnable{

        public void run() {
            while (Game.running) {
            }
            ioChannel.close().awaitUninterruptibly();
            bootstrap.releaseExternalResources();
        }
    }
}
