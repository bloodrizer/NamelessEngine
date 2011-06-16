/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.effects;

/*
 * Effects are quite similar to gui components
 * but they are usualy tile-assigned and have a limited lifespawn
 */
public class EffectsSystem {
    public final Effect_Element root = new Effect_Element();

    public void render(){
        root.render();
    }

    public void update(){
    }
}
