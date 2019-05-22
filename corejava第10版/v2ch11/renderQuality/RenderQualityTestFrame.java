package renderQuality;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

/**
 * This frame contains buttons to set rendering hints and an image that is drawn with the selected
 * hints.
 */
public class RenderQualityTestFrame extends JFrame
{
   private RenderQualityComponent canvas;
   private JPanel buttonBox;
   private RenderingHints hints;
   private int r;

   public RenderQualityTestFrame()
   {
      buttonBox = new JPanel();
      buttonBox.setLayout(new GridBagLayout());
      hints = new RenderingHints(null);

      makeButtons("KEY_ANTIALIASING", "VALUE_ANTIALIAS_OFF", "VALUE_ANTIALIAS_ON");
      makeButtons("KEY_TEXT_ANTIALIASING", "VALUE_TEXT_ANTIALIAS_OFF", "VALUE_TEXT_ANTIALIAS_ON");
      makeButtons("KEY_FRACTIONALMETRICS", "VALUE_FRACTIONALMETRICS_OFF",
            "VALUE_FRACTIONALMETRICS_ON");
      makeButtons("KEY_RENDERING", "VALUE_RENDER_SPEED", "VALUE_RENDER_QUALITY");
      makeButtons("KEY_STROKE_CONTROL", "VALUE_STROKE_PURE", "VALUE_STROKE_NORMALIZE");
      canvas = new RenderQualityComponent();
      canvas.setRenderingHints(hints);

      add(canvas, BorderLayout.CENTER);
      add(buttonBox, BorderLayout.NORTH);
      pack();
   }

   /**
    * Makes a set of buttons for a rendering hint key and values.
    * @param key the key name
    * @param value1 the name of the first value for the key
    * @param value2 the name of the second value for the key
    */
   void makeButtons(String key, String value1, String value2)
   {
      try
      {
         final RenderingHints.Key k = 
            (RenderingHints.Key) RenderingHints.class.getField(key).get(null);
         final Object v1 = RenderingHints.class.getField(value1).get(null);
         final Object v2 = RenderingHints.class.getField(value2).get(null);
         JLabel label = new JLabel(key);

         buttonBox.add(label, new GBC(0, r).setAnchor(GBC.WEST));
         ButtonGroup group = new ButtonGroup();
         JRadioButton b1 = new JRadioButton(value1, true);

         buttonBox.add(b1, new GBC(1, r).setAnchor(GBC.WEST));
         group.add(b1);
         b1.addActionListener(event ->
            {
               hints.put(k, v1);
               canvas.setRenderingHints(hints);
            });
         JRadioButton b2 = new JRadioButton(value2, false);

         buttonBox.add(b2, new GBC(2, r).setAnchor(GBC.WEST));
         group.add(b2);
         b2.addActionListener(event ->
            {
               hints.put(k, v2);
               canvas.setRenderingHints(hints);
            });
         hints.put(k, v1);
         r++;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}

/**
 * This component produces a drawing that shows the effect of rendering hints.
 */
class RenderQualityComponent extends JComponent
{
   private static final Dimension PREFERRED_SIZE = new Dimension(750, 150);
   private RenderingHints hints = new RenderingHints(null);
   private Image image;

   public RenderQualityComponent()
   {
      image = new ImageIcon(getClass().getResource("face.gif")).getImage();      
   }

   public void paintComponent(Graphics g)
   {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHints(hints);

      g2.draw(new Ellipse2D.Double(10, 10, 60, 50));
      g2.setFont(new Font("Serif", Font.ITALIC, 40));
      g2.drawString("Hello", 75, 50);

      g2.draw(new Rectangle2D.Double(200, 10, 40, 40));
      g2.draw(new Line2D.Double(201, 11, 239, 49));
      
      g2.drawImage(image, 250, 10, 100, 100, null);
   }
   
   /**
    * Sets the hints and repaints.
    * @param h the rendering hints
    */
   public void setRenderingHints(RenderingHints h)
   {
      hints = h;
      repaint();
   }
   
   public Dimension getPreferredSize() { return PREFERRED_SIZE; }
}
