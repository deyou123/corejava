package table;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

/**
 * This program demonstrates how to show a simple table.
 * @version 1.12 2012-06-09
 * @author Cay Horstmann
 */
public class TableTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new PlanetTableFrame();
               frame.setTitle("TableTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame contains a table of planet data.
 */
class PlanetTableFrame extends JFrame
{
   private String[] columnNames = { "Planet", "Radius", "Moons", "Gaseous", "Color" };
   private Object[][] cells = { { "Mercury", 2440.0, 0, false, Color.YELLOW },
         { "Venus", 6052.0, 0, false, Color.YELLOW }, { "Earth", 6378.0, 1, false, Color.BLUE },
         { "Mars", 3397.0, 2, false, Color.RED }, { "Jupiter", 71492.0, 16, true, Color.ORANGE },
         { "Saturn", 60268.0, 18, true, Color.ORANGE },
         { "Uranus", 25559.0, 17, true, Color.BLUE }, { "Neptune", 24766.0, 8, true, Color.BLUE },
         { "Pluto", 1137.0, 1, false, Color.BLACK } };

   public PlanetTableFrame()
   {
      final JTable table = new JTable(cells, columnNames);
      table.setAutoCreateRowSorter(true);
      add(table, BorderLayout.CENTER);
      JButton printButton = new JButton("Print");
      printButton.addActionListener(EventHandler.create(ActionListener.class, table, "print"));
      JPanel buttonPanel = new JPanel();
      buttonPanel.add(printButton);
      add(buttonPanel, BorderLayout.SOUTH);
      pack();
   }
}
