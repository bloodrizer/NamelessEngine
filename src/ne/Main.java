/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne;

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

        // TODO code application logic here
        game = new Game();

        game.set_state(Game.GameModes.InGame);
        game.run();
    }

    

}
