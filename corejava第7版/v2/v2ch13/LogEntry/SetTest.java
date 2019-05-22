/**
   @version 1.00 2004-08-15
   @author Cay Horstmann
*/

import java.util.*;
import java.util.logging.*;

/**
   This program logs the equals and hashCode method calls when inserting items into a hash set.
*/
public class SetTest
{  
   public static void main(String[] args)
   {  
      Logger.global.setLevel(Level.FINEST);
      Handler handler = new ConsoleHandler();
      handler.setLevel(Level.FINEST);
      Logger.global.addHandler(handler);

      Set<Item> parts = new HashSet<Item>();
      parts.add(new Item("Toaster", 1279));
      parts.add(new Item("Microwave", 4562));
      parts.add(new Item("Toaster", 1279));
      System.out.println(parts);
   }
}

