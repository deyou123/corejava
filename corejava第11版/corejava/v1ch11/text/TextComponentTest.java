package text;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.42 2018-04-10
 * @author Cay Horstmann
 */
public class TextComponentTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         var frame = new TextComponentFrame();
         frame.setTitle("TextComponentTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
