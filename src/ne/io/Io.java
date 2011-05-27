/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import events.Event;
import events.IEventListener;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */

public class Io implements IEventListener {

    private static Socket tcp_sock = null;

    static PrintWriter out = null;
    static BufferedReader in = null;

    public Io(){
        
    }

    public static void connect(){
        try {
            tcp_sock = new Socket("admin.edi.inteliec.eu", 8022);

            out = new PrintWriter(tcp_sock.getOutputStream(), true);
            in =  new BufferedReader(new InputStreamReader(
                                        tcp_sock.getInputStream()));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Connected successfuly!");
        //TODO: GUI EVENT THERE
    }

    public static boolean login(String login, String pass){
        sock_send("0x0000 "+login+" "+pass);
        //System.out.println(sock_recv());

        String[] reply = sock_recv();
        if (reply.length < 3){
            return false;   //malformed request
        }

        if (reply[2].equals("0x00")){
            return true;
        }else{
            return false;
        }
    }


    public void update(){
        
    }

    private static String[] sock_recv(){
        try {
            return in.readLine().split(" ");
        } catch (IOException ex) {
            Logger.getLogger(Io.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    private static void sock_send(String msg){
        out.println(msg);
    }

    private void send_network_event(Event event){
        //System.event
    }


    public void e_on_event(Event event){

    }
    
    public void e_on_event_rollback(Event event){

    }
}
