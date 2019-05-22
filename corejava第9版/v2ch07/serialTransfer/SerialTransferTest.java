package serialTransfer;

import java.awt.*;
import javax.swing.*;

/**
 * This program demonstrates the transfer of serialized objects between virtual machines.
 * @version 1.02 2007-08-16
 * @author Cay Horstmann
 */
public class SerialTransferTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new SerialTransferFrame();
               frame.setTitle("SerialTransferTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}