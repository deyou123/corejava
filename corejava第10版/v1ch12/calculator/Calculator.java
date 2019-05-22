package calculator;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.34 2015-06-12
 * @author Cay Horstmann
 */
public class Calculator
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         CalculatorFrame frame = new CalculatorFrame();
         frame.setTitle("Calculator");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}
