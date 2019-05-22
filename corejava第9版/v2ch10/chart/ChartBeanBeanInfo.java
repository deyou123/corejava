package chart;
public class ChartBeanBeanInfo extends java.beans.SimpleBeanInfo
{
   public java.beans.PropertyDescriptor[] getPropertyDescriptors()
   {
      try
      {
         java.beans.PropertyDescriptor titleDescriptor
            = new java.beans.PropertyDescriptor("title", chart.ChartBean.class);
         java.beans.PropertyDescriptor valuesDescriptor
            = new java.beans.PropertyDescriptor("values", chart.ChartBean.class);
         valuesDescriptor.setPropertyEditorClass(DoubleArrayEditor.class);
         java.beans.PropertyDescriptor inverseDescriptor
            = new java.beans.PropertyDescriptor("inverse", chart.ChartBean.class);
         inverseDescriptor.setPropertyEditorClass(InverseEditor.class);
         java.beans.PropertyDescriptor titlePositionDescriptor
            = new java.beans.PropertyDescriptor("titlePosition", chart.ChartBean.class);
         titlePositionDescriptor.setPropertyEditorClass(TitlePositionEditor.class);
         java.beans.PropertyDescriptor graphColorDescriptor
            = new java.beans.PropertyDescriptor("graphColor", chart.ChartBean.class);
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
