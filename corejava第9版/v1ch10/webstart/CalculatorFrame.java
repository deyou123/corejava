package webstart;

import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import javax.jnlp.*;
import javax.swing.*;

/**
 * A frame with a calculator panel and a menu to load and save the calculator history.
 */
public class CalculatorFrame extends JFrame
{
   private CalculatorPanel panel;

   public CalculatorFrame()
   {
      setTitle();
      panel = new CalculatorPanel();
      add(panel);

      JMenu fileMenu = new JMenu("File");
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);
      setJMenuBar(menuBar);

      JMenuItem openItem = fileMenu.add("Open");
      openItem.addActionListener(EventHandler.create(ActionListener.class, this, "open"));
      JMenuItem saveItem = fileMenu.add("Save");
      saveItem.addActionListener(EventHandler.create(ActionListener.class, this, "save"));
      
      pack();
   }

   /**
    * Gets the title from the persistent store or asks the user for the title if there is no prior
    * entry.
    */
   public void setTitle()
   {
      try
      {
         String title = null;

         BasicService basic = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
         URL codeBase = basic.getCodeBase();

         PersistenceService service = (PersistenceService) ServiceManager
               .lookup("javax.jnlp.PersistenceService");
         URL key = new URL(codeBase, "title");

         try
         {
            FileContents contents = service.get(key);
            InputStream in = contents.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            title = reader.readLine();
         }
         catch (FileNotFoundException e)
         {
            title = JOptionPane.showInputDialog("Please supply a frame title:");
            if (title == null) return;

            service.create(key, 100);
            FileContents contents = service.get(key);
            OutputStream out = contents.getOutputStream(true);
            PrintStream printOut = new PrintStream(out);
            printOut.print(title);
         }
         setTitle(title);
      }
      catch (UnavailableServiceException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (MalformedURLException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   /**
    * Opens a history file and updates the display.
    */
   public void open()
   {
      try
      {
         FileOpenService service = (FileOpenService) ServiceManager
               .lookup("javax.jnlp.FileOpenService");
         FileContents contents = service.openFileDialog(".", new String[] { "txt" });

         JOptionPane.showMessageDialog(this, contents.getName());
         if (contents != null)
         {
            InputStream in = contents.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null)
            {
               panel.append(line);
               panel.append("\n");
            }
         }
      }
      catch (UnavailableServiceException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   /**
    * Saves the calculator history to a file.
    */
   public void save()
   {
      try
      {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         PrintStream printOut = new PrintStream(out);
         printOut.print(panel.getText());
         InputStream data = new ByteArrayInputStream(out.toByteArray());
         FileSaveService service = (FileSaveService) ServiceManager
               .lookup("javax.jnlp.FileSaveService");
         service.saveFileDialog(".", new String[] { "txt" }, data, "calc.txt");
      }
      catch (UnavailableServiceException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }
}
