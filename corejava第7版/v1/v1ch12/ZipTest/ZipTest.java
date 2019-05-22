/**
   @version 1.31 2004-05-11
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class ZipTest
{  
   public static void main(String[] args)
   {  
      ZipTestFrame frame = new ZipTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame with a text area to show the contents of a file inside
   a zip archive, a combo box to select different files in the
   archive, and a menu to load a new archive.
*/
class ZipTestFrame extends JFrame
{
   public ZipTestFrame()
   {
      setTitle("ZipTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add the menu and the Open and Exit menu items
      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu("File");

      JMenuItem openItem = new JMenuItem("Open");
      menu.add(openItem);
      openItem.addActionListener(new OpenAction());

      JMenuItem exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(new 
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });

      menuBar.add(menu);
      setJMenuBar(menuBar);

      // add the text area and combo box
      fileText = new JTextArea();
      fileCombo = new JComboBox();
      fileCombo.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               loadZipFile((String) fileCombo.getSelectedItem());
            }
         });

      add(fileCombo, BorderLayout.SOUTH);
      add(new JScrollPane(fileText), BorderLayout.CENTER);
   }

   /**
      This is the listener for the File->Open menu item.
   */
   private class OpenAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         // prompt the user for a zip file
         JFileChooser chooser = new JFileChooser();
         chooser.setCurrentDirectory(new File("."));
         ExtensionFileFilter filter = new ExtensionFileFilter();
         filter.addExtension(".zip");
         filter.addExtension(".jar");
         filter.setDescription("ZIP archives");
         chooser.setFileFilter(filter);
         int r = chooser.showOpenDialog(ZipTestFrame.this);
         if (r == JFileChooser.APPROVE_OPTION)
         {  
            zipname = chooser.getSelectedFile().getPath();
            scanZipFile();
         }  
      }
   }

   /**
      Scans the contents of the zip archive and populates
      the combo box.
   */
   public void scanZipFile()
   {  
      fileCombo.removeAllItems();
      try
      {  
         ZipInputStream zin = new ZipInputStream(new FileInputStream(zipname));
         ZipEntry entry;
         while ((entry = zin.getNextEntry()) != null)
         {  
            fileCombo.addItem(entry.getName());
            zin.closeEntry();
         }
         zin.close();
      }
      catch (IOException e)
      {  
         e.printStackTrace(); 
      }
   }

   /**
      Loads a file from the zip archive into the text area
      @param name the name of the file in the archive
   */
   public void loadZipFile(String name)
   {  
      try
      {  
         ZipInputStream zin = new ZipInputStream(new FileInputStream(zipname));
         ZipEntry entry;
         fileText.setText("");

         // find entry with matching name in archive
         while ((entry = zin.getNextEntry()) != null)
         {  
            if (entry.getName().equals(name))
            {  
               // read entry into text area
               BufferedReader in = new BufferedReader(new InputStreamReader(zin));
               String line;
               while ((line = in.readLine()) != null)
               {
                  fileText.append(line);
                  fileText.append("\n");
               }
            }
            zin.closeEntry();
         }
         zin.close();
      }
      catch (IOException e)
      {  
         e.printStackTrace(); 
      }
   }

   public static final int DEFAULT_WIDTH = 400;
   public static final int DEFAULT_HEIGHT = 300;  

   private JComboBox fileCombo;
   private JTextArea fileText;
   private String zipname;
}

/**
   This file filter matches all files with a given set of 
   extensions. From FileChooserTest in chapter 9
*/
class ExtensionFileFilter extends FileFilter
{
   /**
      Adds an extension that this file filter recognizes.
      @param extension a file extension (such as ".txt" or "txt")
   */
   public void addExtension(String extension)
   {
      if (!extension.startsWith("."))
         extension = "." + extension;
      extensions.add(extension.toLowerCase());     
   }

   /**
      Sets a description for the file set that this file filter
      recognizes.
      @param aDescription a description for the file set
   */
   public void setDescription(String aDescription)
   {
      description = aDescription;
   }

   /**
      Returns a description for the file set that this file
      filter recognizes.
      @return a description for the file set
   */
   public String getDescription()
   {
      return description; 
   }

   public boolean accept(File f)
   {
      if (f.isDirectory()) return true;
      String name = f.getName().toLowerCase();

      // check if the file name ends with any of the extensions
      for (String e : extensions)
         if (name.endsWith(e))
            return true;
      return false;
   }
   
   private String description = "";
   private ArrayList<String> extensions = new ArrayList<String>();
}

