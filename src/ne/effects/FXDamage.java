/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.effects;

import events.ETakeDamage;
import game.combat.Damage;
import game.ent.Entity;
import org.newdawn.slick.Color;

import render.TilesetRenderer;
import render.overlay.OverlaySystem;
import world.WorldView;
import world.WorldViewCamera;

/**
 *
 * @author Administrator
 */
class FXDamage extends Effect_Element{

    Entity ent;
    Damage damage;

    public FXDamage(ETakeDamage eTakeDamage) {
        this.damage = eTakeDamage.dmg;
        this.ent = eTakeDamage.ent;

        this.life_time = 500;
    }


    @Override
    public void render(){
        if (ent == null){
            return;
        }
        int ent_screen_x = WorldView.world2local_x(
                (ent.origin.getX() + ent.dx )*TilesetRenderer.TILE_SIZE,
                (ent.origin.getY() + ent.dy )*TilesetRenderer.TILE_SIZE
            ) - 8

            - (int)WorldViewCamera.camera_x
            ;

        int ent_screen_y = WorldView.world2local_y(
                    (ent.origin.getX()+ ent.dx)*TilesetRenderer.TILE_SIZE ,
                    (ent.origin.getY()+ ent.dy)*TilesetRenderer.TILE_SIZE
        ) - 54

           - (int)WorldViewCamera.camera_y
        ;

        int w = 8;
        int h = 24;


        float life_amt = (float)get_life_left()/(float)life_time;

        int x = ent_screen_x - (int)(life_amt * 12);
        int y = ent_screen_y + (int)(life_amt * 20);


        Color text_color = new Color(100,0,0, (int)(life_amt*255) );



        OverlaySystem.ttf.drawString(
                x + 15,
                y - 5,
        "-" + damage.amt, text_color);

    }

}
