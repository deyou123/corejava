package properties;

import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

import javax.swing.*;

/**
 * A program to test properties. The program remembers the frame position, size, and title.
 * @version 1.00 2007-04-29
 * @author Cay Horstmann
 */
public class PropertiesTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               PropertiesFrame frame = new PropertiesFrame();
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame that restores position and size from a properties file and updates the properties upon
 * exit.
 */
class PropertiesFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;

   private File propertiesFile;
   private Properties settings;

   public PropertiesFrame()
   {
      // get position, size, title from properties

      String userDir = System.getProperty("user.home");
      File propertiesDir = new File(userDir, ".corejava");
      if (!propertiesDir.exists()) propertiesDir.mkdir();
      propertiesFile = new File(propertiesDir, "program.properties");

      Properties defaultSettings = new Properties();
      defaultSettings.put("left", "0");
      defaultSettings.put("top", "0");
      defaultSettings.put("width", "" + DEFAULT_WIDTH);
      defaultSettings.put("height", "" + DEFAULT_HEIGHT);
      defaultSettings.put("title", "");

      settings = new Properties(defaultSettings);

      if (propertiesFile.exists()) try
      {
         FileInputStream in = new FileInputStream(propertiesFile);
         settings.load(in);
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
      }

      int left = Integer.parseInt(settings.getProperty("left"));
      int top = Integer.parseInt(settings.getProperty("top"));
      int width = Integer.parseInt(settings.getProperty("width"));
      int height = Integer.parseInt(settings.getProperty("height"));
      setBounds(left, top, width, height);

      // if no title given, ask user

      String title = settings.getProperty("title");
      if (title.equals("")) title = JOptionPane.showInputDialog("Please supply a frame title:");
      if (title == null) title = "";
      setTitle(title);

      addWindowListener(new WindowAdapter()
         {
            public void windowClosing(WindowEvent event)
            {
               settings.put("left", "" + getX());
               settings.put("top", "" + getY());
               settings.put("width", "" + getWidth());
               settings.put("height", "" + getHeight());
               settings.put("title", getTitle());
               try
               {
                  FileOutputStream out = new FileOutputStream(propertiesFile);
                  settings.store(out, "Program Properties");
               }
               catch (IOException ex)
               {
                  ex.printStackTrace();
               }
               System.exit(0);
            }
         });
   }
}
