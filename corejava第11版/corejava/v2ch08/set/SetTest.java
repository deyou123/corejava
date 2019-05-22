package set;

import java.util.*;
import java.util.logging.*;

/**
 * @version 1.03 2018-05-01
 * @author Cay Horstmann
 */
public class SetTest
{  
   public static void main(String[] args)
   {  
      Logger.getLogger("com.horstmann").setLevel(Level.FINEST);
      var handler = new ConsoleHandler();
      handler.setLevel(Level.FINEST);
      Logger.getLogger("com.horstmann").addHandler(handler);

      var parts = new HashSet<Item>();
      parts.add(new Item("Toaster", 1279));
      parts.add(new Item("Microwave", 4104));
      parts.add(new Item("Toaster", 1279));
      System.out.println(parts);
   }
}
