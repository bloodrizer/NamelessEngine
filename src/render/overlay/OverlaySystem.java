/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render.overlay;
        
import java.awt.Font;
import java.io.InputStream;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import render.Render;


/**
 *
 * @author Administrator
 */
public class OverlaySystem {

    
    public DebugOverlay debug = null;

    private static Font font = null;
    public  static TrueTypeFont ttf = null;

    public static final int FONT_SIZE = 15;

    public OverlaySystem() {
        font = new Font("Arial", Font.BOLD, FONT_SIZE);
        ttf = new TrueTypeFont(font, true);
    }

    public static TrueTypeFont precache_font(int size){
        Font _font = new Font("Arial", Font.BOLD, size);
        TrueTypeFont _ttf = new TrueTypeFont(_font, true);

        return _ttf;
    }

    public static TrueTypeFont precache_font(String filename, int size){
        try {
            InputStream is = Render.class.getResourceAsStream(filename);
            Font __font = Font.createFont(Font.TRUETYPE_FONT, is);

            TrueTypeFont _ttf = new TrueTypeFont(__font.deriveFont(Font.PLAIN, size), true);
            return _ttf;
        }
        catch(Exception e){
            System.err.println(filename + " not loaded.  Using serif font.");
            return precache_font(size);
        }
    }

    //render whole overlay
    public void render() {
        
        DebugOverlay.render();
        TileCoordOverlay.render();
        VersionOverlay.render();
        //InventoryOverlay.render();

    }


}
