/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne.ui;

import events.EMouseClick;
import events.Event;
import events.network.EChatMessage;
import player.Player;

/**
 *
 * @author Administrator
 */
public class NE_GUI_Chat extends NE_GUI_FrameModern {

    NE_GUI_Input input;

    NE_GUI_Text chat_history;

    public NE_GUI_Chat(){
        final NE_GUI_Chat __chat_control = this;

        chat_history = new NE_GUI_Text();
        add(chat_history);
        chat_history.x = 40;
        chat_history.y = 15;
        chat_history.dragable = false;

        input = new NE_GUI_Input(){
            @Override
            public void e_on_submit(){
                submit();
            }

        };
        input.x = 40;
        input.y = 95;

        input.w = 380;
        input.dragable = false;

        add(input);

        NE_GUI_Button submit = new NE_GUI_Button(){
            @Override
            public void e_on_mouse_click(EMouseClick e){
                submit();
            }
        };
        add(submit);
        submit.text = "Send";
        submit.set_tw(2);
        submit.x = 430;
        submit.y = 88;
        submit.dragable = false;

    }

    public void submit(){
        String text = input.text;
        input.text = "";

        //chat_history.add_line(text);

        EChatMessage message = new EChatMessage(Player.character_id,text);
        message.post();
    }

    @Override
    public void notify_event(Event e){
        //((NE_GUI_Element)this).notify_event(e);
        super.notify_event(e);

        if (e instanceof EChatMessage){
            EChatMessage chat_event = (EChatMessage)e;
            chat_history.add_line(chat_event.uid + " says: " + chat_event.message);
        }
    }
}
