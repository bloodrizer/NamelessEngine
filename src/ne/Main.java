/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne;

import server.NEServer;

/**
 *
 * @author Administrator
 */
public class Main {

    /**
     * @param args the command line arguments
     */


    public static Game game;

    public static void main(String[] args) {
        
        //run ne server on localhost for debug purpose
        NEServer neServer = new NEServer();
        neServer.run();
        
        System.out.println("-------------------------------------");
        System.out.println("Starting local client");

        game = new Game();

        game.set_state(Game.GameModes.MainMenu);
        game.run();

    }

    

}
