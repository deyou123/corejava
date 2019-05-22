package text;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.41 2015-06-12
 * @author Cay Horstmann
 */
public class TextComponentTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         JFrame frame = new TextComponentFrame();
         frame.setTitle("TextComponentTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
