package list;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates a simple fixed list of strings.
 * @version 1.24 2012-06-07
 * @author Cay Horstmann
 */
public class ListTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ListFrame();
               frame.setTitle("ListTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}