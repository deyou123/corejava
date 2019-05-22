/**
   @version 1.22 2004-05-08
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

/**
   This program demonstrates how to customize a "Hello, World"
   program with a properties file.
*/
public class CustomWorld
{  
   public static void main(String[] args)
   {  
      CustomWorldFrame frame = new CustomWorldFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame displays a message. The frame size, message text,
   font, and color are set in a properties file.
*/
class CustomWorldFrame extends JFrame
{  
   public CustomWorldFrame()
   {  
      Properties defaultSettings = new Properties();
      defaultSettings.put("font", "Monospaced");
      defaultSettings.put("width", "300");
      defaultSettings.put("height", "200");
      defaultSettings.put("message", "Hello, World");
      defaultSettings.put("color.red", "0 50 50");
      defaultSettings.put("color.green", "50");
      defaultSettings.put("color.blue", "50");
      defaultSettings.put("ptsize", "12");

      Properties settings = new Properties(defaultSettings);
      try
      {  
         FileInputStream in = new FileInputStream("CustomWorld.properties");
         settings.load(in);
      }
      catch (IOException e) 
      {
         e.printStackTrace();
      }

      int red = Integer.parseInt(settings.getProperty("color.red"));
      int green = Integer.parseInt(settings.getProperty("color.green"));
      int blue = Integer.parseInt(settings.getProperty("color.blue"));

      Color foreground = new Color(red, green, blue);

      String name = settings.getProperty("font");
      int ptsize = Integer.parseInt(settings.getProperty("ptsize"));
      Font f = new Font(name, Font.BOLD, ptsize);

      int hsize = Integer.parseInt(settings.getProperty("width"));
      int vsize = Integer.parseInt(settings.getProperty("height"));
      setSize(hsize, vsize);
      setTitle(settings.getProperty("message"));

      JLabel label = new JLabel(settings.getProperty("message"), SwingConstants.CENTER);
      label.setFont(f);
      label.setForeground(foreground);
      add(label);
   }
}

