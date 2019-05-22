/*
 * The following HTML tags are required to display this applet in a browser: <applet
 * code="NotHelloWorldApplet.class" width="300" height="100"> </applet>
 */

import java.awt.EventQueue;

import javax.swing.*;

/**
 * @version 1.22 2007-06-12
 * @author Cay Horstmann
 */
public class NotHelloWorldApplet extends JApplet
{
   public void init()
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JLabel label = new JLabel("Not a Hello, World applet", SwingConstants.CENTER);
               add(label);
            }
         });
   }
}
