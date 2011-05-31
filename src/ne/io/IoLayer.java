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
import java.util.Collection;
import ne.Game;

/*
 * Io Layer is a socket wrapper with ability to pack network events and send them to the server
 * NOTE that game network architecture allows several io layers to be run in background threads
 */
public class IoLayer implements IEventListener{
    private Socket layer_sock = null;

    PrintWriter out = null;
    BufferedReader in = null;

    Thread recv_thread;

    String host;

    public IoLayer(String host, int port){

        this.host = host;
        
        //System.out.println("creating socket and i/o buffers");
        try {
            layer_sock = new Socket(host, port);

            out = new PrintWriter(layer_sock.getOutputStream(), true);
            in =  new BufferedReader(new InputStreamReader(
                                        layer_sock.getInputStream()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        //System.out.println("Subscribing");
        EventManager.subscribe(this);

        

        recv_thread = new Thread(new Receiver());

        //System.out.println("Starting listening thread");
        
        recv_thread.setDaemon(true);
        recv_thread.start();

        System.out.println("Successfuly created io layer '"+host+"':"+port);

    }

    Collection<String[]> packets = new ArrayList<String[]>(100);

    private class Receiver implements Runnable{

        public void run() {
            while (!layer_sock.isClosed() && Game.running ) {
                String line = null;
                try {
                    line = in.readLine();
                    //parse_network_data(line.split(" "));
                    packets.add(line.split(" "));

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

    private void send_network_event(NetworkEvent event){
        String[] tokens = event.serialize();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<tokens.length; i++){
            sb.append(tokens[i].concat(" "));
        }

        sock_send(sb.toString());
    }

    //method is synchronised to prevent double closing
    public synchronized void close() {
        if (!layer_sock.isClosed()) {
            try {
                layer_sock.close();
                System.exit(0);
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }

    /*
     * Send raw message to socket
     * Not recommended.
     * Use serialized NetworkEvent messages instead
     */
    public void sock_send(String msg){
        System.err.println("Sending message '"+msg+"'");
        out.println(msg);
    }

    public void update(){
        Object[] packets_arr =  packets.toArray();
        for(int i = 0; i<packets_arr.length; i++){
            //System.out.println("handling packet");
            String[] data = (String[])packets_arr[i];
            parse_network_data(data);
        }

        packets.clear();    //must be cleared, or VERY FUNNY effect occurs
    }

    protected void parse_network_data(String[] data){
        System.out.println("io_layer::parse_network_data");
        //OVERRIDE ME!!!!
    }

    //--------------------------------------------------------------------------
    //                      Event Listener implementation
    //--------------------------------------------------------------------------

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void e_on_event(Event event) {
        //TODO: check if server can accept network event >3
        if(!event.is_local()&&!event.is_dispatched()){
            NetworkEvent net_event = (NetworkEvent)event;

            send_network_event(net_event);
        }
    }
}
