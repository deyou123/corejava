package layer;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates how a layer can decorate a Swing component. 
 * @version 1.0 2012-06-08
 * @author Cay Horstmann
 */
public class LayerTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ColorFrame();
               frame.setTitle("LayerTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}