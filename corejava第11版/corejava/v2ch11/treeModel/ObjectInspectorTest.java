package treeModel;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates how to use a custom tree model. It displays the fields of an object.
 * @version 1.06 2018-05-01
 * @author Cay Horstmann
 */
public class ObjectInspectorTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            var frame = new ObjectInspectorFrame();
            frame.setTitle("ObjectInspectorTest");               
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}



