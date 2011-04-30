/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render.overlay;
        
import java.awt.Font;
import org.newdawn.slick.TrueTypeFont;


/**
 *
 * @author Administrator
 */
public class OverlaySystem {
    
    public DebugOverlay debug = null;

    private static Font font = null;
    public  static TrueTypeFont ttf = null;

    public OverlaySystem() {
        font = new Font("Arial", Font.BOLD, 15);
        ttf = new TrueTypeFont(font, true);

    }

    //render whole overlay
    public void render() {
        DebugOverlay.render();
        TileCoordOverlay.render();
    }


}
