package dndImage;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates drag and drop in an image list.
 * @version 1.01 2012-01-26
 * @author Cay Horstmann
 */
public class ImageListDnDTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ImageListDnDFrame();
               frame.setTitle("ImageListDnDTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}
