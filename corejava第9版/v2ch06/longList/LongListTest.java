package longList;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates a list that dynamically computes list entries.
 * @version 1.23 2007-08-01
 * @author Cay Horstmann
 */
public class LongListTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new LongListFrame();
               frame.setTitle("LongListTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}


