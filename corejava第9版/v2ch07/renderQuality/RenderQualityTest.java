package renderQuality;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates the effect of the various rendering hints.
 * @version 1.10 2007-08-16
 * @author Cay Horstmann
 */
public class RenderQualityTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new RenderQualityTestFrame();
               frame.setTitle("RenderQualityTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}