/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.gameserver;

import events.network.NetworkEvent;
import java.util.concurrent.atomic.AtomicLong;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;


/**
 *
 * @author Administrator
 */
public class GameServerHandler extends SimpleChannelHandler {

    private final AtomicLong transferredBytes = new AtomicLong();
    GameServer server;

    public GameServerHandler(GameServer server){
        this.server = server;
    }

    public long getTransferredBytes() {
        return transferredBytes.get();
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        server.allChannels.add(e.getChannel());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

        String request = (String) e.getMessage();
        System.err.println("Netty Game server: recived ["+request+"]");

        String[] packet = null;
        if (request != null){
            packet = request.split(" ");
            handlePacket(packet, ctx.getChannel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        e.getChannel().close();

        throw new RuntimeException("Unexpected exception from downstream", e.getCause());
    }

    private void handlePacket(String[] packet, Channel ioChannel) {
        //throw new UnsupportedOperationException("Not yet implemented");
        if (packet.length == 0){
            return;
        }
        String eventType = packet[0];

    }

    private void sendMsg(String msg, Channel ioChannel){
        ioChannel.write(msg+"\r\n");
    }

    private void sendNetworkEvent(NetworkEvent event){
        //if (!whitelisted(event.get_id())){
            //return;
        //}

        String[] tokens = event.serialize();
        StringBuilder sb = new StringBuilder();

        sb.append(event.classname().concat(" "));

        for (int i = 1; i<tokens.length; i++){
            sb.append(tokens[i].concat(" "));
        }

        //sock_send(sb.toString());
    }

}

