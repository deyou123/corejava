package resources;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import javax.swing.*;

/**
 * @version 1.5 2018-03-15
 * @author Cay Horstmann
 */
public class ResourceTest
{
   public static void main(String[] args) throws IOException
   {
      Class cl = ResourceTest.class;
      URL aboutURL = cl.getResource("about.gif");
      var icon = new ImageIcon(aboutURL);

      InputStream stream = cl.getResourceAsStream("data/about.txt");
      var about = new String(stream.readAllBytes(), "UTF-8");

      InputStream stream2 = cl.getResourceAsStream("/corejava/title.txt");      
      var title = new String(stream2.readAllBytes(), StandardCharsets.UTF_8).trim();

      JOptionPane.showMessageDialog(null, about, title, JOptionPane.INFORMATION_MESSAGE, icon);
   }
}
