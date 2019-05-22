/**
   @version 1.00 1999-08-21
   @author Cay Horstmann
*/

import org.omg.CosNaming.*;
import org.omg.CORBA.*;

public class EnvClient
{  
   public static void main(String args[])
   {  
      try
      {  
         ORB orb = ORB.init(args, null);
         org.omg.CORBA.Object namingContextObj = orb.resolve_initial_references("NameService");
         NamingContext namingContext = NamingContextHelper.narrow(namingContextObj);

         NameComponent[] path = 
            { 
               new NameComponent("corejava", "Context"),
               new NameComponent("Env", "Object") 
            };
         org.omg.CORBA.Object envObj = namingContext.resolve(path);
         Env env = EnvHelper.narrow(envObj);
         System.out.println(env.getenv("PATH"));
      }
      catch (Exception e)
      {   
         e.printStackTrace();
      }
   }
}


