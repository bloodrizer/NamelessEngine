/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
/*import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.textfield.controller.TextField;
import de.lessvoid.nifty.controls.textfield.controller.TextFieldControl;*/
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.chatcontrol.builder.ChatBuilder;

import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import events.EMouseClick;
import events.network.ESelectCharacter;
import ne.Game;
import ne.Game.GameModes;
import ne.Main;
import ne.io.Io;
import ne.ui.NE_GUI_Button;
import ne.ui.NE_GUI_Frame;
import ne.ui.NE_GUI_Input;
import ne.ui.NE_GUI_Label;
import ne.ui.NE_GUI_System;




/**
 *
 * @author Administrator
 */


public class MainMenuUI implements IUserInterface {

    public NE_GUI_System ui;
    public MainMenuUI(){
         ui = new NE_GUI_System();
    }

    private static Screen screen = null;


    public void login(){

       
    }

    NE_GUI_Input login_input;
    NE_GUI_Input pass_input;


    public void show_message(String message){
        NE_GUI_Frame frame = new NE_GUI_Frame(true);
        ui.root.add(frame);
        frame.set_tw(
                (message.length()*8 / NE_GUI_Frame.WIN_TILE_SIZE)  + 2
                );
        frame.set_th(3);

        NE_GUI_Label label1 = new NE_GUI_Label();
        frame.add(label1);
        label1.text = message;
        label1.x = frame.w/2 - (message.length()/2*8) + 16;
        label1.y = frame.h/2-7;
        
    }

    public void build_ui() {

       

        NE_GUI_Frame frame = new NE_GUI_Frame(false);
        ui.root.add(frame);
        frame.set_tw(12);
        frame.set_th(6);

        login_input = new NE_GUI_Input();
        frame.add(login_input);
        login_input.x = 180;
        login_input.y = 50;
        login_input.w = 120;
        login_input.dragable = false;

        login_input.text = "Red";

        pass_input = new NE_GUI_Input();
        frame.add(pass_input);
        pass_input.x = 180;
        pass_input.y = 80;
        pass_input.w = 120;
        pass_input.dragable = false;

        pass_input.text = "true";

        NE_GUI_Label label1 = new NE_GUI_Label();
        frame.add(label1);
        label1.text = "Login";
        label1.x = 100;
        label1.y = 50;

        NE_GUI_Label label2 = new NE_GUI_Label();
        frame.add(label2);
        label2.text = "Password";
        label2.x = 100;
        label2.y = 80;


        NE_GUI_Button button = new NE_GUI_Button() {
            @Override
            public void e_on_mouse_click(EMouseClick e){
                //NE_GUI_System.root.clear();
                //Main.game.set_state(Game.GameModes.InGame);
                //Main.game.run();
                Io.connect();

                Io.login(login_input.text, pass_input.text);
                /*if(login_result){
                    //Main.game.set_state(Game.GameModes.InGame);

                    System.out.println("Loged in successfuly!");

                    ESelectCharacter event = new ESelectCharacter();
                    event.post();
                }else{
                    show_message("Failed to log in");
                }*/
            }
        };

        frame.add(button);

        button.dragable = false;

        button.x = 140;
        button.y = 120;
        button.set_tw(3);

        
    }

    public NE_GUI_System get_nge_ui() {
        return ui;
    }


}
