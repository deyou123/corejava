package treeRender;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates cell rendering and listening to tree selection events.
 * @version 1.03 2007-08-01
 * @author Cay Horstmann
 */
public class ClassTree
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ClassTreeFrame();
               frame.setTitle("ClassTree");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

