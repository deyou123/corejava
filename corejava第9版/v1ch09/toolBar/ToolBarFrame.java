package toolBar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A frame with a toolbar and menu for color changes.
 */
public class ToolBarFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;
   private JPanel panel;

   public ToolBarFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add a panel for color change

      panel = new JPanel();
      add(panel, BorderLayout.CENTER);

      // set up actions

      Action blueAction = new ColorAction("Blue", new ImageIcon("blue-ball.gif"), Color.BLUE);
      Action yellowAction = new ColorAction("Yellow", new ImageIcon("yellow-ball.gif"),
            Color.YELLOW);
      Action redAction = new ColorAction("Red", new ImageIcon("red-ball.gif"), Color.RED);

      Action exitAction = new AbstractAction("Exit", new ImageIcon("exit.gif"))
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         };
      exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit");

      // populate tool bar

      JToolBar bar = new JToolBar();
      bar.add(blueAction);
      bar.add(yellowAction);
      bar.add(redAction);
      bar.addSeparator();
      bar.add(exitAction);
      add(bar, BorderLayout.NORTH);

      // populate menu

      JMenu menu = new JMenu("Color");
      menu.add(yellowAction);
      menu.add(blueAction);
      menu.add(redAction);
      menu.add(exitAction);
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(menu);
      setJMenuBar(menuBar);
   }

   /**
    * The color action sets the background of the frame to a given color.
    */
   class ColorAction extends AbstractAction
   {
      public ColorAction(String name, Icon icon, Color c)
      {
         putValue(Action.NAME, name);
         putValue(Action.SMALL_ICON, icon);
         putValue(Action.SHORT_DESCRIPTION, name + " background");
         putValue("Color", c);
      }

      public void actionPerformed(ActionEvent event)
      {
         Color c = (Color) getValue("Color");
         panel.setBackground(c);
      }
   }
}
