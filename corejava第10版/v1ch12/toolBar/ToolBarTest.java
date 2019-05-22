package toolBar;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.14 2015-06-12
 * @author Cay Horstmann
 */
public class ToolBarTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         ToolBarFrame frame = new ToolBarFrame();
         frame.setTitle("ToolBarTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}