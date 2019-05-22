package tableCellRender;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates cell rendering and editing in a table.
 * @version 1.04 2016-05-10
 * @author Cay Horstmann
 */
public class TableCellRenderTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {

            JFrame frame = new TableCellRenderFrame();
            frame.setTitle("TableCellRenderTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}