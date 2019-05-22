package menu;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.25 2018-04-10
 * @author Cay Horstmann
 */
public class MenuTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         var frame = new MenuFrame();
         frame.setTitle("MenuTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
