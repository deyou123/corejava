import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

/**
 * This class is the implementation for the remote Warehouse interface.
 * @version 1.01 2012-01-26
 * @author Cay Horstmann
 */
public class WarehouseImpl extends UnicastRemoteObject implements Warehouse
{
   private Map<String, Double> prices;

   public WarehouseImpl() throws RemoteException
   {
      prices = new HashMap<>();
      prices.put("Blackwell Toaster", 24.95);
      prices.put("ZapXpress Microwave Oven", 49.95);
   }

   public double getPrice(String description) throws RemoteException
   {
      Double price = prices.get(description);
      return price == null ? 0 : price;
   }
}
