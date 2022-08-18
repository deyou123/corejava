package preferences;

import java.awt.EventQueue;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.*;
import javax.swing.*;

/**
 * A program to test preference settings. The program remembers the 
 * frame position, size, and last selected file.
 * @version 1.10 2018-04-10
 * @author Cay Horstmann
 */
public class ImageViewer
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            var frame = new ImageViewerFrame();
            frame.setTitle("ImageViewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * An image viewer that restores position, size, and image from user 
 * preferences and updates the preferences upon exit.
 */
class ImageViewerFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;
   private String image;
   
   public ImageViewerFrame()
   {
      Preferences root = Preferences.userRoot();
      Preferences node = root.node("/com/horstmann/corejava/ImageViewer");
      // get position, size, title from properties
      int left = node.getInt("left", 0);
      int top = node.getInt("top", 0);
      int width = node.getInt("width", DEFAULT_WIDTH);
      int height = node.getInt("height", DEFAULT_HEIGHT);
      setBounds(left, top, width, height);
      image = node.get("image", null);
      var label = new JLabel();
      if (image != null) label.setIcon(new ImageIcon(image));

      addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent event)
         {
            node.putInt("left", getX());
            node.putInt("top", getY());
            node.putInt("width", getWidth());
            node.putInt("height", getHeight());
            if (image != null) node.put("image", image);      
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
      openItem.addActionListener(event ->
         {
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
