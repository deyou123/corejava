/**
   @version 1.1 2004-08-22
   @author Cay Horstmann
*/

import com.sun.rowset.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.sql.rowset.*;

/**
   This program shows how to display the result of a 
   database query in a table.
*/
public class ResultSetTable
{  
   public static void main(String[] args)
   {  
      JFrame frame = new ResultSetFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame contains a combo box to select a database table
   and a table to show the data stored in the table
*/
class ResultSetFrame extends JFrame
{  
   public ResultSetFrame()
   {  
      setTitle("ResultSet");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      /* find all tables in the database and add them to
         a combo box
      */

      tableNames = new JComboBox();
      tableNames.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               try
               {  
                  if (scrollPane != null) remove(scrollPane);
                  String tableName = (String) tableNames.getSelectedItem();
                  if (rs != null) rs.close();
                  String query = "SELECT * FROM " + tableName;
                  rs = stat.executeQuery(query);
                  if (scrolling)
                     model = new ResultSetTableModel(rs);
                  else
                  {
                     CachedRowSet crs = new CachedRowSetImpl();
                     crs.populate(rs);
                     model = new ResultSetTableModel(crs);
                  }

                  JTable table = new JTable(model);
                  scrollPane = new JScrollPane(table);
                  add(scrollPane, BorderLayout.CENTER);
                  validate();
               }            
               catch (SQLException e)
               {  
                  e.printStackTrace();
               }
            }
         });
      JPanel p = new JPanel();
      p.add(tableNames);
      add(p, BorderLayout.NORTH);

      try
      {  
         conn = getConnection();
         DatabaseMetaData meta = conn.getMetaData();
         if (meta.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE))
         {
            scrolling = true;
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
               ResultSet.CONCUR_READ_ONLY);
         }
         else
         {
            stat = conn.createStatement();
            scrolling = false;
         }
         ResultSet tables = meta.getTables(null, null, null, new String[] { "TABLE" });
         while (tables.next())
            tableNames.addItem(tables.getString(3));
         tables.close();
      }
      catch (IOException e)
      {  
         e.printStackTrace();
      }
      catch (SQLException e)
      {  
         e.printStackTrace();
      }

      addWindowListener(new
         WindowAdapter()
         {
            public void windowClosing(WindowEvent event)
            {
               try
               {
                  if (conn != null) conn.close();
               }
               catch (SQLException e)
               {
                  e.printStackTrace();
               }              
            }
         });
   }

   /**
      Gets a connection from the properties specified in
      the file database.properties.
      @return the database connection
    */
   public static Connection getConnection()
      throws SQLException, IOException
   {  
      Properties props = new Properties();
      FileInputStream in = new FileInputStream("database.properties");
      props.load(in);
      in.close();

      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null) System.setProperty("jdbc.drivers", drivers);
      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");

      return DriverManager.getConnection(url, username, password);
   }

   private JScrollPane scrollPane;
   private ResultSetTableModel model;
   private JComboBox tableNames;
   private ResultSet rs;
   private Connection conn;
   private Statement stat;
   private boolean scrolling;

   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 300;
}

/** 
   This class is the superclass for the scrolling and the
   caching result set table model. It stores the result set
   and its metadata.
*/
class ResultSetTableModel extends AbstractTableModel
{  
   /**
      Constructs the table model.
      @param aResultSet the result set to display.
   */
   public ResultSetTableModel(ResultSet aResultSet)
   {  
      rs = aResultSet;
      try
      {  
         rsmd = rs.getMetaData();
      }
      catch (SQLException e)
      {  
         e.printStackTrace();
      }
   }

   public String getColumnName(int c)
   {  
      try
      {  
         return rsmd.getColumnName(c + 1);
      }
      catch (SQLException e)
      {  
         e.printStackTrace();
         return "";
      }
   }

   public int getColumnCount()
   {  
      try
      {  
         return rsmd.getColumnCount();
      }
      catch (SQLException e)
      {  
         e.printStackTrace();
         return 0;
      }
   }

   public Object getValueAt(int r, int c)
   {  
      try
      {  
         rs.absolute(r + 1);
         return rs.getObject(c + 1);
      }
      catch(SQLException e)
      {  
         e.printStackTrace();
         return null;
      }
   }

   public int getRowCount()
   {  
      try
      {  
         rs.last();
         return rs.getRow();
      }
      catch(SQLException e)
      {  
         e.printStackTrace();
         return 0;
      }
   }

   private ResultSet rs;
   private ResultSetMetaData rsmd;
}
