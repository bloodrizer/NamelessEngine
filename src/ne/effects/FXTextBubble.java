/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.effects;

import render.AreaRenderer;
import ne.ui.NE_GUI_SpriteArea;
import render.Render;
import events.network.EChatMessage;
import game.ent.Entity;
import game.ent.EntityManager;
import org.newdawn.slick.Color;
import render.Tileset;
import render.overlay.OverlaySystem;
import world.WorldView;
import world.WorldViewCamera;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Administrator
 */
public class FXTextBubble extends Effect_Element {

    Entity ent;
    String message;

    static AreaRenderer bubble_sprite = new AreaRenderer(){
        {
            texture_name = "/render/gfx/effects/bubble.png";
            set_size(64,64);
        }
    };

    FXTextBubble(EChatMessage eChatMessage) {
        if(eChatMessage == null){
            return; //solves compatability issues with child class
        }

        Entity player_ent = EntityManager.get_entity(eChatMessage.uid);
        if (player_ent!=null){
            ent = player_ent;
            message = eChatMessage.message;
        }else{
            System.err.println("Failed to aquire entity #"+eChatMessage.uid);
        }
    }

    /**
     * Calculates entity position in screen coords and draws a text bubble
     */
    @Override
    public void render(){

        if (ent == null){
            return;
        }
        int ent_screen_x = WorldView.world2local_x(
                (ent.origin.getX() + ent.dx )*Tileset.TILE_SIZE,
                (ent.origin.getY() + ent.dy )*Tileset.TILE_SIZE
            ) - message.length()/2*8

            - (int)WorldViewCamera.camera_x
            ;

        int ent_screen_y = WorldView.world2local_y(
                    (ent.origin.getX()+ ent.dx)*Tileset.TILE_SIZE ,
                    (ent.origin.getY()+ ent.dy)*Tileset.TILE_SIZE
        ) - 54

           - (int)WorldViewCamera.camera_y
        ;

        render_bubble(ent_screen_x, ent_screen_y);

    }
    
    //draw bubble sprite and text
    protected void render_bubble( int ent_screen_x, int ent_screen_y){
        int fx_alpha = 255;
        if (get_life_left() < 1000){
            fx_alpha = (int)(((float)get_life_left()/1000.f)*255);
        }
        
        int w = message.length()*8 + 8;
        int h = 24;


        int x = ent_screen_x - 6;
        int y = ent_screen_y - 14;

        glEnable(GL_TEXTURE_2D);
        float a = (float)fx_alpha/255.0f;

        //Render.bind_texture("/render/gfx/effects/bubble.png");

        bubble_sprite.set_rect(0, 0, 8, 32);
        bubble_sprite.render(x, y, 8, 24, a);

        bubble_sprite.set_rect(8, 0, 16, 32);
        bubble_sprite.render(x+8, y, message.length()*8, 24, a);

        bubble_sprite.set_rect(24, 0, 8, 32);
        bubble_sprite.render(x+8 + message.length()*8, y, 8, 24, a);

        bubble_sprite.set_rect(0, 32, 7, 4);
        bubble_sprite.render(x + message.length()*4, y+23, 7, 4, a);

        Color text_color;

        if (get_life_left() < 1000){
            text_color = new Color(0,0,0, fx_alpha );
        }else{
            text_color = Color.black;
        }


        OverlaySystem.ttf.drawString(
                ent_screen_x + 4,
                ent_screen_y - 11,
        message, text_color);
    }

}
