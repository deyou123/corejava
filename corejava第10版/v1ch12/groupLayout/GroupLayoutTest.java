package groupLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * @version 1.01 2015-06-12
 * @author Cay Horstmann
 */
public class GroupLayoutTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         JFrame frame = new FontFrame();
         frame.setTitle("GroupLayoutTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}