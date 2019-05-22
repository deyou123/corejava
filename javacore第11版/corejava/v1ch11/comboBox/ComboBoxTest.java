package comboBox;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.36 2018-04-10
 * @author Cay Horstmann
 */
public class ComboBoxTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         var frame = new ComboBoxFrame();
         frame.setTitle("ComboBoxTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
