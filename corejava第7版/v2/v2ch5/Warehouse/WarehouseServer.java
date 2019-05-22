/**
   @version 1.11 2001-07-10
   @author Cay Horstmann
*/

import java.rmi.*;
import java.rmi.server.*;
import javax.naming.*;

/**
   This server program instantiates a remote warehouse
   objects, registers it with the naming service, and waits 
   for clients to invoke methods.
*/
public class WarehouseServer
{  
   public static void main(String[] args)
   {  
      try
      {  
         System.out.println("Constructing server implementations...");
         WarehouseImpl w = new WarehouseImpl();
         w.add(new ProductImpl("Blackwell Toaster", Product.BOTH, 18, 200, "Household"));
         w.add(new ProductImpl("ZapXpress Microwave Oven", Product.BOTH, 18, 200, "Household"));
         w.add(new ProductImpl("DirtDigger Steam Shovel", Product.MALE, 20, 60, "Gardening"));
         w.add(new ProductImpl("U238 Weed Killer", Product.BOTH, 20, 200, "Gardening"));
         w.add(new ProductImpl("Persistent Java Fragrance", Product.FEMALE, 15, 45, "Beauty"));
         w.add(new ProductImpl("Rabid Rodent Computer Mouse", Product.BOTH, 6, 40, "Computers"));
         w.add(new ProductImpl("My first Espresso Maker", Product.FEMALE, 6, 10, "Household"));
         w.add(new ProductImpl("JavaJungle Eau de Cologne", Product.MALE, 15, 45, "Beauty"));
         w.add(new ProductImpl("FireWire Espresso Maker", Product.BOTH, 20, 50, "Computers"));
         w.add(new ProductImpl("Learn Bad Java Habits in 21 Days Book", Product.BOTH, 20, 200, 
            "Computers"));

         System.out.println("Binding server implementations to registry...");
         Context namingContext = new InitialContext();
         namingContext.bind("rmi:central_warehouse", w);

         System.out.println("Waiting for invocations from clients...");
      }
      catch (Exception e)
      {  
         e.printStackTrace();
      }
   }
}

