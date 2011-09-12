/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package render.overlay;
        
import java.awt.Font;
import java.io.InputStream;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import render.Render;
import world.WorldView;


/**
 * Overlay System features lot's of debug functions and render helpers, that are associated neither with world rendering, no GUI system.
 * 
 * It can be debug text, or lines, or graphs, etc.
 *
 * @author bloodrizer
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
    
    /**
     * Connects two tiles in given world coordinates
     * That might be useful for AI pathfinding debug
     */
    public static void drawLine(Point tileCoord1, Point tileCoord2, Color color){
        int x1 = WorldView.get_tile_x_screen(tileCoord1);
        int y1 = WorldView.get_tile_y_screen(tileCoord1);
        int x2 = WorldView.get_tile_x_screen(tileCoord2);
        int y2 = WorldView.get_tile_y_screen(tileCoord2);
        
        drawLine(x1,x2,y1,y2, color);
    }
    
    public static void drawLine(int x1, int y1, int x2, int y2, Color color){
        
        glLineWidth(4.0f);
        
        glColor3f(color.r, color.g, color.b);
        glBegin(GL_LINE);
        glVertex2d(x1, y1);
        glVertex2d(x2, y2);
        glEnd();
        
        glLineWidth(1.0f);
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
        
    }


}
