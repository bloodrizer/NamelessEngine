/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import events.Event;
import events.IEventListener;
import events.network.EPlayerLogon;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.util.Point;

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

    static Collection<String[]> packets = new ArrayList<String[]>(100);

    private static class Receiver implements Runnable{
        /**
         * run() вызовется после запуска нити из конструктора клиента чата.
         */
        public void run() {
            while (!tcp_sock.isClosed()) {
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
                    close(); // ...закрываемся
                } else {
                    System.out.println("Server:" + line);
                }
            }
        }
    }

    //method is synchronised to prevent double closing
    public static synchronized void close() {
        if (!tcp_sock.isClosed()) {
            try {
                tcp_sock.close();
                System.exit(0);
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }

    static Thread recv_thread;

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

        recv_thread = new Thread(new Receiver());
        recv_thread.start();

        System.out.println("Connected successfuly!");
        //TODO: GUI EVENT THERE
    }

    public static boolean login(String login, String pass){
        sock_send("0x0000 "+login+" "+pass);
        //System.out.println(sock_recv());

        String[] reply = sock_recv();
        if (reply.length < 3){
            System.out.println("Malformed server reply:"+reply);
            return false;   //malformed request
        }

        if (reply[2].equals("0x00")){
            return true;
        }else{
            System.out.println("Unexpected result:"+reply);
            return false;
        }
    }


    public static void update(){
        Object[] packets_arr =  packets.toArray();
        for(int i = 0; i<packets_arr.length; i++){
            String[] data = (String[])packets_arr[i];
            parse_network_data(data);
        }

        packets.clear();    //must be cleared, or VERY FUNNY effect occurs
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

    private static void parse_network_data(String[] data){
        if (data[0].equals("0x0100") && data.length == 3){      //spawn player
            //System.out.println("Player @"+data[1]+";"+data[2]);
            //debug only
            EPlayerLogon event = new EPlayerLogon(new Point(
                    Integer.parseInt( data[1] ),
                    Integer.parseInt( data[2] )
            ));
            event.post();
        }
    }


    //--------------------------------------------------------------------------


    public void e_on_event(Event event){

    }
    
    public void e_on_event_rollback(Event event){

    }
}
