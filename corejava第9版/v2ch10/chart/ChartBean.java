package chart;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;
import sourceAnnotations.*;

/**
 * A bean to draw a bar chart.
 * @version 1.31 2007-10-03
 * @author Cay Horstmann
 */
public class ChartBean extends JComponent
{
   public ChartBean()
   {
      setPreferredSize(new Dimension(XPREFSIZE, YPREFSIZE));
   }

   public void paintComponent(Graphics g)
   {
      Graphics2D g2 = (Graphics2D) g;

      if (values == null || values.length == 0) return;
      double minValue = 0;
      double maxValue = 0;
      for (int i = 0; i < values.length; i++)
      {
         if (minValue > getValues(i)) minValue = getValues(i);
         if (maxValue < getValues(i)) maxValue = getValues(i);
      }
      if (maxValue == minValue) return;

      Rectangle2D bounds = getBounds();
      double clientWidth = bounds.getWidth();
      double clientHeight = bounds.getHeight();
      double barWidth = (clientWidth - 2 * INSETS) / values.length;

      g2.setPaint(inverse ? color : Color.white);
      g2.fill(new Rectangle2D.Double(0, 0, clientWidth, clientHeight));
      g2.setPaint(Color.black);

      Font titleFont = new Font("SansSerif", Font.BOLD, 20);
      FontRenderContext context = g2.getFontRenderContext();
      Rectangle2D titleBounds = titleFont.getStringBounds(title, context);

      double titleWidth = titleBounds.getWidth();
      double y = -titleBounds.getY();
      double x = 0;
      if (titlePosition == Position.CENTER) x += (clientWidth - titleWidth) / 2;
      else if (titlePosition == Position.RIGHT) x += clientWidth - titleWidth;

      g2.setFont(titleFont);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g2.drawString(title, (float) x, (float) y);

      double top = titleBounds.getHeight();
      double scale = (clientHeight - top - 2 * INSETS) / (maxValue - minValue);
      y = clientHeight;

      for (int i = 0; i < values.length; i++)
      {
         double x1 = INSETS + i * barWidth + 1;
         double y1 = INSETS + top;
         double value = getValues(i);
         double height = value * scale;
         if (value >= 0) y1 += (maxValue - value) * scale;
         else
         {
            y1 += (int) (maxValue * scale);
            height = -height;
         }

         g2.setPaint(inverse ? Color.white : color);
         Rectangle2D bar = new Rectangle2D.Double(x1, y1, barWidth - 2, height);
         g2.fill(bar);
         g2.setPaint(Color.black);
         g2.draw(bar);
      }
   }

   /**
    * Sets the title property.
    * @param t the new chart title.
    */
   @Property
   public void setTitle(String t)
   {
      title = t;
   }

   /**
    * Gets the title property.
    * @return the chart title.
    */
   public String getTitle()
   {
      return title;
   }

   /**
    * Sets the indexed values property.
    * @param v the values to display in the chart.
    */
   public void setValues(double[] v)
   {
      values = v;
   }

   /**
    * Gets the indexed values property.
    * @return the values to display in the chart.
    */
   public double[] getValues()
   {
      return values;
   }

   /**
    * Sets the indexed values property.
    * @param i the index of the value to set
    * @param value the new value for that index
    */
   @Property(editor = "DoubleArrayEditor")
   public void setValues(int i, double value)
   {
      if (0 <= i && i < values.length) values[i] = value;
   }

   /**
    * Gets the indexed values property.
    * @param i the index of the value to get
    * @return the value for that index
    */
   public double getValues(int i)
   {
      if (0 <= i && i < values.length) return values[i];
      return 0;
   }

   /**
    * Sets the inverse property.
    * @param b true if the display is inverted (white bars on colored background)
    */
   @Property(editor = "InverseEditor")
   public void setInverse(boolean b)
   {
      inverse = b;
   }

   /**
    * Gets the inverse property.
    * @return true if the display is inverted
    */
   public boolean isInverse()
   {
      return inverse;
   }

   /**
    * Sets the titlePosition property.
    * @param p LEFT, CENTER, or RIGHT
    */
   @Property(editor = "TitlePositionEditor")
   public void setTitlePosition(Position p)
   {
      titlePosition = p;
   }

   /**
    * Gets the titlePosition property.
    * @return LEFT, CENTER, or RIGHT
    */
   public Position getTitlePosition()
   {
      return titlePosition;
   }

   /**
    * Sets the graphColor property.
    * @param c the color to use for the graph
    */
   @Property
   public void setGraphColor(Color c)
   {
      color = c;
   }

   /**
    * Gets the graphColor property.
    * @param c the color to use for the graph
    */
   public Color getGraphColor()
   {
      return color;
   }

   public enum Position { LEFT, CENTER, RIGHT };
   
   private static final int XPREFSIZE = 300;
   private static final int YPREFSIZE = 300;

   private static final int INSETS = 10;

   private double[] values = { 1, 2, 3 };
   private String title = "Title";
   private Position titlePosition = Position.CENTER;
   private boolean inverse;
   private Color color = Color.red;
}
