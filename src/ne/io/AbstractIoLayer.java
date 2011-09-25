/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import events.network.NetworkEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import ne.Game;

/**
 *
 * @author Administrator
 */
public abstract class AbstractIoLayer {

    protected PrintWriter out = null;
    protected BufferedReader in = null;

    Thread recv_thread;

    ArrayList<String> event_whitelist = new ArrayList<String>(64);

    public void set_whitelist(String[] whitelist_array){
        event_whitelist = new ArrayList(Arrays.asList(whitelist_array));
    }

    Collection<String[]> packets = new ArrayList<String[]>(100);




    private boolean whitelisted(String packet_id) {
        if (!this.event_whitelist.isEmpty()){
            return this.event_whitelist.contains(packet_id);
        }
        return true;
    }

    protected void send_network_event(NetworkEvent event){

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

    public void sock_send(String msg){
        if (out!=null){
            out.println(msg);
        }
    }


    public void update() throws IOException{
        Object[] packets_arr =  packets.toArray();
        for(int i = 0; i<packets_arr.length; i++){
            //System.out.println("handling packet");
            String[] data = (String[])packets_arr[i];
            parse_network_data(data);
        }

        packets.clear();    //must be cleared, or VERY FUNNY effect occurs
    }

    protected void parse_network_data(String[] data) throws IOException{
        System.out.println("io_layer::parse_network_data");
        //OVERRIDE ME!!!!
    }
}
