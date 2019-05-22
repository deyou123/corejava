package imageProcessing;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates various image processing operations.
 * @version 1.04 2016-05-10
 * @author Cay Horstmann
 */
public class ImageProcessingTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new ImageProcessingFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}