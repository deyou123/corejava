import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.io.*;
import javax.swing.Timer;

/**
 * This program demonstrates the system tray API.
 * @version 1.00 2007-09-22
 * @author Cay Horstmann
 */
public class SystemTrayTest
{
   public static void main(String[] args)
   {
      final TrayIcon trayIcon;

      if (!SystemTray.isSupported())
      {
         System.err.println("System tray is not supported.");
         return;
      }

      SystemTray tray = SystemTray.getSystemTray();
      Image image = Toolkit.getDefaultToolkit().getImage("cookie.png");

      PopupMenu popup = new PopupMenu();
      MenuItem exitItem = new MenuItem("Exit");
      exitItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               System.exit(0);
            }
         });
      popup.add(exitItem);

      trayIcon = new TrayIcon(image, "Your Fortune", popup);

      trayIcon.setImageAutoSize(true);
      trayIcon.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               trayIcon.displayMessage("How do I turn this off?",
                     "Right-click on the fortune cookie and select Exit.",
                     TrayIcon.MessageType.INFO);
            }
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
      Timer timer = new Timer(10000, new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               int index = (int) (fortunes.size() * Math.random());
               trayIcon.displayMessage("Your Fortune", fortunes.get(index),
                     TrayIcon.MessageType.INFO);
            }
         });
      timer.start();
   }

   private static List<String> readFortunes()
   {
      List<String> fortunes = new ArrayList<String>();
      try
      {
         Scanner in = new Scanner(new File("fortunes"));
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
