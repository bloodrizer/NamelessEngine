/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.io;

import java.net.ServerSocket;

/**
 *
 * @author Administrator
 */
public class Io {

    private ServerSocket tcp_sock = null;

    public Io(){
        try {
            tcp_sock = new ServerSocket();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void update(){
        
    }
}
