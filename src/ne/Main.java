/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne;

import java.io.DataInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

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

        game = new Game();

        game.set_state(Game.GameModes.MainMenu);
        game.run();

    }

    

}
