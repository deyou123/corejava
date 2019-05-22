package com.horstmann.corejava;
public class ChartBeanBeanInfo extends java.beans.SimpleBeanInfo
{
   public java.beans.PropertyDescriptor[] getPropertyDescriptors()
   {
      try
      {
         java.beans.PropertyDescriptor titleDescriptor
            = new java.beans.PropertyDescriptor("title", com.horstmann.corejava.ChartBean.class);
         java.beans.PropertyDescriptor valuesDescriptor
            = new java.beans.PropertyDescriptor("values", com.horstmann.corejava.ChartBean.class);
         valuesDescriptor.setPropertyEditorClass(DoubleArrayEditor.class);
         java.beans.PropertyDescriptor inverseDescriptor
            = new java.beans.PropertyDescriptor("inverse", com.horstmann.corejava.ChartBean.class);
         inverseDescriptor.setPropertyEditorClass(InverseEditor.class);
         java.beans.PropertyDescriptor titlePositionDescriptor
            = new java.beans.PropertyDescriptor("titlePosition", com.horstmann.corejava.ChartBean.class);
         titlePositionDescriptor.setPropertyEditorClass(TitlePositionEditor.class);
         java.beans.PropertyDescriptor graphColorDescriptor
            = new java.beans.PropertyDescriptor("graphColor", com.horstmann.corejava.ChartBean.class);
         return new java.beans.PropertyDescriptor[]
         {
            titleDescriptor,
            valuesDescriptor,
            inverseDescriptor,
            titlePositionDescriptor,
            graphColorDescriptor
         };
      }
      catch (java.beans.IntrospectionException e)
      {
         e.printStackTrace();
         return null;
      }
   }
}
