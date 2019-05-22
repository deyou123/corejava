/**
   @version 1.32 2004-05-06
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BoxLayoutTest
{
   public static void main(String[] args)
   {  
      BoxLayoutFrame frame = new BoxLayoutFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame that uses box layouts to organize various components.
*/
class BoxLayoutFrame extends JFrame
{  
   public BoxLayoutFrame()
   {  
      setTitle("BoxLayoutTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // construct the top horizontal box

      JLabel label1 = new JLabel("Name:");
      JTextField textField1 = new JTextField(10);
      textField1.setMaximumSize(textField1.getPreferredSize());

      Box hbox1 = Box.createHorizontalBox();
      hbox1.add(label1);
      // separate with a 10-pixel strut
      hbox1.add(Box.createHorizontalStrut(10));
      hbox1.add(textField1);

      // construct the middle horizontal box

      JLabel label2 = new JLabel("Password:");
      JTextField textField2 = new JTextField(10);
      textField2.setMaximumSize(textField2.getPreferredSize());
     

      Box hbox2 = Box.createHorizontalBox();
      hbox2.add(label2);
      // separate with a 10-pixel strut
      hbox2.add(Box.createHorizontalStrut(10));
      hbox2.add(textField2);

      // construct the bottom horizontal box

      JButton button1 = new JButton("Ok");
      JButton button2 = new JButton("Cancel");

      Box hbox3 = Box.createHorizontalBox();
      hbox3.add(button1);
      // use "glue" to push the two buttons apart
      hbox3.add(Box.createGlue());
      hbox3.add(button2);

      // add the three horizontal boxes inside a vertical box

      Box vbox = Box.createVerticalBox();
      vbox.add(hbox1);
      vbox.add(hbox2);
      vbox.add(Box.createGlue());
      vbox.add(hbox3);

      add(vbox, BorderLayout.CENTER);
   }

   public static final int DEFAULT_WIDTH = 200;
   public static final int DEFAULT_HEIGHT = 200;  
}


