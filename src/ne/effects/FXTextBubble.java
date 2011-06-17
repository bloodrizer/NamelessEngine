/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.effects;

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

    FXTextBubble(EChatMessage eChatMessage) {
        Entity player_ent = EntityManager.get_entity(eChatMessage.uid);
        if (player_ent!=null){
            ent = player_ent;
            message = eChatMessage.message;
        }else{
            System.err.println("Failed to aquire entity #"+eChatMessage.uid);
        }
    }

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

        int w = message.length()*8 + 8;
        int h = 24;


        int x = ent_screen_x - 6;
        int y = ent_screen_y - 14;

        glEnable(GL_TEXTURE_2D);
        glColor4f(1.0f,1.0f,1.0f,1.0f);

        Render.bind_texture("/render/gfx/effects/bubble.png");

        glBegin(GL_QUADS);
                glTexCoord2f(0.0f, 0.0f);
            glVertex2f( x,   y);
                glTexCoord2f(0.0f+1.0f, 0.0f);
            glVertex2f( x+w, y);
                glTexCoord2f(0.0f+1.0f, 0.0f+1.0f);
            glVertex2f( x+w, y+h);
                glTexCoord2f(0.0f, 0.0f+1.0f);
            glVertex2f( x,   y+h);
        glEnd();


        Color text_color;

        if (get_life_left() < 1000){
            text_color = new Color(0,0,0, (int)(((float)get_life_left()/1000.f)*255) );
        }else{
            text_color = Color.black;
        }


        OverlaySystem.ttf.drawString(
                ent_screen_x,
                ent_screen_y - 10,
        message, text_color);

        /*OverlaySystem.ttf.drawString(200, 200,
                "Transparency:"+ (255-((float)get_life_left()/(float)life_time)*255),
                Color.black);*/

    }

}
