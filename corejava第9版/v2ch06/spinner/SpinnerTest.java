package spinner;

import java.awt.*;
import javax.swing.*;

/**
 * A program to test spinners.
 * @version 1.02 2007-08-01
 * @author Cay Horstmann
 */
public class SpinnerTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new SpinnerFrame();
               frame.setTitle("SpinnerTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

