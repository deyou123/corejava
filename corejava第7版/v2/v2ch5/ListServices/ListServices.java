/**
   @version 1.00 1999-08-21
   @author Cay Horstmann
*/

import org.omg.CORBA.*;

/**
   This program lists all initial services supplied by an ORB.
*/
public class ListServices
{  
   public static void main(String args[]) throws Exception
   {  
      ORB orb = ORB.init(args, null);
      String[] services = orb.list_initial_services();
      for (int i = 0; i < services.length; i++)
         System.out.println(services[i]);
   }
}
