package checkBox;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.35 2018-04-10
 * @author Cay Horstmann
 */
public class CheckBoxTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         var frame = new CheckBoxFrame();
         frame.setTitle("CheckBoxTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
