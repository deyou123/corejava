/**
   @version 1.01 2004-05-08
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.event.*;

/**
   A program to test preference settings. The program
   remembers the frame position and size.
*/
public class PreferencesTest
{
   public static void main(String[] args)
   {
      PreferencesFrame frame = new PreferencesFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame that restores position and size from user
   preferences and updates the preferences upon exit.
*/
class PreferencesFrame extends JFrame
{
   public PreferencesFrame()
   {
      setTitle("PreferencesTest");

      // get position, size from preferences

      Preferences root = Preferences.userRoot();
      final Preferences node = root.node("/com/horstmann/corejava");
      int left = node.getInt("left", 0);
      int top = node.getInt("top", 0);
      int width = node.getInt("width", DEFAULT_WIDTH);
      int height = node.getInt("height", DEFAULT_HEIGHT);
      setBounds(left, top, width, height);

      // set up file chooser that shows XML files

      final JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      // accept all files ending with .xml
      chooser.setFileFilter(new
         javax.swing.filechooser.FileFilter()
         {
            public boolean accept(File f)
            {
               return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
            }
            public String getDescription()
            {
               return "XML files";
            }
         });

      
      // set up menus
      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      JMenu menu = new JMenu("File");
      menuBar.add(menu);

      JMenuItem exportItem = new JMenuItem("Export preferences");
      menu.add(exportItem);
      exportItem.addActionListener(new 
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               if(chooser.showSaveDialog(PreferencesFrame.this) == JFileChooser.APPROVE_OPTION)
               {
                  try
                  {
                     OutputStream out = new FileOutputStream(chooser.getSelectedFile());
                     node.exportSubtree(out);
                     out.close();
                  }
                  catch (Exception e)
                  {
                     e.printStackTrace();
                  }
               }
            }
         });

      JMenuItem importItem = new JMenuItem("Import preferences");
      menu.add(importItem);
      importItem.addActionListener(new 
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               if(chooser.showOpenDialog(PreferencesFrame.this) == JFileChooser.APPROVE_OPTION)
               {
                  try
                  {
                     InputStream in = new FileInputStream(chooser.getSelectedFile());
                     node.importPreferences(in);
                     in.close();
                  }
                  catch (Exception e)
                  {
                     e.printStackTrace();
                  }
               }
            }
         });

      JMenuItem exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               node.putInt("left", getX());
               node.putInt("top", getY());
               node.putInt("width", getWidth());
               node.putInt("height", getHeight());
               System.exit(0);
            }
         });
   }
   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;  
}
