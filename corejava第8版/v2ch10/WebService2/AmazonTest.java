import com.horstmann.amazon.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.xml.ws.*;

/**
 * The client for the Amazon e-commerce test program.
 * @version 1.10 2007-10-20
 * @author Cay Horstmann
 */
public class AmazonTest
{
   public static void main(String[] args)
   {
      JFrame frame = new AmazonTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
 * A frame to select the book author and to display the server response.
 */
class AmazonTestFrame extends JFrame
{
   public AmazonTestFrame()
   {
      setTitle("AmazonTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JPanel panel = new JPanel();

      panel.add(new JLabel("Author:"));
      author = new JTextField(20);
      panel.add(author);

      JButton searchButton = new JButton("Search");
      panel.add(searchButton);
      searchButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               result.setText("Please wait...");
               new SwingWorker<Void, Void>()
                  {
                     @Override
                     protected Void doInBackground() throws Exception
                     {
                        String name = author.getText();
                        String books = searchByAuthor(name);
                        result.setText(books);
                        return null;
                     }
                  }.execute();
            }
         });

      result = new JTextArea();
      result.setLineWrap(true);
      result.setEditable(false);

      if (accessKey.equals("your key here"))
      {
         result.setText("You need to edit the Amazon access key.");
         searchButton.setEnabled(false);
      }

      add(panel, BorderLayout.NORTH);
      add(new JScrollPane(result), BorderLayout.CENTER);
   }

   /**
    * Calls the Amazon web service to find titles that match the author.
    * @param name the author name
    * @return a description of the matching titles
    */
   private String searchByAuthor(String name)
   {
      AWSECommerceService service = new AWSECommerceService();
      AWSECommerceServicePortType port = service.getPort(AWSECommerceServicePortType.class);
      ItemSearchRequest request = new ItemSearchRequest();
      request.getResponseGroup().add("ItemAttributes");
      request.setSearchIndex("Books");

      Holder<List<Items>> responseHolder = new Holder<List<Items>>();
      request.setAuthor(name);
      port.itemSearch("", accessKey, "", "", "", "", request, null, null, responseHolder);

      List<Item> response = responseHolder.value.get(0).getItem();

      StringBuilder r = new StringBuilder();
      for (Item item : response)
      {
         r.append("authors=");
         List<String> authors = item.getItemAttributes().getAuthor();
         r.append(authors);
         r.append(",title=");
         r.append(item.getItemAttributes().getTitle());
         r.append(",publisher=");
         r.append(item.getItemAttributes().getPublisher());
         r.append(",pubdate=");
         r.append(item.getItemAttributes().getPublicationDate());
         r.append("\n");
      }
      return r.toString();
   }

   private static final int DEFAULT_WIDTH = 450;
   private static final int DEFAULT_HEIGHT = 300;

   private static final String accessKey = "12Y1EEATQ8DDYJCVQYR2";

   private JTextField author;
   private JTextArea result;
}
