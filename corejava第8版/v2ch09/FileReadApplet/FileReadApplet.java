import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * This applet can run "outside the sandbox" and read local files when it is given the right
 * permissions.
 * @version 1.11 2007-10-06
 * @author Cay Horstmann
 */
public class FileReadApplet extends JApplet
{
   public void init()
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               fileNameField = new JTextField(20);
               JPanel panel = new JPanel();
               panel.add(new JLabel("File name:"));
               panel.add(fileNameField);
               JButton openButton = new JButton("Open");
               panel.add(openButton);
               ActionListener listener = new ActionListener()
               {
                  public void actionPerformed(ActionEvent event)
                  {
                     loadFile(fileNameField.getText());
                  }
               };
               fileNameField.addActionListener(listener);
               openButton.addActionListener(listener);

               add(panel, "North");

               fileText = new JTextArea();
               add(new JScrollPane(fileText), "Center");
            }
         });
   }

   /**
    * Loads the contents of a file into the text area.
    * @param filename the file name
    */
   public void loadFile(String filename)
   {
      try
      {
         fileText.setText("");
         Scanner in = new Scanner(new FileReader(filename));
         while (in.hasNextLine())
            fileText.append(in.nextLine() + "\n");
         in.close();
      }
      catch (IOException e)
      {
         fileText.append(e + "\n");
      }
      catch (SecurityException e)
      {
         fileText.append("I am sorry, but I cannot do that.\n");
         fileText.append(e + "\n");
      }
   }

   private JTextField fileNameField;
   private JTextArea fileText;
}
