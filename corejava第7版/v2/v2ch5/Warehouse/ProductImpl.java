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
      @param s the suggested sex (MALE, FEMALE, or BOTH)
      @param age1 the lower bound for the suggested age
      @param age2 the upper bound for the suggested age
      @param h the hobby matching this product
   */
   public ProductImpl(String n, int s, int age1, int age2,
      String h) throws RemoteException
   {  
      name = n;
      ageLow = age1;
      ageHigh = age2;
      sex = s;
      hobby = h;
   }

   /**
      Checks whether this product is a good match for a 
      customer. Note that this method is a local method since
      it is not part of the Product interface.
      @param c the customer to match against this product
      @return true if this product is appropriate for the 
      customer
   */
   public boolean match(Customer c)
   {  
      if (c.getAge() < ageLow || c.getAge() > ageHigh)
         return false;
      if (!c.hasHobby(hobby)) return false;
      if ((sex & c.getSex()) == 0) return false;
      return true;
   }

   public String getDescription() throws RemoteException
   {  
      return "I am a " + name + ". Buy me!";
   }

   private String name;
   private int ageLow;
   private int ageHigh;
   private int sex;
   private String hobby;
}
