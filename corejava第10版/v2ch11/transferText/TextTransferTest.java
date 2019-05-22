package transferText;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates the transfer of text between a Java application and the system
 * clipboard.
 * @version 1.14 2016-05-10
 * @author Cay Horstmann
 */
public class TextTransferTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new TextTransferFrame();
            frame.setTitle("TextTransferTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}