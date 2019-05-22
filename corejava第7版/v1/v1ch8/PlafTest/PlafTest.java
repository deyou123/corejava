/**
   @version 1.31 2004-05-04
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlafTest
{  
   public static void main(String[] args)
   {  
      PlafFrame frame = new PlafFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame with a button panel for changing look and feel
*/
class PlafFrame extends JFrame
{
   public PlafFrame()
   {
      setTitle("PlafTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add panel to frame

      PlafPanel panel = new PlafPanel();
      add(panel);
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;  
}

/**
   A panel with buttons to change the pluggable look and feel
*/
class PlafPanel extends JPanel
{  
   public PlafPanel()
   {  
      UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
      for (UIManager.LookAndFeelInfo info : infos)
         makeButton(info.getName(), info.getClassName());
   }

   /**
      Makes a button to change the pluggable look and feel.
      @param name the button name
      @param plafName the name of the look and feel class
    */
   void makeButton(String name, final String plafName)
   {  
      // add button to panel

      JButton button = new JButton(name);
      add(button);
      
      // set button action

      button.addActionListener(new 
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {  
               // button action: switch to the new look and feel
               try
               {  
                  UIManager.setLookAndFeel(plafName);
                  SwingUtilities.updateComponentTreeUI(PlafPanel.this);
               }
               catch(Exception e) { e.printStackTrace(); }
            }
         });
   }
}
