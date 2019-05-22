/**
   @version 1.00 1996-09-07
   @author Cay Horstmann
*/

import java.rmi.*;
import java.rmi.server.*;

/**
   This is the implementation class for the remote product
   objects.
*/
public class ProductImpl
   extends UnicastRemoteObject
   implements Product
{ 
   /**
      Constructs a product implementation
      @param n the product name
   */
   public ProductImpl(String n) throws RemoteException
   {  
      name = n;
   }

   public String getDescription() throws RemoteException
   {  
      return "I am a " + name + ". Buy me!";
   }

   private String name;
}
