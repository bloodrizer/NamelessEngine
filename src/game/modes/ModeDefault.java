/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.modes;

import de.matthiasmann.twl.Widget;
import render.Tileset;
import ui.GameUI;

/**
 *
 * @author Administrator
 */
public class ModeDefault implements IGameMode {

    //private Tileset tileset = null;

    private Tileset bg_tileset;
    public ModeDefault(){
        
    }

    public void run(){
        bg_tileset = new Tileset();
    }

    public void update(){
        bg_tileset.render_background();
    }

    public Widget get_ui(){
        return new GameUI();
    }
}
