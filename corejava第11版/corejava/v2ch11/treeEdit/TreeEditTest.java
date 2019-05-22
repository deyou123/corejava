package treeEdit;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates tree editing.
 * @version 1.05 2018-05-01
 * @author Cay Horstmann
 */
public class TreeEditTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            var frame = new TreeEditFrame();
            frame.setTitle("TreeEditTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}
