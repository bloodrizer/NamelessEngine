/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import ne.ui.NE_GUI_Element;

/**
 *
 * @author dpopov
 */
public class ETooltipShow extends Event{
    
    public NE_GUI_Element element = null;
    
    public ETooltipShow(NE_GUI_Element element){
        this.element = element;
    }
    
}
