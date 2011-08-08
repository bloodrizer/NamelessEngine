/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.effects;

import events.ETakeDamage;
import events.ETooltipShow;
import events.Event;
import events.EventManager;
import events.IEventListener;
import events.network.EChatMessage;
import ne.ui.TooltipSystem;

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
    
    public FXTooltip show_tooltip(){
        return null;
    }

    public void e_on_event(Event event) {
        if (event instanceof EChatMessage){
            root.add(new FXTextBubble(
                (EChatMessage)event
            ));
        }

        if (event instanceof ETakeDamage){
            root.add(new FXDamage(
                (ETakeDamage)event
            ));
        }
        
        if (event instanceof ETooltipShow){
            FXTooltip tooltip = new FXTooltip(
                ((ETooltipShow)event).element
            );
            root.add(tooltip);
            TooltipSystem.set_tooltip(tooltip);
        }
    }

    public void e_on_event_rollback(Event event) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
