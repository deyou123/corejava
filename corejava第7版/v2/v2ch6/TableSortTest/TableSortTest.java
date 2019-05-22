/**
   @version 1.01 2004-08-22
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
   This program demonstrates how to sort a table column.
   Double-click on a table columm's header to sort it.
*/
public class TableSortTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new TableSortFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame contains a table of planet data.
*/
class TableSortFrame extends JFrame
{  
   public TableSortFrame()
   {  
      setTitle("TableSortTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // set up table model and interpose sorter

      DefaultTableModel model = new DefaultTableModel(cells, columnNames);
      final SortFilterModel sorter = new SortFilterModel(model);

      // show table

      final JTable table = new JTable(sorter);
      add(new JScrollPane(table), BorderLayout.CENTER);

      // set up double click handler for column headers

      table.getTableHeader().addMouseListener(new 
         MouseAdapter()
         {  
            public void mouseClicked(MouseEvent event)
            {  
               // check for double click
               if (event.getClickCount() < 2) return;

               // find column of click and
               int tableColumn = table.columnAtPoint(event.getPoint());

               // translate to table model index and sort
               int modelColumn = table.convertColumnIndexToModel(tableColumn);
               sorter.sort(modelColumn);
            }
         });
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

/**
   This table model takes an existing table model and produces a new model that sorts the rows 
   so that the entries in a given column are sorted.
*/
class SortFilterModel extends AbstractTableModel
{
   /**
      Constructs a sort filter model.
      @param m the table model whose rows should be sorted
   */
   public SortFilterModel(TableModel m)
   {  
      model = m;
      rows = new Row[model.getRowCount()];
      for (int i = 0; i < rows.length; i++)
      {  
         rows[i] = new Row();
         rows[i].index = i;
      }
   }

   /**
      Sorts the rows.
      @param c the column that should become sorted
   */
   public void sort(int c)
   {  
      sortColumn = c;
      Arrays.sort(rows);
      fireTableDataChanged();
   }

   // Compute the moved row for the three methods that access model elements

   public Object getValueAt(int r, int c) { return model.getValueAt(rows[r].index, c); }

   public boolean isCellEditable(int r, int c) { return model.isCellEditable(rows[r].index, c); }

   public void setValueAt(Object aValue, int r, int c) 
   { 
      model.setValueAt(aValue, rows[r].index, c);
   }

   // delegate all remaining methods to the model

   public int getRowCount() { return model.getRowCount(); }
   public int getColumnCount() { return model.getColumnCount(); }
   public String getColumnName(int c) { return model.getColumnName(c); }
   public Class getColumnClass(int c) { return model.getColumnClass(c); }

   /** 
      This inner class holds the index of the model row
      Rows are compared by looking at the model row entries
      in the sort column.
   */
   private class Row implements Comparable<Row>
   {  
      public int index;
      public int compareTo(Row other)
      {  
         Object a = model.getValueAt(index, sortColumn);
         Object b = model.getValueAt(other.index, sortColumn);
         if (a instanceof Comparable)
            return ((Comparable) a).compareTo(b);
         else
            return a.toString().compareTo(b.toString());
      }
   }

   private TableModel model;
   private int sortColumn;
   private Row[] rows;
}

