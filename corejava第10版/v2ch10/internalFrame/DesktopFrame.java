package internalFrame;

import java.awt.*;
import java.beans.*;

import javax.swing.*;

/**
 * This desktop frame contains editor panes that show HTML documents.
 */
public class DesktopFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 400;
   private static final String[] planets = { "Mercury", "Venus", "Earth", "Mars", "Jupiter",
         "Saturn", "Uranus", "Neptune", "Pluto", };

   private JDesktopPane desktop;
   private int nextFrameX;
   private int nextFrameY;
   private int frameDistance;
   private int counter;

   public DesktopFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      desktop = new JDesktopPane();
      add(desktop, BorderLayout.CENTER);

      // set up menus

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      JMenu fileMenu = new JMenu("File");
      menuBar.add(fileMenu);
      JMenuItem openItem = new JMenuItem("New");
      openItem.addActionListener(event ->
         {
            createInternalFrame(new JLabel(
                  new ImageIcon(getClass().getResource(planets[counter] + ".gif"))),
                  planets[counter]);
            counter = (counter + 1) % planets.length;
         });
      fileMenu.add(openItem);
      JMenuItem exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(event -> System.exit(0));
      fileMenu.add(exitItem);
      JMenu windowMenu = new JMenu("Window");
      menuBar.add(windowMenu);
      JMenuItem nextItem = new JMenuItem("Next");
      nextItem.addActionListener(event -> selectNextWindow());
      windowMenu.add(nextItem);
      JMenuItem cascadeItem = new JMenuItem("Cascade");
      cascadeItem.addActionListener(event -> cascadeWindows());
      windowMenu.add(cascadeItem);
      JMenuItem tileItem = new JMenuItem("Tile");
      tileItem.addActionListener(event -> tileWindows());
      windowMenu.add(tileItem);
      final JCheckBoxMenuItem dragOutlineItem = new JCheckBoxMenuItem("Drag Outline");
      dragOutlineItem.addActionListener(event ->
         desktop.setDragMode(dragOutlineItem.isSelected() ? JDesktopPane.OUTLINE_DRAG_MODE
            : JDesktopPane.LIVE_DRAG_MODE));         
      windowMenu.add(dragOutlineItem);
   }

   /**
    * Creates an internal frame on the desktop.
    * @param c the component to display in the internal frame
    * @param t the title of the internal frame
    */
   public void createInternalFrame(Component c, String t)
   {
      final JInternalFrame iframe = new JInternalFrame(t, true, // resizable
            true, // closable
            true, // maximizable
            true); // iconifiable

      iframe.add(c, BorderLayout.CENTER);
      desktop.add(iframe);

      iframe.setFrameIcon(new ImageIcon(getClass().getResource("document.gif")));

      // add listener to confirm frame closing
      iframe.addVetoableChangeListener(event ->
         {
            String name = event.getPropertyName();
            Object value = event.getNewValue();

            // we only want to check attempts to close a frame
            if (name.equals("closed") && value.equals(true))
            {
               // ask user if it is ok to close
               int result = JOptionPane.showInternalConfirmDialog(iframe, "OK to close?",
                     "Select an Option", JOptionPane.YES_NO_OPTION);

               // if the user doesn't agree, veto the close
               if (result != JOptionPane.YES_OPTION) throw new PropertyVetoException(
                     "User canceled close", event);
            }
         });

      // position frame
      int width = desktop.getWidth() / 2;
      int height = desktop.getHeight() / 2;
      iframe.reshape(nextFrameX, nextFrameY, width, height);

      iframe.show();

      // select the frame--might be vetoed
      try
      {
         iframe.setSelected(true);
      }
      catch (PropertyVetoException ex)
      {
      }

      frameDistance = iframe.getHeight() - iframe.getContentPane().getHeight();

      // compute placement for next frame

      nextFrameX += frameDistance;
      nextFrameY += frameDistance;
      if (nextFrameX + width > desktop.getWidth()) nextFrameX = 0;
      if (nextFrameY + height > desktop.getHeight()) nextFrameY = 0;
   }

   /**
    * Cascades the noniconified internal frames of the desktop.
    */
   public void cascadeWindows()
   {
      int x = 0;
      int y = 0;
      int width = desktop.getWidth() / 2;
      int height = desktop.getHeight() / 2;

      for (JInternalFrame frame : desktop.getAllFrames())
      {
         if (!frame.isIcon())
         {
            try
            {
               // try to make maximized frames resizable; this might be vetoed
               frame.setMaximum(false);
               frame.reshape(x, y, width, height);

               x += frameDistance;
               y += frameDistance;
               // wrap around at the desktop edge
               if (x + width > desktop.getWidth()) x = 0;
               if (y + height > desktop.getHeight()) y = 0;
            }
            catch (PropertyVetoException ex)
            {
            }
         }
      }
   }

   /**
    * Tiles the noniconified internal frames of the desktop.
    */
   public void tileWindows()
   {
      // count frames that aren't iconized
      int frameCount = 0;
      for (JInternalFrame frame : desktop.getAllFrames())
         if (!frame.isIcon()) frameCount++;
      if (frameCount == 0) return;

      int rows = (int) Math.sqrt(frameCount);
      int cols = frameCount / rows;
      int extra = frameCount % rows;
      // number of columns with an extra row

      int width = desktop.getWidth() / cols;
      int height = desktop.getHeight() / rows;
      int r = 0;
      int c = 0;
      for (JInternalFrame frame : desktop.getAllFrames())
      {
         if (!frame.isIcon())
         {
            try
            {
               frame.setMaximum(false);
               frame.reshape(c * width, r * height, width, height);
               r++;
               if (r == rows)
               {
                  r = 0;
                  c++;
                  if (c == cols - extra)
                  {
                     // start adding an extra row
                     rows++;
                     height = desktop.getHeight() / rows;
                  }
               }
            }
            catch (PropertyVetoException ex)
            {
            }
         }
      }
   }

   /**
    * Brings the next noniconified internal frame to the front.
    */
   public void selectNextWindow()
   {
      JInternalFrame[] frames = desktop.getAllFrames();
      for (int i = 0; i < frames.length; i++)
      {
         if (frames[i].isSelected())
         {
            // find next frame that isn't an icon and can be selected
            int next = (i + 1) % frames.length;
            while (next != i)
            {
               if (!frames[next].isIcon())
               {
                  try
                  {
                     // all other frames are icons or veto selection
                     frames[next].setSelected(true);
                     frames[next].toFront();
                     frames[i].toBack();
                     return;
                  }
                  catch (PropertyVetoException ex)
                  {
                  }
               }
               next = (next + 1) % frames.length;
            }
         }
      }
   }
}
