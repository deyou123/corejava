package layer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * A frame with three text fields to set the background color.
 */
public class ColorFrame extends JFrame
{
   private JPanel panel;
   private JTextField redField;
   private JTextField greenField;
   private JTextField blueField;

   public ColorFrame()
   {
      panel = new JPanel();
      
      panel.add(new JLabel("Red:"));
      redField = new JTextField("255", 3);
      panel.add(redField);

      panel.add(new JLabel("Green:"));
      greenField = new JTextField("255", 3);
      panel.add(greenField);

      panel.add(new JLabel("Blue:"));
      blueField = new JTextField("255", 3);
      panel.add(blueField);

      LayerUI<JPanel> layerUI = new PanelLayer();
      JLayer<JPanel> layer = new JLayer<JPanel>(panel, layerUI);      
      
      add(layer);
      pack();
   }

   class PanelLayer extends LayerUI<JPanel>
   {
      public void installUI(JComponent c) 
      {
         super.installUI(c);
         ((JLayer<?>) c).setLayerEventMask(AWTEvent.KEY_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
      }
          
      public void uninstallUI(JComponent c) 
      {
         ((JLayer<?>) c).setLayerEventMask(0);
         super.uninstallUI(c);
      }
      
      protected void processKeyEvent(KeyEvent e, JLayer<? extends JPanel> l)
      {
         l.repaint();
      }
      
      protected void processFocusEvent(FocusEvent e, JLayer<? extends JPanel> l)
      {         
         if (e.getID() == FocusEvent.FOCUS_GAINED)
         {
            Component c = e.getComponent();
            c.setFont(getFont().deriveFont(Font.BOLD));
         }
         if (e.getID() == FocusEvent.FOCUS_LOST)
         {
            Component c = e.getComponent();
            c.setFont(getFont().deriveFont(Font.PLAIN));
         }
      }
      
      public void paint(Graphics g, JComponent c) 
      {
         super.paint(g, c);

         Graphics2D g2 = (Graphics2D) g.create();
         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
         int red = Integer.parseInt(redField.getText().trim());
         int green = Integer.parseInt(greenField.getText().trim());
         int blue = Integer.parseInt(blueField.getText().trim());
         g2.setPaint(new Color(red, green, blue));
         g2.fillRect(0, 0, c.getWidth(), c.getHeight());
         g2.dispose();
       }
   }
}

