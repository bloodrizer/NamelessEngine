/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render.overlay;
        
import java.awt.Font;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;


/**
 *
 * @author Administrator
 */
public class OverlaySystem {

    
    public DebugOverlay debug = null;

    private static Font font = null;
    public  static TrueTypeFont ttf = null;

    public static final int FONT_SIZE = 15;

    public static final char[] russian_chars = new char[] {
             // 0x451 - буква ё, 0х401 - буква Ё
             0x430, 0x431, 0x432, 0x433, 0x434, 0x435, 0x436, 0x437, 0x438, 0x439, 0x43A,
             0x43B, 0x43C, 0x43D, 0x43E, 0x43F, 0x440, 0x441, 0x442, 0x443, 0x444, 0x445,
             0x446, 0x447, 0x448, 0x449, 0x44A, 0x44B, 0x44C, 0x44D, 0x44E, 0x44F, 0x451,
             0x410, 0x411, 0x412, 0x413, 0x414, 0x415, 0x416, 0x417, 0x418, 0x419, 0x41A,
             0x41B, 0x41C, 0x41D, 0x41E, 0x41F, 0x420, 0x421, 0x422, 0x423, 0x424, 0x425,
             0x426, 0x427, 0x428, 0x429, 0x42A, 0x42B, 0x42C, 0x42D, 0x42E, 0x42F, 0x401
     };

    public OverlaySystem() {
        font = new Font("Arial", Font.BOLD, FONT_SIZE);
        ttf = new TrueTypeFont(font, true, russian_chars);
    }

    public static TrueTypeFont precache_font(int size){
        Font _font = new Font("Arial", Font.BOLD, size);
        TrueTypeFont _ttf = new TrueTypeFont(_font, true, russian_chars);

        return _ttf;
    }

    //render whole overlay
    public void render() {
        
        DebugOverlay.render();
        TileCoordOverlay.render();
        VersionOverlay.render();
        //InventoryOverlay.render();

    }


}
