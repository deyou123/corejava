import java.util.*;

/**
   This program prints out all system properties.
*/
public class SystemInfo
{  
   public static void main(String args[])
   {   
      Properties systemProperties = System.getProperties();
      Enumeration enum = systemProperties.propertyNames();
      while (enum.hasMoreElements())
      {
         String key = (String)enum.nextElement();
         System.out.println(key + "=" +
            systemProperties.getProperty(key));
      }
   }
}
