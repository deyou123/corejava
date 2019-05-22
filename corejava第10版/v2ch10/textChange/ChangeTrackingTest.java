package textChange;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.41 2016-05-10
 * @author Cay Horstmann
 */
public class ChangeTrackingTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new ColorFrame();
            frame.setTitle("ChangeTrackingTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}