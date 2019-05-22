package composite;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates the Porter-Duff composition rules.
 * @version 1.04 2016-05-10
 * @author Cay Horstmann
 */
public class CompositeTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new CompositeTestFrame();
            frame.setTitle("CompositeTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

