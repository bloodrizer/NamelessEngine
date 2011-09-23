/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import events.Event;
import events.IEventListener;
import events.network.NetworkEvent;
import java.net.InetSocketAddress;
import java.util.ArrayList;
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
public abstract class NettyClientLayer implements IEventListener {
    
    ClientBootstrap bootstrap;
    String host;
    int port;

    //the thransport channel we use to write into/read from
    Channel ioChannel;
    Thread  ioThread;

    protected ArrayList<String> packetFilter = new ArrayList<String>();
    
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

    private boolean whitelisted(String classname) {
        return packetFilter.contains(classname);
    }

    private class IOThread implements Runnable{

        public void run() {
            while (Game.running) {
            }
            ioChannel.close().awaitUninterruptibly();
            bootstrap.releaseExternalResources();
        }
    }

    private void sendNetworkEvent(NetworkEvent event){
        if (!whitelisted(event.classname())){
            //return;
        }

        String[] tokens = event.serialize();
        StringBuilder sb = new StringBuilder();

        sb.append(event.classname().concat(" "));

        for (int i = 1; i<tokens.length; i++){
            sb.append(tokens[i].concat(" "));
        }

        sendMsg(sb.toString());
    }


    public void e_on_event(Event event) {
        if (event instanceof NetworkEvent){
            sendNetworkEvent((NetworkEvent)event);
        }
    }



    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
