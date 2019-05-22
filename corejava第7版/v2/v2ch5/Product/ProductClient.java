/**
   @version 1.20 2004-08-15
   @author Cay Horstmann
*/

import java.rmi.*;
import java.rmi.server.*;
import javax.naming.*;

/**
   This program demonstrates how to call a remote method
   on two objects that are located through the naming service.
*/
public class ProductClient
{  
   public static void main(String[] args)
   {  
      System.setProperty("java.security.policy", "client.policy");
      System.setSecurityManager(new RMISecurityManager());
      String url = "rmi://localhost/";
         // change to "rmi://yourserver.com/" when server runs on remote machine yourserver.com
      try
      {  
         Context namingContext = new InitialContext();
         Product c1 = (Product) namingContext.lookup(url + "toaster");
         Product c2 = (Product) namingContext.lookup(url + "microwave");         

         System.out.println(c1.getDescription());
         System.out.println(c2.getDescription());
      }
      catch (Exception e)
      {  
         e.printStackTrace();
      }
   }
}
