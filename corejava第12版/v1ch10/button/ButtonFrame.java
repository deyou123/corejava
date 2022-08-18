package button;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A frame with a button panel.
 */
public class ButtonFrame extends JFrame
{
   private JPanel buttonPanel;
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;

   public ButtonFrame()
   {      
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // create buttons
      var yellowButton = new JButton("Yellow");
      var blueButton = new JButton("Blue");
      var redButton = new JButton("Red");

      buttonPanel = new JPanel();

      // add buttons to panel
      buttonPanel.add(yellowButton);
      buttonPanel.add(blueButton);
      buttonPanel.add(redButton);

      // add panel to frame
      add(buttonPanel);

      // create button actions
      var yellowAction = new ColorAction(Color.YELLOW);
      var blueAction = new ColorAction(Color.BLUE);
      var redAction = new ColorAction(Color.RED);

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
      private Color backgroundColor;

      public ColorAction(Color c)
      {
         backgroundColor = c;
      }

      public void actionPerformed(ActionEvent event)
      {
         buttonPanel.setBackground(backgroundColor);
      }
   }
}
