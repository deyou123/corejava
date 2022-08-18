package dataExchange;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.36 2021-05-30
 * @author Cay Horstmann
 */
public class DataExchangeTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         var frame = new DataExchangeFrame();
         frame.setTitle("DataExchangeTest");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
