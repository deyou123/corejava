package slider;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.14 2012-01-26
 * @author Cay Horstmann
 */
public class SliderTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               SliderFrame frame = new SliderFrame();
               frame.setTitle("SliderTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}