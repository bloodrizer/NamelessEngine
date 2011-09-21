/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.concurrent.atomic.AtomicLong;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 *
 * @author bloodrizer
 */
public class CharServerHandler extends SimpleChannelHandler {
    
    private final AtomicLong transferredBytes = new AtomicLong();

    public long getTransferredBytes() {
        return transferredBytes.get();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        // Send back the received message to the remote peer.
        //transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
        //e.getChannel().write(e.getMessage());

        /*ChannelBuffer buf = (ChannelBuffer) e.getMessage();
        System.out.println("Netty character server: recieved reqest ["+buf.toString()+"]");*/

        String request = (String) e.getMessage();
        System.out.println("Netty character server: recieved reqest ["+request+"]");

        //System.out.println("Netty character server: recieved reqest [unknown]");
        
        //TODO: handle request there
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        e.getChannel().close();

        throw new RuntimeException("Unexpected exception from downstream", e.getCause());
    }
    
}
