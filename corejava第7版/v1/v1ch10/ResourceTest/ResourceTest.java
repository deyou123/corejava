/**
   @version 1.31 2004-05-07
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class ResourceTest
{  
   public static void main(String[] args)
   {  
      ResourceTestFrame frame = new ResourceTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame with a panel that has components demonstrating
   resource access from a JAR file.
*/
class ResourceTestFrame extends JFrame 
{  
   public ResourceTestFrame()
   {  
      setTitle("ResourceTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      add(new AboutPanel());
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 300;  
}

/**
   A panel with a text area and an "About" button. Pressing
   the button fills the text area with text from a resource.
*/
class AboutPanel extends JPanel
{  
   public AboutPanel()
   {  
      setLayout(new BorderLayout());

      // add text area
      textArea = new JTextArea();
      add(new JScrollPane(textArea), BorderLayout.CENTER);

      // add About button
      URL aboutURL = AboutPanel.class.getResource("about.gif");
      JButton aboutButton = new JButton("About", new ImageIcon(aboutURL));
      aboutButton.addActionListener(new AboutAction());
      add(aboutButton, BorderLayout.SOUTH);
   }

   private JTextArea textArea;

   private class AboutAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         InputStream stream = AboutPanel.class.getResourceAsStream("about.txt");
         Scanner in = new Scanner(stream);
         while (in.hasNext())
            textArea.append(in.nextLine() + "\n");
      } 
   }
}

