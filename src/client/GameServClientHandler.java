/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 *
 * @author bloodrizer
 */
public class GameServClientHandler extends SimpleChannelHandler {
    
    final ClientBootstrap bootstrap;

    public GameServClientHandler(ClientBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

}
