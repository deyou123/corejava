/**
   @version 1.20 2004-08-30
   @author Cay Horstmann
*/

package com.horstmann.corejava;

import java.awt.*;
import java.beans.*;

/**
   The bean info for the chart bean, specifying the 
   icons and the customizer.
*/
public class ChartBean2BeanInfo extends SimpleBeanInfo
{  
   public BeanDescriptor getBeanDescriptor()
   {  
      return new BeanDescriptor(ChartBean2.class, ChartBean2Customizer.class);
   }

   public Image getIcon(int iconType)
   {  
      String name = "";
      if (iconType == BeanInfo.ICON_COLOR_16x16) name = "COLOR_16x16";
      else if (iconType == BeanInfo.ICON_COLOR_32x32) name = "COLOR_32x32";
      else if (iconType == BeanInfo.ICON_MONO_16x16) name = "MONO_16x16";
      else if (iconType == BeanInfo.ICON_MONO_32x32) name = "MONO_32x32";
      else return null;
      return loadImage("ChartBean2_" + name + ".gif");
   }
}
