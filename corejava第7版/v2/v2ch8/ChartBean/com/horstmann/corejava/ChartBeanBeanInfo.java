/**
  @version 1.10 1997-10-27
  @author Cay Horstmann
*/

package com.horstmann.corejava;

import java.beans.*;

/**
   The bean info for the chart bean, specifying the property editors.
*/
public class ChartBeanBeanInfo extends SimpleBeanInfo
{  
   public PropertyDescriptor[] getPropertyDescriptors()
   {  
      try
      {  
         PropertyDescriptor titlePositionDescriptor
            = new PropertyDescriptor("titlePosition", ChartBean.class);
         titlePositionDescriptor.setPropertyEditorClass(TitlePositionEditor.class);
         PropertyDescriptor inverseDescriptor
            = new PropertyDescriptor("inverse", ChartBean.class);
         inverseDescriptor.setPropertyEditorClass(InverseEditor.class);
         PropertyDescriptor valuesDescriptor 
            = new PropertyDescriptor("values", ChartBean.class);
         valuesDescriptor.setPropertyEditorClass(DoubleArrayEditor.class);

         return new PropertyDescriptor[]
         {  
            new PropertyDescriptor("title", ChartBean.class),
            titlePositionDescriptor, 
            valuesDescriptor,
            new PropertyDescriptor("graphColor", ChartBean.class),
            inverseDescriptor
         };
      }
      catch (IntrospectionException e)
      {  
         e.printStackTrace();
         return null;
      }
   }
}
