/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.gameserver;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import ne.Game;
import ne.io.Io;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * @author Administrator
 */
public class GameServer {
    Thread recv_thread;
    NioServerSocketChannelFactory nio_factory;

    public void run(){
        System.out.println("Starting local game server on "+Io.GAME_SERVER_PORT);

        nio_factory = new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(
            nio_factory
        );

        // Set up the pipeline factory.

        //this shit not works yet
        /*bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            CharServer server = CharServer.this;

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());

                pipeline.addLast("handler", new CharServerHandler(server));

                return pipeline;
            }
        });

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(Io.GAME_SERVER_PORT));

        recv_thread = new Thread(new Receiver());
        recv_thread.setDaemon(true);
        recv_thread.start();*/
    }

    private class Receiver implements Runnable{

        public void run() {
            while (Game.running) {
            }
            nio_factory.releaseExternalResources();
        }
    }
}
