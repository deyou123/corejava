package genericAlgorithms;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

/**
 * @version 1.00 2015-05-21
 * @author Cay Horstmann
 */
public class GenericAlgorithms
{
   public static void main(String[] args)
   {
      Pair<String> p = Pair.makePair(String::new);
      System.out.println(p);
      
      p = Pair.makePair(String.class);
      System.out.println(p);      
      
      String[] ss = ArrayAlg.minmax("Tom", "Dick", "Harry");
      System.out.println(Arrays.toString(ss));
      
      ss = ArrayAlg.minmax(String[]::new, "Tom", "Dick", "Harry");
      System.out.println(Arrays.toString(ss));      
   }
}

class ArrayAlg
{
   public static <T extends Comparable> T[] minmax(IntFunction<T[]> constr, T... a)
   {
      T[] mm = constr.apply(2);
      T min = a[0];
      T max = a[0];
      for (int i = 1; i < a.length; i++)
      {
         if (min.compareTo(a[i]) > 0) min = a[i];
         if (max.compareTo(a[i]) < 0) max = a[i];
      }
      return mm;
   }
   
   public static <T extends Comparable> T[] minmax(T... a)
   {
      T[] mm = (T[]) Array.newInstance(a.getClass().getComponentType(), 2);
      T min = a[0];
      T max = a[0];
      for (int i = 1; i < a.length; i++)
      {
         if (min.compareTo(a[i]) > 0) min = a[i];
         if (max.compareTo(a[i]) < 0) max = a[i];
      }
      return (T[]) mm; // compiles with warning
   }      
}

