package fileChooser;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.24 2012-05-05
 * @author Cay Horstmann
 */
public class FileChooserTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ImageViewerFrame();
               frame.setTitle("FileChooserTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

