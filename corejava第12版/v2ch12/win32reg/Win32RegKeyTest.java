import java.util.*;

/**
   @version 1.04 2021-05-30
   @author Cay Horstmann
*/
public class Win32RegKeyTest
{  
   public static void main(String[] args)
   {  
      var key = new Win32RegKey(
         Win32RegKey.HKEY_CURRENT_USER, "Software\\JavaSoft\\Java Runtime Environment");

      key.setValue("Default user", "Harry Hacker");
      key.setValue("Lucky number", Integer.valueOf(13));
      key.setValue("Small primes", new byte[] { 2, 3, 5, 7, 11 });

      Enumeration<String> e = key.names();

      while (e.hasMoreElements())
      {  
         String name = e.nextElement();
         System.out.print(name + "=");

         Object value = key.getValue(name);

         if (value instanceof byte[] bytes)
            for (byte b : bytes) System.out.print((b & 0xFF) + " ");
         else 
            System.out.print(value);

         System.out.println();
      }
   }
}
