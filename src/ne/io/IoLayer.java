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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    int port;

    //String[] whitelist_array = {};
    ArrayList<String> event_whitelist = new ArrayList<String>(64);

    public void set_whitelist(String[] whitelist_array){
        event_whitelist = new ArrayList(Arrays.asList(whitelist_array));
    }

    public IoLayer(String host, int port) throws IOException{

        this.host = host;
        this.port = port;
        
        //System.out.println("creating socket and i/o buffers");

            SocketAddress sockaddr = new InetSocketAddress(host, port);
            layer_sock = new Socket();
            layer_sock.connect(sockaddr, 5000); //5ms
            //layer_sock.setSoTimeout(5000);

            out = new PrintWriter(layer_sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        layer_sock.getInputStream()));
        
        /*catch(IOException e){
e.printStackTrace();
Logger.getLogger(IoLayer.class.getName()).log(Level.SEVERE, null, e);
}*/

        /*
catch(java.net.SocketTimeoutException e){
Io.reset();
e.printStackTrace(); <<connection timed out there
return;
}*/
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


    private boolean whitelisted(String packet_id) {
        if (!this.event_whitelist.isEmpty()){
            return this.event_whitelist.contains(packet_id);
        }
        return true;
    }

    private void send_network_event(NetworkEvent event){

        if (!whitelisted(event.get_id())){
            return;
        }

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
                //System.exit(0);
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
        if (out!=null){
            System.err.println(host+":"+port+" >>'"+msg+"'");
            out.println(msg);
        }
    }

    public void update() throws IOException{
        Object[] packets_arr = packets.toArray();
        for(int i = 0; i<packets_arr.length; i++){
            //System.out.println("handling packet");
            String[] data = (String[])packets_arr[i];
            parse_network_data(data);
        }

        packets.clear(); //must be cleared, or VERY FUNNY effect occurs
    }

    protected void parse_network_data(String[] data) throws IOException{
        System.out.println("io_layer::parse_network_data");
        //OVERRIDE ME!!!!
    }

    //--------------------------------------------------------------------------
    // Event Listener implementation
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