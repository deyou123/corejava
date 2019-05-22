package signed;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;

/**
 * This applet can run "outside the sandbox" and read local files when it is given the right
 * permissions.
 * @version 1.13 2016-05-10
 * @author Cay Horstmann
 */
public class FileReadApplet extends JApplet
{
   private JTextField fileNameField;
   private JTextArea fileText;

   public void init()
   {
      EventQueue.invokeLater(() ->
         {
            fileNameField = new JTextField(20);
            JPanel panel = new JPanel();
            panel.add(new JLabel("File name:"));
            panel.add(fileNameField);
            JButton openButton = new JButton("Open");
            panel.add(openButton);
            ActionListener listener = event -> loadFile(fileNameField.getText());
            fileNameField.addActionListener(listener);
            openButton.addActionListener(listener);
            add(panel, "North");
            fileText = new JTextArea();
            add(new JScrollPane(fileText), "Center");
         });
   }

   /**
    * Loads the contents of a file into the text area.
    * @param filename the file name
    */
   public void loadFile(String filename)
   {
      fileText.setText("");
      try 
      {
         fileText.append(new String(Files.readAllBytes(Paths.get(filename))));            
      }
      catch (IOException ex)
      {
         fileText.append(ex + "\n");
      }
      catch (SecurityException ex)
      {
         fileText.append("I am sorry, but I cannot do that.\n");
         fileText.append(ex + "\n");
         ex.printStackTrace();
      }
   }
}
