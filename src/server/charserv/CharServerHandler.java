/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.charserv;

import events.network.NetworkEvent;
import java.util.concurrent.atomic.AtomicLong;
import ne.io.Io;
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
 * @author bloodrizer
 */
public class CharServerHandler extends SimpleChannelHandler {
    
    private final AtomicLong transferredBytes = new AtomicLong();
    AServerIoLayer server;

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        server.allChannels.add(e.getChannel());
    }

    public CharServerHandler(AServerIoLayer server){
        this.server = server;
    }

    public long getTransferredBytes() {
        return transferredBytes.get();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

        String request = (String) e.getMessage();
        System.err.println("Netty Character server: recived ["+request+"]");
        
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
            
            sendMsg("EPlayerAuthorize", ioChannel);
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
            
            sendMsg("EPlayerLogon "+gameServerHost+" "+gameServerPort+" "+user_id, ioChannel);
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
