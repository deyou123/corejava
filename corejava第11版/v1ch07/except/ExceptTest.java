package except;

import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @version 1.40 2018-03-17
 * @author Cay Horstmann
 */
public class ExceptTest
{
   public static void main(String[] args)
   {
      int thousand = 1000;
      double[] a = { 1000, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
      performAction("Integer divide by zero", () -> 1 / (a.length - a.length));

      performAction("Floating point divide by zero",
            () -> a[2] / (a[3] - a[3]));

      performAction("Integer overflow",
            () -> thousand * thousand * thousand * thousand);

      performAction("Square root of negative number", () -> Math.sqrt(-1));

      performAction("Array index out of bounds", () -> a[1] - a[100]);

      performAction("Bad cast", () -> (int[]) (Object) a);

      performAction("Null pointer",
            () -> System.getProperty("woozle").toString());

      performAction("No such file",
            () -> new Scanner(Paths.get("woozle.txt"), StandardCharsets.UTF_8).next());
   }

   /**
    * Performs the given action and reports the result or failure.
    * @param description the description of the action
    * @param action the action to be carried out
    */
   private static void performAction(String description,
         Callable<Object> action)
   {
      System.out.println(description);
      try
      {
         System.out.println(action.call());
      }
      catch (Throwable t)
      {
         System.out.println(t.getClass().getName() + ": " + t.getMessage());
      }
   }
}
