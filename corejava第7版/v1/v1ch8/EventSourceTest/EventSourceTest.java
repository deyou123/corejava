/**
   @version 1.31 2004-05-03
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.beans.*;

public class EventSourceTest
{  
   public static void main(String[] args)
   {  
      EventSourceFrame frame = new EventSourceFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame that contains a panel with drawings
*/
class EventSourceFrame extends JFrame
{
   public EventSourceFrame()
   {
      setTitle("EventSourceTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add panel to frame

      final PaintCountPanel panel = new PaintCountPanel();
      add(panel);

      panel.addPropertyChangeListener(new
         PropertyChangeListener()
         {
            public void propertyChange(PropertyChangeEvent event)
            {               
               setTitle("EventSourceTest - " + event.getNewValue());
            }
         });
   }

   public static final int DEFAULT_WIDTH = 400;
   public static final int DEFAULT_HEIGHT = 200;  
}

/**
   A panel that counts how often it is painted. 
*/
class PaintCountPanel extends JPanel
{  
   public void paintComponent(Graphics g)
   {  
      int oldPaintCount = paintCount;
      paintCount++;
      firePropertyChangeEvent(new PropertyChangeEvent(this, 
         "paintCount", oldPaintCount, paintCount));
      super.paintComponent(g);
   }

   /**
      Adds a change listener
      @param listener the listener to add
   */
   public void addPropertyChangeListener(PropertyChangeListener listener)
   {  
      listenerList.add(PropertyChangeListener.class, listener);
   }

   /**
      Removes a change listener
      @param listener the listener to remove
   */
   public void removePropertyChangeListener(PropertyChangeListener listener)
   {  
      listenerList.remove(PropertyChangeListener.class, listener);
   }

   public void firePropertyChangeEvent(PropertyChangeEvent event)
   {  
      EventListener[] listeners = listenerList.getListeners(PropertyChangeListener.class);
      for (EventListener l : listeners)
         ((PropertyChangeListener) l).propertyChange(event);
   }

   public int getPaintCount()
   {
      return paintCount;
   }

   private int paintCount;
}
