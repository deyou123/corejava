package internalFrame;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates the use of internal frames.
 * @version 1.12 2016-05-10
 * @author Cay Horstmann
 */
public class InternalFrameTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new DesktopFrame();
            frame.setTitle("InternalFrameTest");               
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}