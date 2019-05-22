package limitations;

import java.util.*;

public class NoGenericArray
{
   public static <T extends Comparable> T[] minmax(T... a)
   {
      var mm = new Comparable[2];
      T min = a[0];
      T max = a[0];
      for (int i = 1; i < a.length; i++)
      {
         if (min.compareTo(a[i]) > 0) min = a[i];
         if (max.compareTo(a[i]) < 0) max = a[i];
      }
      mm[0] = min;
      mm[1] = max;
      return (T[]) mm; // compiles with warning
   }
   
   public static void main(String[] args)
   {
      String[] fl = minmax("Tom", "Dick", "Harry");
      System.out.println(Arrays.toString(fl));
   }
}
