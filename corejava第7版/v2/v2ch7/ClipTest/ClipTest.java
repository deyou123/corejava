/**
   @version 1.02 2004-08-24
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**
   This program demonstrates the use of a clip shape.
*/
public class ClipTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new ClipTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame contains a check box to turn a clip off
   and on, and a panel to draw a set of lines with or without
   clipping.
*/
class ClipTestFrame extends JFrame
{  
   public ClipTestFrame()
   {  
      setTitle("ClipTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      final JCheckBox checkBox = new JCheckBox("Clip");
      checkBox.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               panel.repaint();
            }
         });
      add(checkBox, BorderLayout.NORTH);

      panel = new 
         JPanel()
         {
            public void paintComponent(Graphics g)
            {  
               super.paintComponent(g);
               Graphics2D g2 = (Graphics2D)g;

               if (clipShape == null) clipShape = makeClipShape(g2);

               g2.draw(clipShape);

               if (checkBox.isSelected()) g2.clip(clipShape);
                  
               // draw line pattern
               final int NLINES = 50;
               Point2D p = new Point2D.Double(0, 0);
               for (int i = 0; i < NLINES; i++)
               {  
                  double x = (2 * getWidth() * i) / NLINES;
                  double y = (2 * getHeight() * (NLINES - 1 - i)) / NLINES;
                  Point2D q = new Point2D.Double(x, y);
                  g2.draw(new Line2D.Double(p, q));
               }
            }
         };
      add(panel, BorderLayout.CENTER);
   }

   /**
      Makes the clip shape.
      @param g2 the graphics context
      @return the clip shape
   */
   Shape makeClipShape(Graphics2D g2)
   {
      FontRenderContext context = g2.getFontRenderContext();
      Font f = new Font("Serif", Font.PLAIN, 100);
      GeneralPath clipShape = new GeneralPath();
      
      TextLayout layout = new TextLayout("Hello", f, context);
      AffineTransform transform = AffineTransform.getTranslateInstance(0, 100);
      Shape outline = layout.getOutline(transform);
      clipShape.append(outline, false);
      
      layout = new TextLayout("World", f, context);
      transform = AffineTransform.getTranslateInstance(0, 200);
      outline = layout.getOutline(transform);
      clipShape.append(outline, false);
      return clipShape;
   }

   private JPanel panel;
   private Shape clipShape;
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 300;
}
