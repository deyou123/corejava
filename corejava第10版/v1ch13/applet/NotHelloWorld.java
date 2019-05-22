package applet;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.24 2015-06-12
 * @author Cay Horstmann
 */
public class NotHelloWorld extends JApplet
{
   public void init()
   {
      EventQueue.invokeLater(() -> {
         JLabel label = new JLabel("Not a Hello, World applet",
               SwingConstants.CENTER);
         add(label);
      });
   }
}