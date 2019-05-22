package tree;

import java.awt.*;
import javax.swing.*;

/**
 * This program shows a simple tree.
 * @version 1.03 2016-05-10
 * @author Cay Horstmann
 */
public class SimpleTree
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new SimpleTreeFrame();
            frame.setTitle("SimpleTree");               
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}
