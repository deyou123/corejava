package fill;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * @version 1.34 2015-05-12
 * @author Cay Horstmann
 */
public class FillTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new FillFrame();
            frame.setTitle("FillTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * A frame that contains a component with drawings
 */
class FillFrame extends JFrame
{
   public FillFrame()
   {
      add(new FillComponent());
      pack();
   }
}

/**
 * A component that displays filled rectangles and ellipses
 */
class FillComponent extends JComponent
{
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 400;

   public void paintComponent(Graphics g)
   {
      Graphics2D g2 = (Graphics2D) g;

      // draw a rectangle

      double leftX = 100;
      double topY = 100;
      double width = 200;
      double height = 150;

      Rectangle2D rect = new Rectangle2D.Double(leftX, topY, width, height);
      g2.setPaint(Color.BLACK);
      g2.draw(rect);
      g2.setPaint(Color.RED);
      g2.fill(rect); // Note that the right and bottom edge are not painted over
      
      // draw the enclosed ellipse

      Ellipse2D ellipse = new Ellipse2D.Double();
      ellipse.setFrame(rect);
      g2.setPaint(new Color(0, 128, 128)); // a dull blue-green
      g2.fill(ellipse);
   }
   
   public Dimension getPreferredSize() { return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); }
}
