/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.effects;

import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.EChatMessage;

/*
 * Effects are quite similar to gui components
 * but they are usualy tile-assigned and have a limited lifespawn
 */
public class EffectsSystem implements IEventListener{
    public final Effect_Element root = new Effect_Element();

    public EffectsSystem(){
        EventManager.subscribe(this);
    }

    public void render(){
        root.render();
    }

    public void update(){
        root.update();
    }

    public void e_on_event(Event event) {
        if (event instanceof EChatMessage){
            root.add(new FXTextBubble(
                (EChatMessage)event
            ));
        }
    }

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
