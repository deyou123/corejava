package serializationTweaks;

import java.awt.*;
import java.io.*;

/**
 * @version 1.0 2021-09-09
 * @author Cay Horstmann
 */
class ObjectStreamTest
{
   public static void main(String[] args) throws IOException, ClassNotFoundException
   {
      var harry = new Employee("Harry Hacker", 50000, 1989, 10, 1);
      var lp = new LabeledPoint("Rome", 41.902782, 12.496366);
      var lp2 = new LabeledPixel("bottom right", 1919, 1079);
      var cp = new ColoredPoint(Color.PINK, 3, 4);
      try (var out = new ObjectOutputStream(new FileOutputStream("test.ser"))) 
      {
         out.writeObject(harry);
         out.writeObject(lp);
         out.writeObject(lp2);
         out.writeObject(cp);
      }

      try (var in = new ObjectInputStream(new FileInputStream("test.ser")))
      {
         harry = (Employee) in.readObject();
         System.out.println(harry);
         lp = (LabeledPoint) in.readObject();
         System.out.println(lp);
         lp2 = (LabeledPixel) in.readObject();
         System.out.println(lp2);
         cp = (ColoredPoint) in.readObject();
         System.out.println(cp);
      }
   }
}
