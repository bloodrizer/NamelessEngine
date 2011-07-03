/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.effects;

import world.Timer;

/**
 *
 * @author Administrator
 */
public class FXTooltip extends FXTextBubble {
    public FXTooltip(){
        super(null);

        life_time = 500;
    }

    public void refresh(){
        spawn_time = Timer.get_time();
    }

}
