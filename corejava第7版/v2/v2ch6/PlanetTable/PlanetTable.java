/**
   @version 1.10 2004-08-22
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
   This program demonstrates how to show a simple table
*/
public class PlanetTable
{ 
   public static void main(String[] args)
   {  
      JFrame frame = new PlanetTableFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);      
   }
}

/**
   This frame contains a table of planet data.
*/
class PlanetTableFrame extends JFrame
{  
   public PlanetTableFrame()
   {  
      setTitle("PlanetTable");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      final JTable table = new JTable(cells, columnNames);
      add(new JScrollPane(table), BorderLayout.CENTER);
      JButton printButton = new JButton("Print");
      printButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               try
               {
                  table.print();
               }
               catch (java.awt.print.PrinterException e)
               {
                  e.printStackTrace();
               }
            }
         });
      JPanel buttonPanel = new JPanel();
      buttonPanel.add(printButton);
      add(buttonPanel, BorderLayout.SOUTH);
   }

   private Object[][] cells =
   {  
      { "Mercury", 2440.0,  0, false, Color.yellow },    
      { "Venus", 6052.0, 0, false, Color.yellow },
      { "Earth", 6378.0, 1, false, Color.blue },
      { "Mars", 3397.0, 2, false, Color.red },
      { "Jupiter", 71492.0, 16, true, Color.orange },
      { "Saturn", 60268.0, 18, true, Color.orange },
      { "Uranus", 25559.0, 17, true, Color.blue },
      { "Neptune", 24766.0, 8, true, Color.blue },
      { "Pluto", 1137.0, 1, false, Color.black }
   };

   private String[] columnNames = { "Planet", "Radius", "Moons", "Gaseous", "Color" };

   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 200;
}
