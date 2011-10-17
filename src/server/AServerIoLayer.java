/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.util.ArrayList;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;

/**
 *
 * @author Administrator
 */
public abstract class AServerIoLayer {
    public ChannelGroup allChannels;
    String name;
    ArrayList<NEDataPacket> packets;
    
    protected AServerHandler handler;


    protected ServerBootstrap bootstrap;

    public AServerIoLayer(String name){
        this.name = name;
        allChannels = new DefaultChannelGroup(name);
        packets = new ArrayList<NEDataPacket>();
    }


    public void destroy(){
        System.out.println("stoping server '"+name+"'");

        ChannelGroupFuture future = allChannels.close();
        future.awaitUninterruptibly();

        bootstrap.releaseExternalResources();
    }

    public void registerPacket(NEDataPacket packet) {
        //System.out.println(name + ":registering packet");
        packets.add(packet);
    }
    
    public void update(){
        //System.out.println(name+":updating");

        for (NEDataPacket packet: packets){
            //System.out.println(name+":handling packet #");
            handlePacket(packet);
        }
        packets.clear();
    }

    protected abstract void handlePacket(NEDataPacket packet);
}
