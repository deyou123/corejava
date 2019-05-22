package dialog;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.34 2012-06-12
 * @author Cay Horstmann
 */
public class DialogTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         JFrame frame = new DialogFrame();
         frame.setTitle("DialogTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}