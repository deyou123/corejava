package gridbag;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * @version 1.36 2018-04-10
 * @author Cay Horstmann
 */
public class GridBagLayoutTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->           {
               var frame = new FontFrame();
               frame.setTitle("GridBagLayoutTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
         });
   }
}
