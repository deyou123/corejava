package textFormat;

import java.awt.*;
import javax.swing.*;

/**
 * A program to test formatted text fields
 * @version 1.04 2016-05-10
 * @author Cay Horstmann
 */
public class FormatTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new FormatTestFrame();
            frame.setTitle("FormatTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

