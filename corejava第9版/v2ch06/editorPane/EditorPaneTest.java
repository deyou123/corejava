package editorPane;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates how to display HTML documents in an editor pane.
 * @version 1.04 2012-01-26
 * @author Cay Horstmann
 */
public class EditorPaneTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new EditorPaneFrame();
               frame.setTitle("EditorPaneTest");               
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}
