package systemTray;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;

/**
 * This program demonstrates the system tray API.
 * @version 1.02 2016-05-10
 * @author Cay Horstmann
 */
public class SystemTrayTest
{
   public static void main(String[] args)
   {
      SystemTrayApp app = new SystemTrayApp();
      app.init();
   }
}

class SystemTrayApp
{
   public void init()
   {      
      final TrayIcon trayIcon;

      if (!SystemTray.isSupported())
      {
         System.err.println("System tray is not supported.");
         return;
      }

      SystemTray tray = SystemTray.getSystemTray();
      Image image = new ImageIcon(getClass().getResource("cookie.png")).getImage();

      PopupMenu popup = new PopupMenu();
      MenuItem exitItem = new MenuItem("Exit");
      exitItem.addActionListener(event -> System.exit(0));
      popup.add(exitItem);

      trayIcon = new TrayIcon(image, "Your Fortune", popup);

      trayIcon.setImageAutoSize(true);
      trayIcon.addActionListener(event ->
         {
            trayIcon.displayMessage("How do I turn this off?",
                  "Right-click on the fortune cookie and select Exit.",
                  TrayIcon.MessageType.INFO);
         });

      try
      {
         tray.add(trayIcon);
      }
      catch (AWTException e)
      {
         System.err.println("TrayIcon could not be added.");
         return;
      }

      final List<String> fortunes = readFortunes();
      Timer timer = new Timer(10000, event ->
         {
            int index = (int) (fortunes.size() * Math.random());
            trayIcon.displayMessage("Your Fortune", fortunes.get(index),
                  TrayIcon.MessageType.INFO);
         });
      timer.start();
   }

   private List<String> readFortunes()
   {
      List<String> fortunes = new ArrayList<>();
      try (InputStream inStream = getClass().getResourceAsStream("fortunes"))       
      {
         Scanner in = new Scanner(inStream, "UTF-8"); 
         StringBuilder fortune = new StringBuilder();
         while (in.hasNextLine())
         {
            String line = in.nextLine();
            if (line.equals("%"))
            {
               fortunes.add(fortune.toString());
               fortune = new StringBuilder();
            }
            else
            {
               fortune.append(line);
               fortune.append(' ');
            }
         }
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
      }
      return fortunes;
   }
}