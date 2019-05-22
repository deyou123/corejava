/**
   @version 1.00 2004-08-15
   @author Cay Horstmann
*/

import com.amazon.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.util.*;
import javax.swing.*;

/**
   The client for the warehouse program. 
*/
public class SOAPTest
{  
   public static void main(String[] args)
   {
      JFrame frame = new SOAPTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame to select the book author and to display the server response.
*/
class SOAPTestFrame extends JFrame
{  
   public SOAPTestFrame()
   {  
      setTitle("SOAPTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JPanel panel = new JPanel();

      panel.add(new JLabel("Author:"));
      author = new JTextField(20);
      panel.add(author);

      JButton searchButton = new JButton("Search");
      panel.add(searchButton);
      searchButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {  
               result.setText("Please wait...");
               new Thread(new 
                  Runnable()
                  {
                     public void run()
                     {   
                        String name = author.getText();
                        String books = searchByAuthor(name);
                        result.setText(books);
                     }
                  }).start();
            }            
         });

      result = new JTextArea();
      result.setLineWrap(true);
      result.setEditable(false);

      add(panel, BorderLayout.NORTH);
      add(new JScrollPane(result), BorderLayout.CENTER);
   }

   /**
      Calls the Amazon web service to find titles that match the author.
      @param name the author name
      @return a description of the matching titles
   */
   private String searchByAuthor(String name)
   {  
      try
      {         
         AmazonSearchPort port = (AmazonSearchPort) 
            (new AmazonSearchService_Impl().getAmazonSearchPort());
         
         AuthorRequest request
            = new AuthorRequest(name, "1", "books", "", "lite", "", token, "", "", "");
         ProductInfo response = port.authorSearchRequest(request);          

         Details[] details = response.getDetails();
         StringBuilder r = new StringBuilder();
         for (Details d : details)
         {
            r.append("authors=");
            String[] authors = d.getAuthors();
            if (authors == null) r.append("[]");
            else r.append(Arrays.asList(d.getAuthors()));
            r.append(",title=");
            r.append(d.getProductName());
            r.append(",publisher=");
            r.append(d.getManufacturer());
            r.append(",pubdate=");
            r.append(d.getReleaseDate());
            r.append("\n");
         }
         return r.toString();
      } 
      catch (RemoteException e)
      {  
         return "Exception: " + e;
      }
   }

   private static final int DEFAULT_WIDTH = 450;
   private static final int DEFAULT_HEIGHT = 300;

   private static final String token = "your token goes here"; 
   
   private JTextField author;
   private JTextArea result;
}
