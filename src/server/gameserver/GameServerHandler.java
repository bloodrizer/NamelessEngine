/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.gameserver;

import events.network.NetworkEvent;
import game.GameEnvironment;
import game.ent.Entity;
import game.ent.EntityNPC;
import game.ent.controller.NpcController;
import java.util.concurrent.atomic.AtomicLong;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.lwjgl.util.Point;
import player.Player;
import server.AServerHandler;
import server.AServerIoLayer;
import server.NEDataPacket;
import server.ServerUserPool;
import server.User;
import world.WorldChunk;
import world.WorldModel;
import world.layers.WorldLayer;


/**
 *
 * @author Administrator
 */
public class GameServerHandler extends AServerHandler {

    private final AtomicLong transferredBytes = new AtomicLong();
    AServerIoLayer server;

    public GameServer getServer(){
        return (GameServer)server;
    }

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

        spawnPlayerCharacter(user);

        //WorldModel model = getServer().getEnv().getWorld();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

        String request = (String) e.getMessage();
        System.err.println("Netty Game server: recived ["+request+"]");

        String[] packet = null;
        if (request != null){
            packet = request.split(" ");
            server.registerPacket(new NEDataPacket(packet, ctx.getChannel()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        e.getChannel().close();

        throw new RuntimeException("Unexpected exception from downstream", e.getCause());
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
    
    /**
     * Preloads 3x3 chunk cluster so pathfinding could work correctly near the chunk border
     */
    private void worldUpdateLazyLoad(int x, int y){
        WorldLayer serverGroundLayer = getServer().getEnv().getWorldLayer(WorldLayer.GROUND_LAYER);
        for (int i = x-1; i<= x+1; i++){
            for (int j = y-1; j<= y+1; j++){
                serverGroundLayer.get_cached_chunk(i, j);
            }
        }
    }

    /**
     * Spawns player character binded to the connection channel
     *
     *
     *
     */
    private void spawnPlayerCharacter(User user) {

        //This shit loads resources for whatever reason.
        //TODO: solve this

        GameEnvironment env = getServer().getEnv();

        Entity mplayer_ent = new EntityNPC();
        mplayer_ent.setEnvironment(env);
        mplayer_ent.set_controller(new NpcController(env));

        //TODO: load layer
        mplayer_ent.setLayerId(0);
        mplayer_ent.setEnvironment(env);

        //EntityManager.add(mplayer_ent);       ?
        mplayer_ent.spawn(user.getId(), new Point(10,10));

        user.setEntity(mplayer_ent);
        worldUpdateLazyLoad(0,0);
    }

    private void moveUser(User user, int x, int y) {
        Entity ent = user.getEntity();

        if(ent == null){
            throw new RuntimeException("trying to move NULL user entity");
        }

        Point destCoord = new Point(x,y);

        ((NpcController) ent.controller).set_destination(destCoord);

        //ent.move_to(new Point(x, y));
        Point chunkCoord = WorldChunk.get_chunk_coord(destCoord);
        worldUpdateLazyLoad(chunkCoord.getX(),chunkCoord.getY());
    }

}

