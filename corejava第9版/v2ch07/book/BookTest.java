package book;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates the printing of a multi-page book. It prints a "banner", by blowing up
 * a text string to fill the entire page vertically. The program also contains a generic print
 * preview dialog.
 * @version 1.12 2007-08-16
 * @author Cay Horstmann
 */
public class BookTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new BookTestFrame();
               frame.setTitle("BookTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}