/**
   @version 1.10 2004-08-15
   @author Cay Horstmann
*/

import java.io.*;
import java.rmi.*;
import java.util.*;
import java.rmi.server.*;
import java.util.*;
import java.util.concurrent.locks.*;

/**
   This class is the implementation for the remote
   Warehouse interface.
*/
public class WarehouseImpl
   extends UnicastRemoteObject
   implements Warehouse
{  
   /**
      Constructs a warehouse implementation.
   */
   public WarehouseImpl()
      throws RemoteException
   {  
      products = new ArrayList<ProductImpl>();
      add(new ProductImpl("Core Java Book", 0, 200, Product.BOTH, "Computers"));
   }

   /**
      Add a product to the warehouse. Note that this is a local method.
      @param p the product to add
   */
   public void add(ProductImpl p)
   {  
      Lock wlock = rwlock.writeLock();
      wlock.lock();
      try 
      {
         products.add(p);
      }
      finally
      {
         wlock.unlock();
      }
   }

   public ArrayList<Product> find(Customer c)
      throws RemoteException
   {  
      Lock rlock = rwlock.readLock();
      rlock.lock();
      try 
      {
         ArrayList<Product> result = new ArrayList<Product>();
         // add all matching products
         for (ProductImpl p : products)
         {  
            if (p.match(c)) result.add(p);
         }
         // add the product that is a good match for everyone, a copy of Core Java
         if (!result.contains(products.get(0)))
            result.add(products.get(0));
         
         // we reset c just to show that c is a copy of the client object
         c.reset(); 
         return result;
      }
      finally
      {
         rlock.unlock();
      }
   }

   private ArrayList<ProductImpl> products;
   private ReadWriteLock rwlock = new ReentrantReadWriteLock();
}
