import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 * This program shows how to build a table from a table model.
 * @version 1.02 2007-08-01
 * @author Cay Horstmann
 */
public class InvestmentTable
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new InvestmentTableFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
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
      setTitle("InvestmentTable");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      TableModel model = new InvestmentTableModel(30, 5, 10);
      JTable table = new JTable(model);
      add(new JScrollPane(table));
   }

   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 300;
}

/**
 * This table model computes the cell entries each time they are requested. The table contents shows
 * the growth of an investment for a number of years under different interest rates.
 */
class InvestmentTableModel extends AbstractTableModel
{
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

   private int years;
   private int minRate;
   private int maxRate;

   private static double INITIAL_BALANCE = 100000.0;
}
