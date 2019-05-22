package tableModel;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 * This program shows how to build a table from a table model.
 * @version 1.03 2006-05-10
 * @author Cay Horstmann
 */
public class InvestmentTable
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new InvestmentTableFrame();
            frame.setTitle("InvestmentTable");               
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * This frame contains the investment table.
 */
class InvestmentTableFrame extends JFrame
{
   public InvestmentTableFrame()
   {
      TableModel model = new InvestmentTableModel(30, 5, 10);
      JTable table = new JTable(model);
      add(new JScrollPane(table));
      pack();
   }

}

/**
 * This table model computes the cell entries each time they are requested. The table contents shows
 * the growth of an investment for a number of years under different interest rates.
 */
class InvestmentTableModel extends AbstractTableModel
{
   private static double INITIAL_BALANCE = 100000.0;
   
   private int years;
   private int minRate;
   private int maxRate;

   /**
    * Constructs an investment table model.
    * @param y the number of years
    * @param r1 the lowest interest rate to tabulate
    * @param r2 the highest interest rate to tabulate
    */
   public InvestmentTableModel(int y, int r1, int r2)
   {
      years = y;
      minRate = r1;
      maxRate = r2;
   }

   public int getRowCount()
   {
      return years;
   }

   public int getColumnCount()
   {
      return maxRate - minRate + 1;
   }

   public Object getValueAt(int r, int c)
   {
      double rate = (c + minRate) / 100.0;
      int nperiods = r;
      double futureBalance = INITIAL_BALANCE * Math.pow(1 + rate, nperiods);
      return String.format("%.2f", futureBalance);
   }

   public String getColumnName(int c)
   {
      return (c + minRate) + "%";
   }
}
