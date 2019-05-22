import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * This program demonstrates several complex database queries.
 * @version 1.23 2007-06-28
 * @author Cay Horstmann
 */
public class QueryDB
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new QueryDBFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame displays combo boxes for query parameters, a text area for command results, and
 * buttons to launch a query and an update.
 */
class QueryDBFrame extends JFrame
{
   public QueryDBFrame()
   {
      setTitle("QueryDB");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      setLayout(new GridBagLayout());

      authors = new JComboBox();
      authors.setEditable(false);
      authors.addItem("Any");

      publishers = new JComboBox();
      publishers.setEditable(false);
      publishers.addItem("Any");

      result = new JTextArea(4, 50);
      result.setEditable(false);

      priceChange = new JTextField(8);
      priceChange.setText("-5.00");

      try
      {
         conn = getConnection();
         Statement stat = conn.createStatement();

         String query = "SELECT Name FROM Authors";
         ResultSet rs = stat.executeQuery(query);
         while (rs.next())
            authors.addItem(rs.getString(1));
         rs.close();

         query = "SELECT Name FROM Publishers";
         rs = stat.executeQuery(query);
         while (rs.next())
            publishers.addItem(rs.getString(1));
         rs.close();
         stat.close();
      }
      catch (SQLException e)
      {
         for (Throwable t : e)
            result.append(t.getMessage());
      }
      catch (IOException e)
      {
         result.setText("" + e);
      }

      // we use the GBC convenience class of Core Java Volume 1 Chapter 9
      add(authors, new GBC(0, 0, 2, 1));

      add(publishers, new GBC(2, 0, 2, 1));

      JButton queryButton = new JButton("Query");
      queryButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               executeQuery();
            }
         });
      add(queryButton, new GBC(0, 1, 1, 1).setInsets(3));

      JButton changeButton = new JButton("Change prices");
      changeButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               changePrices();
            }
         });
      add(changeButton, new GBC(2, 1, 1, 1).setInsets(3));

      add(priceChange, new GBC(3, 1, 1, 1).setFill(GBC.HORIZONTAL));

      add(new JScrollPane(result), new GBC(0, 2, 4, 1).setFill(GBC.BOTH).setWeight(100, 100));

      addWindowListener(new WindowAdapter()
         {
            public void windowClosing(WindowEvent event)
            {
               try
               {
                  if (conn != null) conn.close();
               }
               catch (SQLException e)
               {
                  for (Throwable t : e)
                     t.printStackTrace();
               }
            }
         });
   }

   /**
    * Executes the selected query.
    */
   private void executeQuery()
   {
      ResultSet rs = null;
      try
      {
         String author = (String) authors.getSelectedItem();
         String publisher = (String) publishers.getSelectedItem();
         if (!author.equals("Any") && !publisher.equals("Any"))
         {
            if (authorPublisherQueryStmt == null) authorPublisherQueryStmt = conn
                  .prepareStatement(authorPublisherQuery);
            authorPublisherQueryStmt.setString(1, author);
            authorPublisherQueryStmt.setString(2, publisher);
            rs = authorPublisherQueryStmt.executeQuery();
         }
         else if (!author.equals("Any") && publisher.equals("Any"))
         {
            if (authorQueryStmt == null) authorQueryStmt = conn.prepareStatement(authorQuery);
            authorQueryStmt.setString(1, author);
            rs = authorQueryStmt.executeQuery();
         }
         else if (author.equals("Any") && !publisher.equals("Any"))
         {
            if (publisherQueryStmt == null) publisherQueryStmt = conn
                  .prepareStatement(publisherQuery);
            publisherQueryStmt.setString(1, publisher);
            rs = publisherQueryStmt.executeQuery();
         }
         else
         {
            if (allQueryStmt == null) allQueryStmt = conn.prepareStatement(allQuery);
            rs = allQueryStmt.executeQuery();
         }

         result.setText("");
         while (rs.next())
         {
            result.append(rs.getString(1));
            result.append(", ");
            result.append(rs.getString(2));
            result.append("\n");
         }
         rs.close();
      }
      catch (SQLException e)
      {
         for (Throwable t : e)
            result.append(t.getMessage());         
      }
   }

   /**
    * Executes an update statement to change prices.
    */
   public void changePrices()
   {
      String publisher = (String) publishers.getSelectedItem();
      if (publisher.equals("Any"))
      {
         result.setText("I am sorry, but I cannot do that.");
         return;
      }
      try
      {
         if (priceUpdateStmt == null) priceUpdateStmt = conn.prepareStatement(priceUpdate);
         priceUpdateStmt.setString(1, priceChange.getText());
         priceUpdateStmt.setString(2, publisher);
         int r = priceUpdateStmt.executeUpdate();
         result.setText(r + " records updated.");
      }
      catch (SQLException e)
      {
         for (Throwable t : e)
            result.append(t.getMessage());
      }
   }

   /**
    * Gets a connection from the properties specified in the file database.properties
    * @return the database connection
    */
   public static Connection getConnection() throws SQLException, IOException
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

   public static final int DEFAULT_WIDTH = 400;
   public static final int DEFAULT_HEIGHT = 400;

   private JComboBox authors;
   private JComboBox publishers;
   private JTextField priceChange;
   private JTextArea result;
   private Connection conn;
   private PreparedStatement authorQueryStmt;
   private PreparedStatement authorPublisherQueryStmt;
   private PreparedStatement publisherQueryStmt;
   private PreparedStatement allQueryStmt;
   private PreparedStatement priceUpdateStmt;

   private static final String authorPublisherQuery = "SELECT Books.Price, Books.Title FROM Books, BooksAuthors, Authors, Publishers"
         + " WHERE Authors.Author_Id = BooksAuthors.Author_Id AND BooksAuthors.ISBN = Books.ISBN"
         + " AND Books.Publisher_Id = Publishers.Publisher_Id AND Authors.Name = ?"
         + " AND Publishers.Name = ?";

   private static final String authorQuery = "SELECT Books.Price, Books.Title FROM Books, BooksAuthors, Authors"
         + " WHERE Authors.Author_Id = BooksAuthors.Author_Id AND BooksAuthors.ISBN = Books.ISBN"
         + " AND Authors.Name = ?";

   private static final String publisherQuery = "SELECT Books.Price, Books.Title FROM Books, Publishers"
         + " WHERE Books.Publisher_Id = Publishers.Publisher_Id AND Publishers.Name = ?";

   private static final String allQuery = "SELECT Books.Price, Books.Title FROM Books";

   private static final String priceUpdate = "UPDATE Books " + "SET Price = Price + ? "
         + " WHERE Books.Publisher_Id = (SELECT Publisher_Id FROM Publishers WHERE Name = ?)";
}
