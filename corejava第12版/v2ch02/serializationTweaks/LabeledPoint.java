package serializationTweaks;

import java.io.*;
import java.awt.geom.*;

public class LabeledPoint implements Serializable
{
   private String label;
   private transient Point2D.Double point;

   public LabeledPoint(String label, double x, double y)
   {
      this.label = label;
      this.point = new Point2D.Double(x, y);
   }

   @Serial private void writeObject(ObjectOutputStream out)
      throws IOException
   {
      System.out.println("writeObject");
      out.defaultWriteObject();
      out.writeDouble(point.getX());
      out.writeDouble(point.getY());
   }

   @Serial private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException
   {
      System.out.println("readObject");
      in.defaultReadObject();
      double x = in.readDouble();
      double y = in.readDouble();
      point = new Point2D.Double(x, y);
   }

   public String getLabel()
   {
      return label;
   }

   public Point2D.Double getPoint()
   {
      return point;
   }

   public String toString()
   {
      return "%s[label=%s,point=%s]".formatted(getClass(), label, point);
   }
}
