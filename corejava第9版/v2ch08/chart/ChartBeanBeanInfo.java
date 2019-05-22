package chart;

import java.awt.*;
import java.beans.*;

/**
 * The bean info for the chart bean, specifying the property editors.
 * @version 1.20 2007-10-05
 * @author Cay Horstmann
 */
public class ChartBeanBeanInfo extends SimpleBeanInfo
{
   private PropertyDescriptor[] propertyDescriptors;
   private Image iconColor16;
   private Image iconColor32;
   private Image iconMono16;
   private Image iconMono32;

   public ChartBeanBeanInfo()
   {
      iconColor16 = loadImage("ChartBean_COLOR_16x16.gif");
      iconColor32 = loadImage("ChartBean_COLOR_32x32.gif");
      iconMono16 = loadImage("ChartBean_MONO_16x16.gif");
      iconMono32 = loadImage("ChartBean_MONO_32x32.gif");

      try
      {
         PropertyDescriptor titlePositionDescriptor = new PropertyDescriptor("titlePosition",
               ChartBean.class);
         titlePositionDescriptor.setPropertyEditorClass(TitlePositionEditor.class);
         PropertyDescriptor inverseDescriptor = new PropertyDescriptor("inverse", ChartBean.class);
         inverseDescriptor.setPropertyEditorClass(InverseEditor.class);
         PropertyDescriptor valuesDescriptor = new PropertyDescriptor("values", ChartBean.class);
         valuesDescriptor.setPropertyEditorClass(DoubleArrayEditor.class);
         propertyDescriptors = new PropertyDescriptor[] {
               new PropertyDescriptor("title", ChartBean.class), titlePositionDescriptor,
               valuesDescriptor, new PropertyDescriptor("graphColor", ChartBean.class),
               inverseDescriptor };
      }
      catch (IntrospectionException e)
      {
         e.printStackTrace();
      }
   }

   public PropertyDescriptor[] getPropertyDescriptors()
   {
      return propertyDescriptors;
   }

   public Image getIcon(int iconType)
   {
      if (iconType == BeanInfo.ICON_COLOR_16x16) return iconColor16;
      else if (iconType == BeanInfo.ICON_COLOR_32x32) return iconColor32;
      else if (iconType == BeanInfo.ICON_MONO_16x16) return iconMono16;
      else if (iconType == BeanInfo.ICON_MONO_32x32) return iconMono32;
      else return null;
   }
}
