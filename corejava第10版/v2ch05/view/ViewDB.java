package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

import javax.sql.*;
import javax.sql.rowset.*;
import javax.swing.*;

/**
 * This program uses metadata to display arbitrary tables in a database.
 * @version 1.33 2016-04-27
 * @author Cay Horstmann
 */
public class ViewDB
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new ViewDBFrame();
            frame.setTitle("ViewDB");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * The frame that holds the data panel and the navigation buttons.
 */
class ViewDBFrame extends JFrame
{
   private JButton previousButton;
   private JButton nextButton;
   private JButton deleteButton;
   private JButton saveButton;
   private DataPanel dataPanel;
   private Component scrollPane;
   private JComboBox<String> tableNames;
   private Properties props;
   private CachedRowSet crs;
   private Connection conn;

   public ViewDBFrame()
   {
      tableNames = new JComboBox<String>();

      try
      {
         readDatabaseProperties();
         conn = getConnection();
         DatabaseMetaData meta = conn.getMetaData();
         try (ResultSet mrs = meta.getTables(null, null, null, new String[] { "TABLE" }))
         {
            while (mrs.next())
               tableNames.addItem(mrs.getString(3));
         }
      }
      catch (SQLException ex)
      {
         for (Throwable t : ex)
            t.printStackTrace();
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
      }

      tableNames.addActionListener(
         event -> showTable((String) tableNames.getSelectedItem(), conn));
      add(tableNames, BorderLayout.NORTH);
      addWindowListener(new WindowAdapter()
         {
            public void windowClosing(WindowEvent event)
            {
               try
               {
                  if (conn != null) conn.close();
               }
               catch (SQLException ex)
               {
                  for (Throwable t : ex)
                     t.printStackTrace();
               }               
            }
         });

      JPanel buttonPanel = new JPanel();
      add(buttonPanel, BorderLayout.SOUTH);

      previousButton = new JButton("Previous");
      previousButton.addActionListener(event -> showPreviousRow());
      buttonPanel.add(previousButton);

      nextButton = new JButton("Next");
      nextButton.addActionListener(event -> showNextRow());
      buttonPanel.add(nextButton);

      deleteButton = new JButton("Delete");
      deleteButton.addActionListener(event -> deleteRow());
      buttonPanel.add(deleteButton);

      saveButton = new JButton("Save");
      saveButton.addActionListener(event -> saveChanges());
      buttonPanel.add(saveButton);
      if (tableNames.getItemCount() > 0)
         showTable(tableNames.getItemAt(0), conn);
   }

   /**
    * Prepares the text fields for showing a new table, and shows the first row.
    * @param tableName the name of the table to display
    * @param conn the database connection
    */
   public void showTable(String tableName, Connection conn)
   {
      try (Statement stat = conn.createStatement();
         ResultSet result = stat.executeQuery("SELECT * FROM " + tableName))
      {
         // get result set
         
         // copy into cached row set
         RowSetFactory factory = RowSetProvider.newFactory();            
         crs = factory.createCachedRowSet();
         crs.setTableName(tableName);
         crs.populate(result);            

         if (scrollPane != null) remove(scrollPane);
         dataPanel = new DataPanel(crs);
         scrollPane = new JScrollPane(dataPanel);
         add(scrollPane, BorderLayout.CENTER);
         pack();
         showNextRow();
      }
      catch (SQLException ex)
      {
         for (Throwable t : ex)
            t.printStackTrace();
      }
   }

   /**
    * Moves to the previous table row.
    */
   public void showPreviousRow()
   {
      try
      {
         if (crs == null || crs.isFirst()) return;
         crs.previous();
         dataPanel.showRow(crs);
      }
      catch (SQLException ex)
      {
         for (Throwable t : ex)
            t.printStackTrace();
      }
   }

   /**
    * Moves to the next table row.
    */
   public void showNextRow()
   {
      try
      {
         if (crs == null || crs.isLast()) return;
         crs.next();
         dataPanel.showRow(crs);
      }
      catch (SQLException ex)
      {
         for (Throwable t : ex)
            t.printStackTrace();
      }
   }

   /**
    * Deletes current table row.
    */
   public void deleteRow()
   {
      if (crs == null) return;
      new SwingWorker<Void, Void>()
      {
         public Void doInBackground() throws SQLException
         {
            crs.deleteRow();
            crs.acceptChanges(conn);               
            if (crs.isAfterLast()) 
               if (!crs.last()) crs = null;
            return null;
         }
         public void done()
         {
            dataPanel.showRow(crs);
         }
      }.execute();
   }
   /**
    * Saves all changes.
    */
   public void saveChanges()
   {
      if (crs == null) return;
      new SwingWorker<Void, Void>()
      {
         public Void doInBackground() throws SQLException
         {
            dataPanel.setRow(crs);
            crs.acceptChanges(conn);
            return null;
         }
      }.execute();      
   }

   private void readDatabaseProperties() throws IOException
   {
      props = new Properties();
      try (InputStream in = Files.newInputStream(Paths.get("database.properties")))
      {
         props.load(in);
      }
      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null) System.setProperty("jdbc.drivers", drivers);
   }
   
   /**
    * Gets a connection from the properties specified in the file database.properties.
    * @return the database connection
    */
   private Connection getConnection() throws SQLException
   {
      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");

      return DriverManager.getConnection(url, username, password);
   }
}

/**
 * This panel displays the contents of a result set.
 */
class DataPanel extends JPanel
{
   private java.util.List<JTextField> fields;

   /**
    * Constructs the data panel.
    * @param rs the result set whose contents this panel displays
    */
   public DataPanel(RowSet rs) throws SQLException
   {
      fields = new ArrayList<>();
      setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridwidth = 1;
      gbc.gridheight = 1;

      ResultSetMetaData rsmd = rs.getMetaData();
      for (int i = 1; i <= rsmd.getColumnCount(); i++)
      {
         gbc.gridy = i - 1;

         String columnName = rsmd.getColumnLabel(i);
         gbc.gridx = 0;
         gbc.anchor = GridBagConstraints.EAST;
         add(new JLabel(columnName), gbc);

         int columnWidth = rsmd.getColumnDisplaySize(i);
         JTextField tb = new JTextField(columnWidth);
         if (!rsmd.getColumnClassName(i).equals("java.lang.String"))
            tb.setEditable(false);
               
         fields.add(tb);

         gbc.gridx = 1;
         gbc.anchor = GridBagConstraints.WEST;
         add(tb, gbc);
      }
   }

   /**
    * Shows a database row by populating all text fields with the column values.
    */
   public void showRow(ResultSet rs)
   {
      try
      {
         if (rs == null) return;
         for (int i = 1; i <= fields.size(); i++)
         {
            String field = rs == null ? "" : rs.getString(i);
            JTextField tb = fields.get(i - 1);
            tb.setText(field);
         }
      }
      catch (SQLException ex)
      {
         for (Throwable t : ex)
            t.printStackTrace();
      }
   }
   
   /**
    * Updates changed data into the current row of the row set.
    */
   public void setRow(RowSet rs) throws SQLException
   {
      for (int i = 1; i <= fields.size(); i++)
      {
         String field = rs.getString(i);
         JTextField tb = fields.get(i - 1);
         if (!field.equals(tb.getText()))
            rs.updateString(i, tb.getText());
      }
      rs.updateRow();
   }
}
