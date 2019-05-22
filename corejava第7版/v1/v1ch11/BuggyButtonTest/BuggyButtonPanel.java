/**
   @version 1.21 2002-07-06
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class BuggyButtonPanel extends JPanel 
{  
   public BuggyButtonPanel()
   {  
      ActionListener listener = new ButtonListener();

      JButton yellowButton = new JButton("Yellow");
      add(yellowButton);
      yellowButton.addActionListener(listener); 

      JButton blueButton = new JButton("Blue");
      add(blueButton);
      blueButton.addActionListener(listener); 

      JButton redButton = new JButton("Red");
      add(redButton);
      redButton.addActionListener(listener);
   }

   private class ButtonListener implements ActionListener
   {   
      public void actionPerformed(ActionEvent event)
      {
         String arg = event.getActionCommand();
         if (arg.equals("yellow"))
            setBackground(Color.yellow);
         else if (arg.equals("blue")) 
            setBackground(Color.blue);
         else if (arg.equals("red")) 
            setBackground(Color.red);
      }
   }
}  

