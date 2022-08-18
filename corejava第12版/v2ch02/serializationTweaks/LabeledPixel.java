package serializationTweaks;

import java.awt.*;
import java.io.*;
import java.awt.geom.*;

public class LabeledPixel extends Point2D.Double implements Externalizable
{
   private String label;

   public LabeledPixel()
   {
      System.out.println("no-arg constructor");
   }
   
   public LabeledPixel(String label, int x, int y)
   {
      super(x, y);
      this.label = label;
   }

   public void writeExternal(ObjectOutput out)
      throws IOException
   {
      System.out.println("writeExternal");
      out.writeInt((int) getX());
      out.writeInt((int) getY());
      out.writeUTF(label);
   }

   public void readExternal(ObjectInput in)
      throws IOException, ClassNotFoundException
   {
      int x = in.readInt();
      int y = in.readInt();
      setLocation(x, y);
      label = in.readUTF();
   }

   public String getLabel()
   {
      return label;
   }

   public String toString()
   {
      return "%s[label=%s]".formatted(super.toString(), label);
   }
}
