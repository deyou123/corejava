import com.sun.rowset.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.sql.*;
import javax.sql.rowset.*;

/**
 * This program uses metadata to display arbitrary tables in a database.
 * @version 1.31 2007-06-28
 * @author Cay Horstmann
 */
public class ViewDB
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ViewDBFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * The frame that holds the data panel and the navigation buttons.
 */
class ViewDBFrame extends JFrame
{
   public ViewDBFrame()
   {
      setTitle("ViewDB");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      tableNames = new JComboBox();
      tableNames.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               showTable((String) tableNames.getSelectedItem());
            }
         });
      add(tableNames, BorderLayout.NORTH);

      try
      {
         readDatabaseProperties();
         Connection conn = getConnection();
         try
         {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet mrs = meta.getTables(null, null, null, new String[] { "TABLE" });
            while (mrs.next())
               tableNames.addItem(mrs.getString(3));
         }
         finally
         {
            conn.close();
         }
      }
      catch (SQLException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }

      JPanel buttonPanel = new JPanel();
      add(buttonPanel, BorderLayout.SOUTH);

      previousButton = new JButton("Previous");
      previousButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               showPreviousRow();
            }
         });
      buttonPanel.add(previousButton);

      nextButton = new JButton("Next");
      nextButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               showNextRow();
            }
         });
      buttonPanel.add(nextButton);

      deleteButton = new JButton("Delete");
      deleteButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               deleteRow();
            }
         });
      buttonPanel.add(deleteButton);

      saveButton = new JButton("Save");
      saveButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               saveChanges();
            }
         });
      buttonPanel.add(saveButton);
   }

   /**
    * Prepares the text fields for showing a new table, and shows the first row.
    * @param tableName the name of the table to display
    */
   public void showTable(String tableName)
   {
      try
      {
         // open connection
         Connection conn = getConnection();
         try
         {
            // get result set
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM " + tableName);
            // copy into cached row set
            crs = new CachedRowSetImpl();
            crs.setTableName(tableName);
            crs.populate(result);            
         }
         finally
         {
            conn.close();
         }

         if (scrollPane != null) remove(scrollPane);
         dataPanel = new DataPanel(crs);
         scrollPane = new JScrollPane(dataPanel);
         add(scrollPane, BorderLayout.CENTER);
         validate();
         showNextRow();
      }
      catch (SQLException e)
      {
         JOptionPane.showMessageDialog(this, e);
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
      catch (SQLException e)
      {
         for (Throwable t : e)
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
      catch (SQLException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   /**
    * Deletes current table row.
    */
   public void deleteRow()
   {
      try
      {
         Connection conn = getConnection();
         try
         {
            crs.deleteRow();
            crs.acceptChanges(conn);
            if (!crs.isLast()) crs.next();
            else if (!crs.isFirst()) crs.previous();
            else crs = null;
            dataPanel.showRow(crs);
         }
         finally
         {
            conn.close();
         }
      }
      catch (SQLException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   /**
    * Saves all changes.
    */
   public void saveChanges()
   {
      try
      {
         Connection conn = getConnection();
         try
         {
            dataPanel.setRow(crs);
            crs.acceptChanges(conn);
         }
         finally
         {
            conn.close();
         }
      }
      catch (SQLException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   private void readDatabaseProperties() throws IOException
   {
      props = new Properties();
      FileInputStream in = new FileInputStream("database.properties");
      props.load(in);
      in.close();
      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null) System.setProperty("jdbc.drivers", drivers);      
   }
   
   /**
    * Gets a connection from the properties specified in the file database.properties
    * @return the database connection
    */
   private Connection getConnection() throws SQLException
   {
      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");

      return DriverManager.getConnection(url, username, password);
   }

   public static final int DEFAULT_WIDTH = 400;
   public static final int DEFAULT_HEIGHT = 200;

   private JButton previousButton;
   private JButton nextButton;
   private JButton deleteButton;
   private JButton saveButton;
   private DataPanel dataPanel;
   private Component scrollPane;
   private JComboBox tableNames;
   private Properties props;
   private CachedRowSet crs;
}

/**
 * This panel displays the contents of a result set.
 */
class DataPanel extends JPanel
{
   /**
    * Constructs the data panel.
    * @param rs the result set whose contents this panel displays
    */
   public DataPanel(RowSet rs) throws SQLException
   {
      fields = new ArrayList<JTextField>();
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
   public void showRow(ResultSet rs) throws SQLException
   {
      for (int i = 1; i <= fields.size(); i++)
      {
         String field = rs.getString(i);
         JTextField tb = (JTextField) fields.get(i - 1);
         tb.setText(field);
      }
   }
   
   /**
    * Updates changed data into the current row of the row set
    */
   public void setRow(RowSet rs) throws SQLException
   {
      for (int i = 1; i <= fields.size(); i++)
      {
         String field = rs.getString(i);
         JTextField tb = (JTextField) fields.get(i - 1);
         if (!field.equals(tb.getText()))
            rs.updateString(i, tb.getText());
      }
      rs.updateRow();
   }

   private ArrayList<JTextField> fields;
}