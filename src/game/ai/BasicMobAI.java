/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.ai;

import game.ent.controller.NpcController;
import org.lwjgl.util.Point;

/**
 *
 * @author Administrator
 */
public class BasicMobAI extends AI{

    enum AIState{
        STATE_IDLE,
        STATE_ROAMING,
        STATE_CHASE,
        STATE_AVOIDE
    }

    AIState state = AIState.STATE_IDLE;

    @Override
    public void update(){
        state = AIState.STATE_ROAMING;
    }

    @Override
    public void think(){

        if(owner.controller == null || ! (owner.controller instanceof NpcController)){
            return;
        }

        NpcController npc_ctrl = (NpcController)(owner.controller);

        switch(state){
            case STATE_ROAMING:
                if (npc_ctrl.path == null && (int)(Math.random() * 20) < 25 ){
                    int x = owner.origin.getX() + 5 - (int)(Math.random() * 10);
                    int y = owner.origin.getY() + 5 - (int)(Math.random() * 10);

                    npc_ctrl.set_destination(new Point(x,y));
                }
            break;
        }
    }
}
