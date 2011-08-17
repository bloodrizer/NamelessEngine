/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import render.overlay.OverlaySystem;
import world.Timer;

/**
 * 
 * Simple main menu logo placeholder
 *
 * @author bloodrizer
 */
public class MainMenuLogo {
    private static final int LOGO_SIZE = 48;
    private TrueTypeFont logo_font;
    
    public int x;
    public int y;
    
    public String text = "Nameless Game";
    
    public MainMenuLogo(){
        logo_font = OverlaySystem.precache_font(LOGO_SIZE);
    }

    public void render(){
        //logo_font.drawString(x, y, text, color);
    }
    
    public int get_w(){
        return LOGO_SIZE*text.length();
    }
    
    public int get_h(){
        return LOGO_SIZE;
    }
    
    private int i;
    private Color color = Color.yellow;
    private long last_tick = Timer.get_time();
    
    public void update(){

        if ((Timer.get_time()-last_tick) < 2000 ){
            return;
        }
        last_tick = Timer.get_time();

        i++;
        double a = (double)i;
        color.r = (float)((128.0f+Math.sin(a)*128.0f)/255.0f);
        color.g = (float)((128.0f+Math.cos(a+2*Math.PI/3)*128.0f)/255.0f);
        color.b = (float)((128.0f+Math.sin(a+4*Math.PI/3)*128.0f)/255.0f); 
    }
}
