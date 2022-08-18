package properties;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import javax.swing.*;

/**
 * A program to test properties. The program remembers the frame position, size,
 * and last selected file.
 * @version 1.10 2018-03-15
 * @author Cay Horstmann
 */
public class ImageViewer
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> {
         var frame = new ImageViewerFrame();
         frame.setTitle("ImageViewer");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
      });
   }
}

/**
 * An image viewer that restores position and size from a properties file
 * and updates the properties upon exit.
 */
class ImageViewerFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;

   private File propertiesFile;
   private Properties settings;
   private String image;
   private JLabel label = new JLabel();

   public ImageViewerFrame()
   {
      // get position, size, title from properties

      String userDir = System.getProperty("user.home");
      var propertiesDir = new File(userDir, ".corejava");
      if (!propertiesDir.exists()) propertiesDir.mkdir();
      propertiesFile = new File(propertiesDir, "ImageViewer.properties");

      var defaultSettings = new Properties();
      defaultSettings.setProperty("left", "0");
      defaultSettings.setProperty("top", "0");
      defaultSettings.setProperty("width", "" + DEFAULT_WIDTH);
      defaultSettings.setProperty("height", "" + DEFAULT_HEIGHT);
      defaultSettings.setProperty("title", "");

      settings = new Properties(defaultSettings);

      if (propertiesFile.exists()) 
         try (var in = new FileInputStream(propertiesFile))
         {         
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
      image = settings.getProperty("image");
      if (image != null) label.setIcon(new ImageIcon(image));

      addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent event)
         {
            settings.setProperty("left", "" + (int) getX());
            settings.setProperty("top", "" + (int) getY());
            settings.setProperty("width", "" + (int) getWidth());
            settings.setProperty("height", "" + (int) getHeight());
            if (image != null)
               settings.setProperty("image", image);
            try (var out = new FileWriter(propertiesFile, StandardCharsets.UTF_8))
            {
               settings.store(out, "Program Properties");
            }
            catch (IOException ex)
            {
               ex.printStackTrace();
            }
            System.exit(0);
         }
      });

      // use a label to display the images
      add(label);

      // set up the file chooser
      var chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      // set up the menu bar
      var menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      var menu = new JMenu("File");
      menuBar.add(menu);

      var openItem = new JMenuItem("Open");
      menu.add(openItem);
      openItem.addActionListener(event -> {
         // show file chooser dialog
            int result = chooser.showOpenDialog(null);

            // if file selected, set it as icon of the label
            if (result == JFileChooser.APPROVE_OPTION)
            {
               image = chooser.getSelectedFile().getPath();
               label.setIcon(new ImageIcon(image));
            }
         });

      var exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(event -> System.exit(0));
   }
}
