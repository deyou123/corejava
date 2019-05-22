package textFormat;

import java.awt.*;
import javax.swing.*;

/**
 * A program to test formatted text fields
 * @version 1.03 2012-06-08
 * @author Cay Horstmann
 */
public class FormatTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new FormatTestFrame();
               frame.setTitle("FormatTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

