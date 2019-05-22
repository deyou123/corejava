/**
   @version 1.00 2004-08-10
   @author Cay Horstmann
*/

import com.sun.rowset.*;
import java.net.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.sql.*;
import javax.sql.rowset.*;

/**
   This program uses metadata to display arbitrary tables
   in a database.
*/
public class RowSetTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new RowSetFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   The frame that holds the data panel and the navigation 
   buttons.
*/
class RowSetFrame extends JFrame
{
   public RowSetFrame()
   {  
      setTitle("RowSetTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      tableNames = new JComboBox();
      tableNames.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               showTable((String) tableNames.getSelectedItem());
            }
         });
      add(tableNames, BorderLayout.NORTH);

      try
      {  
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
      previousButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               showPreviousRow();
            }
         });
      buttonPanel.add(previousButton);

      nextButton = new JButton("Next");
      nextButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               showNextRow();
            }
         });
      buttonPanel.add(nextButton);

      deleteButton = new JButton("Delete");
      deleteButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               deleteRow();
            }
         });
      buttonPanel.add(deleteButton);

      saveButton = new JButton("Save");
      saveButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               saveChanges();
            }
         });
      buttonPanel.add(saveButton);
   }
  
   /**
      Prepares the text fields for showing a new table, and
      shows the first row.
      @param tableName the name of the table to display
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
            // copy into row set
            rs = new CachedRowSetImpl();
            rs.setTableName(tableName); 
            rs.populate(result);
         }
         finally
         {
            conn.close();
         }

         if (scrollPane != null) 
            remove(scrollPane);
         dataPanel = new DataPanel(rs);
         scrollPane = new JScrollPane(dataPanel);
         add(scrollPane, BorderLayout.CENTER);
         validate();
         showNextRow();
      }
      catch (SQLException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }      
   }

   /**
      Moves to the previous table row.
   */
   public void showPreviousRow()
   {  
      try
      {  
         if (rs == null || rs.isFirst()) return;
         rs.previous();
         dataPanel.showRow(rs);
      }
      catch (SQLException e)
      {  
         System.out.println("Error " + e);
      }      
   }

   /**
      Moves to the next table row.
   */
   public void showNextRow()
   {  
      try
      {  
         if (rs == null || rs.isLast()) return;
         rs.next();
         dataPanel.showRow(rs);
      }
      catch (SQLException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }      
   }

   /**
      Deletes current table row.
   */
   public void deleteRow()
   {  
      try
      {  
         rs.deleteRow();
         if (!rs.isLast()) rs.next();
         else if (!rs.isFirst()) rs.previous();
         else rs = null;
         dataPanel.showRow(rs);
      }
      catch (SQLException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }      
   }

   /**
      Saves all changes.
   */
   public void saveChanges()
   {  
      try
      {  
         Connection conn = getConnection();
         try
         {
            rs.acceptChanges(conn);
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
   }

   /**
      Gets a connection from the properties specified
      in the file database.properties
      @return the database connection
   */
   public static Connection getConnection()
      throws SQLException, IOException
   {  
      Properties props = new Properties();
      FileInputStream in 
         = new FileInputStream("database.properties");
      props.load(in);
      in.close();

      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null) System.setProperty("jdbc.drivers", drivers);
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

   private CachedRowSet rs;
}

/**
   This panel displays the contents of a result set.
*/
class DataPanel extends JPanel
{
   /**
      Constructs the data panel.
      @param rs the result set whose contents this panel displays
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
         fields.add(tb);

         gbc.gridx = 1;
         gbc.anchor = GridBagConstraints.WEST;
         add(tb, gbc);
      }      
   }

   /**
      Shows a database row by populating all text fields
      with the column values.
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

   private ArrayList<JTextField> fields;
}
