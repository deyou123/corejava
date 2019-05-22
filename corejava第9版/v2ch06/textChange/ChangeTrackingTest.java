package textChange;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.40 2007-08-05
 * @author Cay Horstmann
 */
public class ChangeTrackingTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ColorFrame();
               frame.setTitle("ChangeTrackingTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}