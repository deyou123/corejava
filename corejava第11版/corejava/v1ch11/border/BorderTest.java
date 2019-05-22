package border;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.35 2018-04-10
 * @author Cay Horstmann
 */
public class BorderTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         var frame = new BorderFrame();
         frame.setTitle("BorderTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
