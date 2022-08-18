package arrays;

import java.lang.reflect.*;
import java.util.*;

/**
 * This program demonstrates the use of reflection for manipulating arrays.
 * @version 1.2 2012-05-04
 * @author Cay Horstmann
 */
public class CopyOfTest
{
   public static void main(String[] args)
   {
      int[] a = { 1, 2, 3 };
      a = (int[]) goodCopyOf(a, 10);
      System.out.println(Arrays.toString(a));

      String[] b = { "Tom", "Dick", "Harry" };
      b = (String[]) goodCopyOf(b, 10);
      System.out.println(Arrays.toString(b));

      System.out.println("The following call will generate an exception.");
      b = (String[]) badCopyOf(b, 10);
   }

   /**
    * This method attempts to grow an array by allocating a new array and
    * copying all elements.
    * @param a the array to grow
    * @param newLength the new length
    * @return a larger array that contains all elements of a. However, the returned 
    * array has type Object[], not the same type as a
    */
   public static Object[] badCopyOf(Object[] a, int newLength) // not useful
   {
      var newArray = new Object[newLength];
      System.arraycopy(a, 0, newArray, 0, Math.min(a.length, newLength));
      return newArray;
   }

   /**
    * This method grows an array by allocating a new array of the same type and
    * copying all elements.
    * @param a the array to grow. This can be an object array or a primitive
    * type array
    * @return a larger array that contains all elements of a
    */
   public static Object goodCopyOf(Object a, int newLength) 
   {
      Class cl = a.getClass();
      if (!cl.isArray()) return null;
      Class componentType = cl.getComponentType();
      int length = Array.getLength(a);
      Object newArray = Array.newInstance(componentType, newLength);
      System.arraycopy(a, 0, newArray, 0, Math.min(length, newLength));
      return newArray;
   }
}
