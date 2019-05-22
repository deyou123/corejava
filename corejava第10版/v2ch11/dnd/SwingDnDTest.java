package dnd;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates the basic Swing support for drag and drop.
 * @version 1.11 2016-05-10
 * @author Cay Horstmann
 */
public class SwingDnDTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new SwingDnDFrame();
            frame.setTitle("SwingDnDTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}