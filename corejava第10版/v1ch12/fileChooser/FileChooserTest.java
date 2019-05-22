package fileChooser;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.25 2015-06-12
 * @author Cay Horstmann
 */
public class FileChooserTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         JFrame frame = new ImageViewerFrame();
         frame.setTitle("FileChooserTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
