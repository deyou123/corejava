package tableSelection;

/**
 @version 1.03 2007-08-01
 @author Cay Horstmann
 */

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates selection, addition, and removal of rows and columns.
 */
public class TableSelectionTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
      {
         public void run()
         {
            JFrame frame = new TableSelectionFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         }
      });
   }
}