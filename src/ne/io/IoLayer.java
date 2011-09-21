/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.NetworkEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import ne.Game;

/*
 * Io Layer is a socket wrapper with ability to pack network events and send them to the server
 * NOTE that game network architecture allows several io layers to be run in background threads
 */
public class IoLayer extends AbstractIoLayer implements IEventListener{
    private Socket layer_sock = null;

    String host;
    protected int port;


    public IoLayer(String host, int port) throws IOException{

        this.host = host;
        this.port = port;

            SocketAddress sockaddr = new InetSocketAddress(host, port);
            layer_sock = new Socket();
            layer_sock.connect(sockaddr, 5000); //5ms
            //layer_sock.setSoTimeout(5000);

            out = new PrintWriter(layer_sock.getOutputStream(), true);
            in =  new BufferedReader(new InputStreamReader(
                                        layer_sock.getInputStream()));

        EventManager.subscribe(this);

        recv_thread = new Thread(new Receiver());

        //System.out.println("Starting listening thread");
        
        recv_thread.setDaemon(true);
        recv_thread.start();

        System.out.println("Successfuly created io layer '"+host+"':"+port);
    }

    //method is synchronised to prevent double closing
    public synchronized void close() {
        if (!layer_sock.isClosed()) {
            try {
                layer_sock.close();
                //System.exit(0);
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }

    private class Receiver implements Runnable{

        public void run() {
            while (!layer_sock.isClosed() && Game.running && in!=null) {
                String line = null;
                try {
                    line = in.readLine();

                    if (line != null){
                        packets.add(line.split(" "));
                    }

                } catch (IOException e) {
                    if ("Socket closed".equals(e.getMessage())) {
                        break;
                    }
                    System.out.println("Connection lost");
                    close();
                }
                if (line == null) {
                    System.out.println("Server has closed connection");
                    close();
                } else {
                    System.out.println(host + ":" + line);
                }
            }
        }
    }

    /*
     * Send raw message to socket
     * Not recommended.
     * Use serialized NetworkEvent messages instead
     */

    @Override
    public void sock_send(String msg){
        System.err.println(host+":"+port+" >>'"+msg+"'");
        super.sock_send(msg);
    }

    //--------------------------------------------------------------------------
    //                      Event Listener implementation
    //--------------------------------------------------------------------------

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void e_on_event(Event event) {
        if(!event.is_local()&&!event.is_dispatched()){
            NetworkEvent net_event = (NetworkEvent)event;

            send_network_event(net_event);
        }
    }
}
