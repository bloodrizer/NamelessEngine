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
import server.AServerIoLayer;
import server.ServerUserPool;
import server.User;


/**
 *
 * @author Administrator
 */
public class GameServerHandler extends SimpleChannelHandler {

    private final AtomicLong transferredBytes = new AtomicLong();
    AServerIoLayer server;

    public GameServerHandler(AServerIoLayer server){
        this.server = server;
    }

    public long getTransferredBytes() {
        return transferredBytes.get();
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        
        Channel channel = e.getChannel();
        User user = ServerUserPool.getUser(channel);
        
        //Remote user connected to the game server
        if(user == null){
            throw new RuntimeException("Game Server: remote connection from unregistered user");
        }
        
        ((GameServer)server).registerUser(user);
        server.allChannels.add(channel);

        //load player coords and shit
        sendMsg("EPlayerSpawn 10 10", channel);
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

        if (eventType.equals("events.network.EBuildStructure")){

            final int SPAWN_OBJECT = 0;

            String entType = packet[1];
            int x = Integer.parseInt(packet[2]);
            int y = Integer.parseInt(packet[3]);

            sendMsg("EEntitySpawn "+SPAWN_OBJECT+" "+entType+" "+x+" "+y, ioChannel);
        }

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

