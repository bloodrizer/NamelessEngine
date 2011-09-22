/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import events.*;
import java.net.InetSocketAddress;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 *
 * @author bloodrizer
 */
class CharClientHandler extends SimpleChannelHandler {
        
    final ClientBootstrap bootstrap;

    public CharClientHandler(ClientBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) bootstrap.getOption("remoteAddress");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String request = (String) e.getMessage();
        System.err.println("char client: recieved msg: ["+request+"]");
        
        String[] packet = null;
        if (request != null){
            packet = request.split(" ");
            handlePacket(packet, ctx.getChannel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

     @Override
     public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        System.out.println("Connected to: " + getRemoteAddress());
     }
     
     //-------------------------------------------------------------------------
     
     private void sendMsg(String msg, Channel ioChannel){
        ioChannel.write(msg+"\r\n");
     }
     
     private void handlePacket(String[] packet, Channel ioChannel) {
        //throw new UnsupportedOperationException("Not yet implemented");
        if (packet.length == 0){
            return;
        }
        String eventType = packet[0];
        
        if (eventType.equals("EPlayerAuthorize")){
            //Initialization problem there
            
            EPlayerAuthorise event = new EPlayerAuthorise();
            event.post();
        }
     }
     

}
