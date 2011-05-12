/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ne;

/**
 *
 * @author Administrator
 */
public class NEInput {

}
/*public class OverlayInputSystem implements InputSystem {
      private final LwjglKeyboardInputEventCreator inputEventCreator;
      private final List<MouseEvent> mouseEvents;
      private final List<KeyboardInputEvent> keyEvents;
      private final NiftyInputConsumer userConsumer; // unused events go here

      public OverlayInputSystem(NiftyInputConsumer userConsumer) {
         this.userConsumer = userConsumer;
         inputEventCreator = new LwjglKeyboardInputEventCreator();
         mouseEvents = Collections.synchronizedList(new ArrayList<MouseEvent>());
         keyEvents = Collections.synchronizedList(new ArrayList<KeyboardInputEvent>());
      }

      @Override
      public void forwardEvents(NiftyInputConsumer inputEventConsumer) {
         synchronized (mouseEvents) {
            for (MouseEvent event : mouseEvents) {
               if (!inputEventConsumer.processMouseEvent(event.mouseX, event.mouseY, event.mouseWheel, event.button, event.buttonDown)) {
                  // Event not used by Nifty, so send to the given user event consumer
                  userConsumer.processMouseEvent(event.mouseX, event.mouseY, event.mouseWheel, event.button, event.buttonDown);
                  // TODO because nifty runs in the render() method, I am concerned that the
                  // user consumer may eat too much time. Perhaps a polling mechanism with
                  // a ConcurrentQueue would be preferable.
               }
            }
            mouseEvents.clear();
         }
         synchronized (keyEvents) {
            for (KeyboardInputEvent event : keyEvents) {
               if (!inputEventConsumer.processKeyboardEvent(event)) {
                  // Event not used by Nifty, so send to the given user event consumer
                  userConsumer.processKeyboardEvent(event);
               }
            }
            keyEvents.clear();
         }
      }

      public void addKeyEvent(final int key, final char c, final boolean keyDown) {
         synchronized (keyEvents) {
            keyEvents.add(inputEventCreator.createEvent(key, c, keyDown));
         }

      }

      // Adds an Event directly to the user, bypassing the Nifty queue.
      // May be required if Nifty is not being rendered since it will then also not read its queue
      public void addUserKeyEvent(final int key, final char c, final boolean keyDown) {
         userConsumer.processKeyboardEvent(inputEventCreator.createEvent(key, c, keyDown));
      }

      @Override
      public void setMousePosition(int x, int y) {
      }

      public void addMouseEvent(final int x, final int y, final int mouseWheel, final int button, final boolean buttonDown) {
         MouseEvent event = new MouseEvent(x, y, buttonDown, button);
         event.mouseWheel = mouseWheel;
         synchronized (mouseEvents) {
            mouseEvents.add(event);
         }
         lastMouseEventButton = button;
         lastMouseEvents[lastMouseEventButton] = event;
      }

      public void addUserMouseEvent(final int x, final int y, final int mouseWheel, final int button, final boolean buttonDown) {
         userConsumer.processMouseEvent(x, y, mouseWheel, button, buttonDown);
         lastMouseEventButton = button;
         MouseEvent event = new MouseEvent(x, y, buttonDown, button);
         event.mouseWheel = mouseWheel;
         lastMouseEvents[lastMouseEventButton] = event;
      }
   }

   private class BasicScreenController implements ScreenController {
      @Override
      public void bind(Nifty nifty, Screen screen) {
         Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
                                                         "Default empty ScreenController attached to Nifty screen " + screen + ". "
                 + "This is probably not what you had in mind.");
      }

      @Override
      public void onEndScreen() {
      }

      @Override
      public void onStartScreen() {
      }
   }
}*/