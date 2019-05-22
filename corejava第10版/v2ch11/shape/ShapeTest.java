package shape;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**
 * This program demonstrates the various 2D shapes.
 * @version 1.03 2016-05-10
 * @author Cay Horstmann
 */
public class ShapeTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new ShapeTestFrame();
            frame.setTitle("ShapeTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * This frame contains a combo box to select a shape and a component to draw it.
 */
class ShapeTestFrame extends JFrame
{
   public ShapeTestFrame()
   {
      final ShapeComponent comp = new ShapeComponent();
      add(comp, BorderLayout.CENTER);
      final JComboBox<ShapeMaker> comboBox = new JComboBox<>();
      comboBox.addItem(new LineMaker());
      comboBox.addItem(new RectangleMaker());
      comboBox.addItem(new RoundRectangleMaker());
      comboBox.addItem(new EllipseMaker());
      comboBox.addItem(new ArcMaker());
      comboBox.addItem(new PolygonMaker());
      comboBox.addItem(new QuadCurveMaker());
      comboBox.addItem(new CubicCurveMaker());
      comboBox.addActionListener(event ->
         {
            ShapeMaker shapeMaker = comboBox.getItemAt(comboBox.getSelectedIndex());
            comp.setShapeMaker(shapeMaker);
         });
      add(comboBox, BorderLayout.NORTH);
      comp.setShapeMaker((ShapeMaker) comboBox.getItemAt(0));
      pack();
   }
}

/**
 * This component draws a shape and allows the user to move the points that define it.
 */
class ShapeComponent extends JComponent
{
   private static final Dimension PREFERRED_SIZE = new Dimension(300, 200); 
   private Point2D[] points;
   private static Random generator = new Random();
   private static int SIZE = 10;
   private int current;
   private ShapeMaker shapeMaker;

   public ShapeComponent()
   {
      addMouseListener(new MouseAdapter()
         {
            public void mousePressed(MouseEvent event)
            {
               Point p = event.getPoint();
               for (int i = 0; i < points.length; i++)
               {
                  double x = points[i].getX() - SIZE / 2;
                  double y = points[i].getY() - SIZE / 2;
                  Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
                  if (r.contains(p))
                  {
                     current = i;
                     return;
                  }
               }
            }

            public void mouseReleased(MouseEvent event)
            {
               current = -1;
            }
         });
      addMouseMotionListener(new MouseMotionAdapter()
         {
            public void mouseDragged(MouseEvent event)
            {
               if (current == -1) return;
               points[current] = event.getPoint();
               repaint();
            }
         });
      current = -1;
   }

   /**
    * Set a shape maker and initialize it with a random point set.
    * @param aShapeMaker a shape maker that defines a shape from a point set
    */
   public void setShapeMaker(ShapeMaker aShapeMaker)
   {
      shapeMaker = aShapeMaker;
      int n = shapeMaker.getPointCount();
      points = new Point2D[n];
      for (int i = 0; i < n; i++)
      {
         double x = generator.nextDouble() * getWidth();
         double y = generator.nextDouble() * getHeight();
         points[i] = new Point2D.Double(x, y);
      }
      repaint();
   }

   public void paintComponent(Graphics g)
   {
      if (points == null) return;
      Graphics2D g2 = (Graphics2D) g;
      for (int i = 0; i < points.length; i++)
      {
         double x = points[i].getX() - SIZE / 2;
         double y = points[i].getY() - SIZE / 2;
         g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
      }

      g2.draw(shapeMaker.makeShape(points));
   }
   
   public Dimension getPreferredSize() { return PREFERRED_SIZE; }
}

/**
 * A shape maker can make a shape from a point set. Concrete subclasses must return a shape in the
 * makeShape method.
 */
abstract class ShapeMaker
{
   private int pointCount;

   /**
    * Constructs a shape maker.
    * @param ointCount the number of points needed to define this shape.
    */
   public ShapeMaker(int pointCount)
   {
      this.pointCount = pointCount;
   }

   /**
    * Gets the number of points needed to define this shape.
    * @return the point count
    */
   public int getPointCount()
   {
      return pointCount;
   }

   /**
    * Makes a shape out of the given point set.
    * @param p the points that define the shape
    * @return the shape defined by the points
    */
   public abstract Shape makeShape(Point2D[] p);

   public String toString()
   {
      return getClass().getName();
   }
}

/**
 * Makes a line that joins two given points.
 */
class LineMaker extends ShapeMaker
{
   public LineMaker()
   {
      super(2);
   }

   public Shape makeShape(Point2D[] p)
   {
      return new Line2D.Double(p[0], p[1]);
   }
}

/**
 * Makes a rectangle that joins two given corner points.
 */
class RectangleMaker extends ShapeMaker
{
   public RectangleMaker()
   {
      super(2);
   }

   public Shape makeShape(Point2D[] p)
   {
      Rectangle2D s = new Rectangle2D.Double();
      s.setFrameFromDiagonal(p[0], p[1]);
      return s;
   }
}

/**
 * Makes a round rectangle that joins two given corner points.
 */
class RoundRectangleMaker extends ShapeMaker
{
   public RoundRectangleMaker()
   {
      super(2);
   }

   public Shape makeShape(Point2D[] p)
   {
      RoundRectangle2D s = new RoundRectangle2D.Double(0, 0, 0, 0, 20, 20);
      s.setFrameFromDiagonal(p[0], p[1]);
      return s;
   }
}

/**
 * Makes an ellipse contained in a bounding box with two given corner points.
 */
class EllipseMaker extends ShapeMaker
{
   public EllipseMaker()
   {
      super(2);
   }

   public Shape makeShape(Point2D[] p)
   {
      Ellipse2D s = new Ellipse2D.Double();
      s.setFrameFromDiagonal(p[0], p[1]);
      return s;
   }
}

/**
 * Makes an arc contained in a bounding box with two given corner points, and with starting and
 * ending angles given by lines emanating from the center of the bounding box and ending in two
 * given points. To show the correctness of the angle computation, the returned shape contains the
 * arc, the bounding box, and the lines.
 */
class ArcMaker extends ShapeMaker
{
   public ArcMaker()
   {
      super(4);
   }

   public Shape makeShape(Point2D[] p)
   {
      double centerX = (p[0].getX() + p[1].getX()) / 2;
      double centerY = (p[0].getY() + p[1].getY()) / 2;
      double width = Math.abs(p[1].getX() - p[0].getX());
      double height = Math.abs(p[1].getY() - p[0].getY());

      double skewedStartAngle = Math.toDegrees(Math.atan2(-(p[2].getY() - centerY) * width, 
         (p[2].getX() - centerX) * height));
      double skewedEndAngle = Math.toDegrees(Math.atan2(-(p[3].getY() - centerY) * width, 
         (p[3].getX() - centerX) * height));
      double skewedAngleDifference = skewedEndAngle - skewedStartAngle;
      if (skewedStartAngle < 0) skewedStartAngle += 360;
      if (skewedAngleDifference < 0) skewedAngleDifference += 360;

      Arc2D s = new Arc2D.Double(0, 0, 0, 0, skewedStartAngle, skewedAngleDifference, Arc2D.OPEN);
      s.setFrameFromDiagonal(p[0], p[1]);

      GeneralPath g = new GeneralPath();
      g.append(s, false);
      Rectangle2D r = new Rectangle2D.Double();
      r.setFrameFromDiagonal(p[0], p[1]);
      g.append(r, false);
      Point2D center = new Point2D.Double(centerX, centerY);
      g.append(new Line2D.Double(center, p[2]), false);
      g.append(new Line2D.Double(center, p[3]), false);
      return g;
   }
}

/**
 * Makes a polygon defined by six corner points.
 */
class PolygonMaker extends ShapeMaker
{
   public PolygonMaker()
   {
      super(6);
   }

   public Shape makeShape(Point2D[] p)
   {
      GeneralPath s = new GeneralPath();
      s.moveTo((float) p[0].getX(), (float) p[0].getY());
      for (int i = 1; i < p.length; i++)
         s.lineTo((float) p[i].getX(), (float) p[i].getY());
      s.closePath();
      return s;
   }
}

/**
 * Makes a quad curve defined by two end points and a control point.
 */
class QuadCurveMaker extends ShapeMaker
{
   public QuadCurveMaker()
   {
      super(3);
   }

   public Shape makeShape(Point2D[] p)
   {
      return new QuadCurve2D.Double(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY(), 
         p[2].getX(), p[2].getY());
   }
}

/**
 * Makes a cubic curve defined by two end points and two control points.
 */
class CubicCurveMaker extends ShapeMaker
{
   public CubicCurveMaker()
   {
      super(4);
   }

   public Shape makeShape(Point2D[] p)
   {
      return new CubicCurve2D.Double(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY(), p[2]
            .getX(), p[2].getY(), p[3].getX(), p[3].getY());
   }
}
