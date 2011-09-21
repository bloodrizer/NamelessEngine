/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 *
 * @author bloodrizer
 */
public class CharClientPipelineFactory implements ChannelPipelineFactory {

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        
        pipeline.addLast("handler", new CharClientHandler());
        return pipeline;
    }
    
}
