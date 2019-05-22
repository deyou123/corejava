/**
 * @version 1.23 2007-06-12
 * @author Cay Horstmann
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConsoleWindowTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               // this is the button test program from chapter 8
               ButtonFrame frame = new ButtonFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);

               // initialize the console window--System.out will show here
               ConsoleWindow.init();
            }
         });
   }
}

/**
 * A frame with a button panel This code is identical to ButtonTest in chapter 8, except for the
 * logging in the actionPerformed method of the ColorAction class
 */
class ButtonFrame extends JFrame
{
   public ButtonFrame()
   {
      setTitle("ButtonTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add panel to frame

      ButtonPanel panel = new ButtonPanel();
      add(panel);
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;
}

/**
 * A panel with three buttons
 */
class ButtonPanel extends JPanel
{
   public ButtonPanel()
   {
      // create buttons

      JButton yellowButton = new JButton("Yellow");
      JButton blueButton = new JButton("Blue");
      JButton redButton = new JButton("Red");

      // add buttons to panel

      add(yellowButton);
      add(blueButton);
      add(redButton);

      // create button actions

      ColorAction yellowAction = new ColorAction(Color.YELLOW);
      ColorAction blueAction = new ColorAction(Color.BLUE);
      ColorAction redAction = new ColorAction(Color.RED);

      // associate actions with buttons

      yellowButton.addActionListener(yellowAction);
      blueButton.addActionListener(blueAction);
      redButton.addActionListener(redAction);
   }

   /**
    * An action listener that sets the panel's background color.
    */
   private class ColorAction implements ActionListener
   {
      public ColorAction(Color c)
      {
         backgroundColor = c;
      }

      public void actionPerformed(ActionEvent event)
      {
         System.out.println(event); // shows in console window
         setBackground(backgroundColor);
      }

      private Color backgroundColor;
   }
}
