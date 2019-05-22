/**
   @version 1.30 2001-08-21
   @author Cay Horstmann
*/

package com.horstmann.corejava;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;
import java.beans.*;
import java.io.*;
import javax.swing.*;

/**
   A bean to draw a bar chart.
*/
public class ChartBean extends JPanel
{  
   public void paint(Graphics g)
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

      Dimension d = getSize();
      Rectangle2D bounds = getBounds();
      double clientWidth = bounds.getWidth();
      double clientHeight = bounds.getHeight();
      double barWidth = clientWidth / values.length;

      g2.setPaint(inverse ? color : Color.white);
      g2.fill(bounds);
      g2.setPaint(Color.black);

      Font titleFont = new Font("SansSerif", Font.BOLD, 20);
      FontRenderContext context = g2.getFontRenderContext();
      Rectangle2D titleBounds = titleFont.getStringBounds(title, context);

      double titleWidth = titleBounds.getWidth();
      double y = -titleBounds.getY();
      double x;
      if (titlePosition == LEFT) x = 0;
      else if (titlePosition == CENTER) x = (clientWidth - titleWidth) / 2;
      else x = clientWidth - titleWidth;

      g2.setFont(titleFont);
      g2.drawString(title, (float) x, (float) y);

      double top = titleBounds.getHeight();
      double scale = (clientHeight - top) / (maxValue - minValue);
      y = clientHeight;

      for (int i = 0; i < values.length; i++)
      {  
         double x1 = i * barWidth + 1;
         double y1 = top;
         double value = getValues(i);
         double height =  value * scale;
         if (value >= 0)
            y1 += (maxValue - value) * scale;
         else
         {  
            y1 += (int)(maxValue * scale);
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
      Sets the title property.
      @param t the new chart title.
   */
   public void setTitle(String t) { title = t; }

   /**
      Gets the title property.
      @return the chart title.
   */
   public String getTitle() { return title; }

   /**
      Sets the indexed values property.
      @param v the values to display in the chart.
   */
   public void setValues(double[] v) { values = v; }

   /**
      Gets the indexed values property.
      @return the values to display in the chart.
   */
   public double[] getValues() { return values; }

   /**
      Sets the indexed values property.
      @param i the index of the value to set
      @param value the new value for that index
   */
   public void setValues(int i, double value)
   {  
      if (0 <= i && i < values.length) values[i] = value;
   }

   /**
      Gets the indexed values property.
      @param i the index of the value to get
      @return the value for that index
   */
   public double getValues(int i)
   {  
      if (0 <= i && i < values.length) return values[i];
      return 0;
   }

   /**
      Sets the inverse property.
      @param b true if the display is inverted (white bars
      on colored background)
   */
   public void setInverse(boolean b) { inverse = b; }

   /**
      Gets the inverse property.
      @return true if the display is inverted
   */
   public boolean isInverse() { return inverse; }

   /**
      Sets the titlePosition property.
      @param p LEFT, CENTER, or RIGHT
   */
   public void setTitlePosition(int p) { titlePosition = p; }

   /**
      Gets the titlePosition property.
      @return LEFT, CENTER, or RIGHT
   */
   public int getTitlePosition() { return titlePosition; }

   /**
      Sets the graphColor property.
      @param c the color to use for the graph
   */
   public void setGraphColor(Color c) { color = c; }

   /**
      Gets the graphColor property.
      @param c the color to use for the graph
   */
   public Color getGraphColor() { return color; }

   public Dimension getPreferredSize()
   {  
      return new Dimension(XPREFSIZE, YPREFSIZE);
   }

   private static final int LEFT = 0;
   private static final int CENTER = 1;
   private static final int RIGHT = 2;

   private static final int XPREFSIZE = 300;
   private static final int YPREFSIZE = 300;
   private double[] values = { 1, 2, 3 };
   private String title = "Title";
   private int titlePosition = CENTER;
   private boolean inverse;
   private Color color = Color.red;
}
