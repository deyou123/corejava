package util;

import java.util.*;
import java.util.function.*;

public class Choices
{
   /**
    * Choose from an array of choices.
    * @param in the scanner from which to read the choice index
    * @param choices the objects to choose from
    * @param formatter a function that produces a description from a choice
    * @return the chosen object
    */
   public static <T> T choose(Scanner in, T[] choices,
         Function<T, String> formatter)
   {
      for (int i = 0; i < choices.length; i++)
      {
         System.out.printf("%2d: %s%n", i + 1, formatter.apply(choices[i]));
      }
      while (true)
      {
         System.out.print("Your choice: ");
         if (in.hasNextInt())
         {
            int choice = in.nextInt();
            if (0 < choice && choice <= choices.length)
            {
               in.nextLine(); // Consume newline
               return choices[choice - 1];
            }
         }
         in.nextLine(); // Consume bad input
      }
   }

   /**
    * Chooses among static fields of a class.
    * @param in the scanner from which to read the choice index
    * @param cl a class
    * @param labels an array of strings describing static field names of cl that
    *        have type T
    * @return the chosen value
    */
   public static <T> T choose(Scanner in, Class<?> cl, String... labels)
   {
      String label = choose(in, labels, Function.identity());
      String name = label.toUpperCase().replace(' ', '_');
      try
      {
         java.lang.reflect.Field f = cl.getField(name);
         @SuppressWarnings("unchecked")
         T value = (T) f.get(cl);
         return value;
      }
      catch (Exception e)
      {
         System.err.println("Unmatched label " + label);
         return null;
      }
   }

   /**
    * Choose from a map of choices to descriptions.
    * @param in the scanner from which to read the choice index
    * @param choices a map from description strings to choices
    * @return the chosen object
    */
   public static <T> T choose(Scanner in, Map<T, String> choices)
   {
      Object[] objects = choices.keySet().toArray();
      @SuppressWarnings("unchecked")
      T choice = (T) choose(in, objects, o -> choices.get((T) o));
      return choice;
   }

   /**
    * Choose from strings.
    * @param in the scanner from which to read the choice index
    * @param choices the string choices
    * @return the chosen string
    */
   public static String choose(Scanner in, String... choices)
   {
      return choose(in, choices, Function.identity());
   }
}
