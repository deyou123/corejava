/**
   @version 1.00 1996-09-07
   @author Cay Horstmann
*/

import java.io.*;
import java.rmi.*;
import java.rmi.activation.*;

/**
   This is the implementation class for the remote product
   objects.
*/
public class ProductImpl
   extends Activatable
   implements Product
{ 
   /**
      Constructs a product implementation
      @param id the activation id
      @param data the marshalled construction parameter (containing the product name)
   */
   public ProductImpl(ActivationID id, MarshalledObject data) 
      throws RemoteException, IOException, ClassNotFoundException
   {  
      super(id, 0);
      name = (String) data.get();
      System.out.println("Constructed " + name);
   }

   public String getDescription() throws RemoteException
   {  
      return "I am a " + name + ". Buy me!";
   }

   private String name;
}
