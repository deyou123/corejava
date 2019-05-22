package gridbag;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.34 2012-05-05
 * @author Cay Horstmann
 */
public class GridBagLayoutTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new FontFrame();
               frame.setTitle("GridBagLayoutTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}