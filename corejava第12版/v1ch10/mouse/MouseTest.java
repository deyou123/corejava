package mouse;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.35 2018-04-10
 * @author Cay Horstmann
 */
public class MouseTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         var frame = new MouseFrame();
         frame.setTitle("MouseTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
