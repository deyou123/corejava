/**
   @version 1.02 2004-08-15
   @author Cay Horstmann
*/

import java.rmi.*;
import java.util.*;

/**
   The remote interface for a warehouse with products.
*/
public interface Warehouse extends Remote
{  
   /**
      Gets products that are good matches for a customer.
      @param c the customer to match
      @return an array list of matching products
   */
   ArrayList<Product> find(Customer c) throws RemoteException;
}
