package tableRowColumn;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * This frame contains a table of planet data.
 */
public class PlanetTableFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 500;

   public static final int COLOR_COLUMN = 4;
   public static final int IMAGE_COLUMN = 5;

   private JTable table;
   private HashSet<Integer> removedRowIndices;
   private ArrayList<TableColumn> removedColumns;   
   private JCheckBoxMenuItem rowsItem;
   private JCheckBoxMenuItem columnsItem;
   private JCheckBoxMenuItem cellsItem;

   private String[] columnNames = { "Planet", "Radius", "Moons", "Gaseous", "Color", "Image" };

   private Object[][] cells = {
         { "Mercury", 2440.0, 0, false, Color.YELLOW, 
            new ImageIcon(getClass().getResource("Mercury.gif")) },
         { "Venus", 6052.0, 0, false, Color.YELLOW, 
            new ImageIcon(getClass().getResource("Venus.gif")) },
         { "Earth", 6378.0, 1, false, Color.BLUE, 
            new ImageIcon(getClass().getResource("Earth.gif")) },
         { "Mars", 3397.0, 2, false, Color.RED, 
            new ImageIcon(getClass().getResource("Mars.gif")) },
         { "Jupiter", 71492.0, 16, true, Color.ORANGE, 
            new ImageIcon(getClass().getResource("Jupiter.gif")) },
         { "Saturn", 60268.0, 18, true, Color.ORANGE, 
            new ImageIcon(getClass().getResource("Saturn.gif")) },
         { "Uranus", 25559.0, 17, true, Color.BLUE, 
            new ImageIcon(getClass().getResource("Uranus.gif")) },
         { "Neptune", 24766.0, 8, true, Color.BLUE, 
            new ImageIcon(getClass().getResource("Neptune.gif")) },
         { "Pluto", 1137.0, 1, false, Color.BLACK, 
            new ImageIcon(getClass().getResource("Pluto.gif")) } };

   public PlanetTableFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      TableModel model = new DefaultTableModel(cells, columnNames)
         {
            public Class<?> getColumnClass(int c)
            {
               return cells[0][c].getClass();
            }
         };

      table = new JTable(model);
      
      table.setRowHeight(100);
      table.getColumnModel().getColumn(COLOR_COLUMN).setMinWidth(250);
      table.getColumnModel().getColumn(IMAGE_COLUMN).setMinWidth(100);
      
      final TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
      table.setRowSorter(sorter);
      sorter.setComparator(COLOR_COLUMN, new Comparator<Color>()
         {
            public int compare(Color c1, Color c2)
            {
               int d = c1.getBlue() - c2.getBlue();
               if (d != 0) return d;
               d = c1.getGreen() - c2.getGreen();
               if (d != 0) return d;               
               return c1.getRed() - c2.getRed();
            }
         });
      sorter.setSortable(IMAGE_COLUMN, false);
      add(new JScrollPane(table), BorderLayout.CENTER);

      removedRowIndices = new HashSet<>();
      removedColumns = new ArrayList<>();

      final RowFilter<TableModel, Integer> filter = new RowFilter<TableModel, Integer>()
      {
         public boolean include(Entry<? extends TableModel, ? extends Integer> entry)
         {
            return !removedRowIndices.contains(entry.getIdentifier());
         } 
      };
      
      // create menu

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu selectionMenu = new JMenu("Selection");
      menuBar.add(selectionMenu);

      rowsItem = new JCheckBoxMenuItem("Rows");
      columnsItem = new JCheckBoxMenuItem("Columns");
      cellsItem = new JCheckBoxMenuItem("Cells");

      rowsItem.setSelected(table.getRowSelectionAllowed());
      columnsItem.setSelected(table.getColumnSelectionAllowed());
      cellsItem.setSelected(table.getCellSelectionEnabled());

      rowsItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               table.clearSelection();
               table.setRowSelectionAllowed(rowsItem.isSelected());
               updateCheckboxMenuItems();
            }
         });
      selectionMenu.add(rowsItem);

      columnsItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               table.clearSelection();
               table.setColumnSelectionAllowed(columnsItem.isSelected());
               updateCheckboxMenuItems();
            }
         });
      selectionMenu.add(columnsItem);

      cellsItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               table.clearSelection();
               table.setCellSelectionEnabled(cellsItem.isSelected());
               updateCheckboxMenuItems();
            }
         });
      selectionMenu.add(cellsItem);

      JMenu tableMenu = new JMenu("Edit");
      menuBar.add(tableMenu);

      JMenuItem hideColumnsItem = new JMenuItem("Hide Columns");
      hideColumnsItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               int[] selected = table.getSelectedColumns();
               TableColumnModel columnModel = table.getColumnModel();

               // remove columns from view, starting at the last
               // index so that column numbers aren't affected

               for (int i = selected.length - 1; i >= 0; i--)
               {
                  TableColumn column = columnModel.getColumn(selected[i]);
                  table.removeColumn(column);

                  // store removed columns for "show columns" command

                  removedColumns.add(column);
               }
            }
         });
      tableMenu.add(hideColumnsItem);

      JMenuItem showColumnsItem = new JMenuItem("Show Columns");
      showColumnsItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               // restore all removed columns
               for (TableColumn tc : removedColumns)
                  table.addColumn(tc);
               removedColumns.clear();
            }
         });
      tableMenu.add(showColumnsItem);
      
      JMenuItem hideRowsItem = new JMenuItem("Hide Rows");
      hideRowsItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               int[] selected = table.getSelectedRows();
               for (int i : selected)
                  removedRowIndices.add(table.convertRowIndexToModel(i));
               sorter.setRowFilter(filter);
            }
         });
      tableMenu.add(hideRowsItem);

      JMenuItem showRowsItem = new JMenuItem("Show Rows");
      showRowsItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               removedRowIndices.clear();
               sorter.setRowFilter(filter);
            }
         });
      tableMenu.add(showRowsItem);
      
      JMenuItem printSelectionItem = new JMenuItem("Print Selection");
      printSelectionItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               int[] selected = table.getSelectedRows();
               System.out.println("Selected rows: " + Arrays.toString(selected));
               selected = table.getSelectedColumns();
               System.out.println("Selected columns: " + Arrays.toString(selected));
            }
         });
      tableMenu.add(printSelectionItem);      
   }

   private void updateCheckboxMenuItems()
   {
      rowsItem.setSelected(table.getRowSelectionAllowed());
      columnsItem.setSelected(table.getColumnSelectionAllowed());
      cellsItem.setSelected(table.getCellSelectionEnabled());
   }
}