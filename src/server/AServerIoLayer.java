/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import org.jboss.netty.bootstrap.ServerBootstrap;
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

    protected ServerBootstrap bootstrap;

    public AServerIoLayer(String name){
        this.name = name;
        allChannels = new DefaultChannelGroup(name);
    }


    public void destroy(){
        System.out.println("stoping server '"+name+"'");

        ChannelGroupFuture future = allChannels.close();
        future.awaitUninterruptibly();

        bootstrap.releaseExternalResources();
    }
}
