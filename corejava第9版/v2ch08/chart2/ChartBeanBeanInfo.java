package chart2;

import java.awt.*;
import java.beans.*;

/**
 * The bean info for the chart bean, specifying the icons and the customizer.
 * @version 1.30 2007-10-05
 * @author Cay Horstmann
 */
public class ChartBeanBeanInfo extends SimpleBeanInfo
{
   private BeanDescriptor beanDescriptor;
   private Image iconColor16;
   private Image iconColor32;
   private Image iconMono16;
   private Image iconMono32;

   public ChartBeanBeanInfo()
   {
      beanDescriptor = new BeanDescriptor(ChartBean.class, ChartBeanCustomizer.class);
   }
   
   public BeanDescriptor getBeanDescriptor()
   {
      return beanDescriptor;
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
