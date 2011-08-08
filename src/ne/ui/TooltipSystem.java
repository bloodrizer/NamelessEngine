/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ne.ui;

import events.ETooltipShow;
import ne.Input;
import ne.effects.FXTooltip;
import world.Timer;

/**
 *
 * @author dpopov
 */
public class TooltipSystem {
    
    static final long TOOLTIP_HOVER_TIME = 4000;    //number of ms to hold mouse over control before showing tooltip
    
    NE_GUI_System gui = null;
    NE_GUI_Element focused_element = null;
    long hover_time = 0;
    long last_tick = 0;
    
    static FXTooltip fx_tooltip = null;

    void set_gui(NE_GUI_System gui) {
        this.gui = gui;
    }
    
    public static void set_tooltip(FXTooltip fx_tooltip){
        TooltipSystem.fx_tooltip = fx_tooltip;
    }
    
    void update() {
        
        
        NE_GUI_Element elem = gui.get_gui_element(Input.get_mx(), Input.get_my());
        if (elem != null && elem != focused_element){
            tooltip_cancel();
        }else{
            hover_time += ( Timer.get_time() - last_tick );
            
            if (hover_time > TOOLTIP_HOVER_TIME) {
                tooltip_show(elem);
            }
        }

        last_tick = Timer.get_time();
    }

    private void tooltip_cancel() {
        hover_time = 0;
        //hide tooltip if presents
        if (fx_tooltip != null){
            //fx_tooltip.gc();
            fx_tooltip.disable();
        }
    }

    private void tooltip_show(NE_GUI_Element elem) {
        //throw new UnsupportedOperationException("Not yet implemented");
        if (fx_tooltip == null){
            //show it there
            ETooltipShow tooltip_show_msg = new ETooltipShow(elem);
            tooltip_show_msg.post();
        }
    }
    
}
