/**
   @version 1.00 1996-09-07
   @author Cay Horstmann
*/

import java.rmi.*;

/**
   The interface for remote product objects.
*/
public interface Product extends Remote
{ 
   /**
      Gets the description of this product.
      @return the product description
   */
   String getDescription() throws RemoteException;

   final int MALE = 1;
   final int FEMALE = 2;
   final int BOTH = MALE + FEMALE;
}
