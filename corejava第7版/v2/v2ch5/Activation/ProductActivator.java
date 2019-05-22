/**
   @version 1.20 2004-08-15
   @author Cay Horstmann
*/

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.activation.*;
import java.util.*;
import javax.naming.*;

/**
   This server program activates two remote objects and
   registers them with the naming service.
*/
public class ProductActivator
{  
   public static void main(String args[])
   {  
      try
      {  
         System.out.println("Constructing activation descriptors...");

         Properties props = new Properties();
         // use the server.policy file in the current directory
         props.put("java.security.policy", new File("server.policy").getCanonicalPath());
         ActivationGroupDesc group = new ActivationGroupDesc(props, null);
         ActivationGroupID id = ActivationGroup.getSystem().registerGroup(group);
         MarshalledObject p1param = new MarshalledObject("Blackwell Toaster");
         MarshalledObject p2param = new MarshalledObject("ZapXpress Microwave Oven");

         String classDir = ".";
         // turn the class directory into a file URL
         // for this demo we assume that the classes are in the current dir         
         // we use toURI so that spaces and other special characters in file names are escaped
         String classURL = new File(classDir).getCanonicalFile().toURI().toString();

         ActivationDesc desc1 = new ActivationDesc(id, "ProductImpl", classURL, p1param);
         ActivationDesc desc2 = new ActivationDesc(id, "ProductImpl", classURL, p2param);
         
         Product p1 = (Product) Activatable.register(desc1);
         Product p2 = (Product) Activatable.register(desc2);

         System.out.println("Binding activable implementations to registry...");
         Context namingContext = new InitialContext();
         namingContext.bind("rmi:toaster", p1);
         namingContext.bind("rmi:microwave", p2);         
         System.out.println("Exiting...");
      }
      catch (Exception e)
      {  
         e.printStackTrace();
      }
   }
}

