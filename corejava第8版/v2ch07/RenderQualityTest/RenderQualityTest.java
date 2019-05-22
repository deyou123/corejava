import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * This program demonstrates the effect of the various rendering hints.
 * @version 1.10 2007-08-16
 * @author Cay Horstmann
 */
public class RenderQualityTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new RenderQualityTestFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame contains buttons to set rendering hints and an image that is drawn with the selected
 * hints.
 */
class RenderQualityTestFrame extends JFrame
{
   public RenderQualityTestFrame()
   {
      setTitle("RenderQualityTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

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
   }

   /**
    * Makes a set of buttons for a rendering hint key and values
    * @param key the key name
    * @param value1 the name of the first value for the key
    * @param value2 the name of the second value for the key
    */
   void makeButtons(String key, String value1, String value2)
   {
      try
      {
         final RenderingHints.Key k = (RenderingHints.Key) RenderingHints.class.getField(key).get(
               null);
         final Object v1 = RenderingHints.class.getField(value1).get(null);
         final Object v2 = RenderingHints.class.getField(value2).get(null);
         JLabel label = new JLabel(key);

         buttonBox.add(label, new GBC(0, r).setAnchor(GBC.WEST));
         ButtonGroup group = new ButtonGroup();
         JRadioButton b1 = new JRadioButton(value1, true);

         buttonBox.add(b1, new GBC(1, r).setAnchor(GBC.WEST));
         group.add(b1);
         b1.addActionListener(new ActionListener()
            {
               public void actionPerformed(ActionEvent event)
               {
                  hints.put(k, v1);
                  canvas.setRenderingHints(hints);
               }
            });
         JRadioButton b2 = new JRadioButton(value2, false);

         buttonBox.add(b2, new GBC(2, r).setAnchor(GBC.WEST));
         group.add(b2);
         b2.addActionListener(new ActionListener()
            {
               public void actionPerformed(ActionEvent event)
               {
                  hints.put(k, v2);
                  canvas.setRenderingHints(hints);
               }
            });
         hints.put(k, v1);
         r++;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   private RenderQualityComponent canvas;
   private JPanel buttonBox;
   private RenderingHints hints;
   private int r;
   private static final int DEFAULT_WIDTH = 750;
   private static final int DEFAULT_HEIGHT = 300;
}

/**
 * This component produces a drawing that shows the effect of rendering hints.
 */
class RenderQualityComponent extends JComponent
{
   public RenderQualityComponent()
   {
      try
      {
         image = ImageIO.read(new File("face.gif"));
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
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

   private RenderingHints hints = new RenderingHints(null);
   private Image image;
}
