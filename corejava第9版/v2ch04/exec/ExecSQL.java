package exec;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.sql.*;

/**
 * Executes all SQL statements in a file. Call this program as <br>
 * java -classpath driverPath:. ExecSQL commandFile
 * 
 * @version 1.31 2012-06-05
 * @author Cay Horstmann
 */
class ExecSQL
{
   public static void main(String args[]) throws IOException
   {
      try
      {
         Scanner in = args.length == 0 ? new Scanner(System.in) : new Scanner(Paths.get(args[0]));

         try (Connection conn = getConnection())
         {
            Statement stat = conn.createStatement();

            while (true)
            {
               if (args.length == 0) System.out.println("Enter command or EXIT to exit:");

               if (!in.hasNextLine()) return;

               String line = in.nextLine();
               if (line.equalsIgnoreCase("EXIT")) return;
               if (line.trim().endsWith(";")) // remove trailing semicolon
               {
                  line = line.trim();
                  line = line.substring(0, line.length() - 1);
               }
               try
               {
                  boolean isResult = stat.execute(line);
                  if (isResult)
                  {
                     ResultSet rs = stat.getResultSet();
                     showResultSet(rs);
                  }
                  else
                  {
                     int updateCount = stat.getUpdateCount();
                     System.out.println(updateCount + " rows updated");
                  }
               }
               catch (SQLException ex)
               {
                  for (Throwable e : ex)
                     e.printStackTrace();
               }
            }
         }
      }
      catch (SQLException e)
      {
         for (Throwable t : e)
            t.printStackTrace();
      }
   }

   /**
    * Gets a connection from the properties specified in the file database.properties
    * @return the database connection
    */
   public static Connection getConnection() throws SQLException, IOException
   {
      Properties props = new Properties();
      try (InputStream in = Files.newInputStream(Paths.get("database.properties")))
      {
         props.load(in);
      }

      String drivers = props.getProperty("jdbc.drivers");
      if (drivers != null) System.setProperty("jdbc.drivers", drivers);

      String url = props.getProperty("jdbc.url");
      String username = props.getProperty("jdbc.username");
      String password = props.getProperty("jdbc.password");

      return DriverManager.getConnection(url, username, password);
   }

   /**
    * Prints a result set.
    * @param result the result set to be printed
    */
   public static void showResultSet(ResultSet result) throws SQLException
   {
      ResultSetMetaData metaData = result.getMetaData();
      int columnCount = metaData.getColumnCount();

      for (int i = 1; i <= columnCount; i++)
      {
         if (i > 1) System.out.print(", ");
         System.out.print(metaData.getColumnLabel(i));
      }
      System.out.println();

      while (result.next())
      {
         for (int i = 1; i <= columnCount; i++)
         {
            if (i > 1) System.out.print(", ");
            System.out.print(result.getString(i));
         }
         System.out.println();
      }
   }
}
