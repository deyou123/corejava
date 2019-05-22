/**
   @version 1.10 2001-07-15
   @author Cay Horstmann
*/

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

class SysPropImpl extends SysPropPOA
{  
   public String getProperty(String key)
   {  
      return System.getProperty(key);
   }
}

public class SysPropServer
{  
   public static void main(String args[])
   {  
      try
      {  
         System.out.println("Creating and initializing the ORB...");

         ORB orb = ORB.init(args, null);

         System.out.println("Registering server implementation with the ORB...");
         
         POA rootpoa = (POA) orb.resolve_initial_references("RootPOA");
         rootpoa.the_POAManager().activate();

         SysPropImpl impl = new SysPropImpl();
         org.omg.CORBA.Object ref = rootpoa.servant_to_reference(impl);
         
         System.out.println(orb.object_to_string(ref));

         org.omg.CORBA.Object namingContextObj = orb.resolve_initial_references("NameService");
         NamingContext namingContext = NamingContextHelper.narrow(namingContextObj);
         NameComponent[] path =
            {  
               new NameComponent("SysProp", "Object")
            };

         System.out.println("Binding server implemenation to name service...");
         namingContext.rebind(path, ref);

         System.out.println("Waiting for invocations from clients...");
         orb.run();
      }
      catch (Exception e)
      {   
         e.printStackTrace(System.out);
      }
   }
}
