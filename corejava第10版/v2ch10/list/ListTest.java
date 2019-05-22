package list;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates a simple fixed list of strings.
 * @version 1.25 2016-05-10
 * @author Cay Horstmann
 */
public class ListTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new ListFrame();
            frame.setTitle("ListTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}