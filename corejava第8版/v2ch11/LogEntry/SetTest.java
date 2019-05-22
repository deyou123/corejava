import java.util.*;
import java.util.logging.*;

/**
 * @version 1.01 2007-10-27
 * @author Cay Horstmann
 */
public class SetTest
{  
   public static void main(String[] args)
   {  
      Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.FINEST);
      Handler handler = new ConsoleHandler();
      handler.setLevel(Level.FINEST);
      Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).addHandler(handler);

      Set<Item> parts = new HashSet<Item>();
      parts.add(new Item("Toaster", 1279));
      parts.add(new Item("Microwave", 4104));
      parts.add(new Item("Toaster", 1279));
      System.out.println(parts);
   }
}

