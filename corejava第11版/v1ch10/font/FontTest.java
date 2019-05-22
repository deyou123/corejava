package font;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * @version 1.35 2018-04-10
 * @author Cay Horstmann
 */
public class FontTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            var frame = new FontFrame();
            frame.setTitle("FontTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * A frame with a text message component.
 */
class FontFrame extends JFrame
{
   public FontFrame()
   {      
      add(new FontComponent());
      pack();
   }
}

/**
 * A component that shows a centered message in a box.
 */
class FontComponent extends JComponent
{
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;

   public void paintComponent(Graphics g)
   {
      var g2 = (Graphics2D) g;

      var message = "Hello, World!";

      var f = new Font("Serif", Font.BOLD, 36);
      g2.setFont(f);

      // measure the size of the message

      FontRenderContext context = g2.getFontRenderContext();
      Rectangle2D bounds = f.getStringBounds(message, context);

      // set (x,y) = top left corner of text

      double x = (getWidth() - bounds.getWidth()) / 2;
      double y = (getHeight() - bounds.getHeight()) / 2;

      // add ascent to y to reach the baseline

      double ascent = -bounds.getY();
      double baseY = y + ascent;

      // draw the message

      g2.drawString(message, (int) x, (int) baseY);

      g2.setPaint(Color.LIGHT_GRAY);

      // draw the baseline

      g2.draw(new Line2D.Double(x, baseY, x + bounds.getWidth(), baseY));

      // draw the enclosing rectangle

      var rect = new Rectangle2D.Double(x, y, bounds.getWidth(), bounds.getHeight());
      g2.draw(rect);
   }
   
   public Dimension getPreferredSize() 
   { 
      return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); 
   }
}
