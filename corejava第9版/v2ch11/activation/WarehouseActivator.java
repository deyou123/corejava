import java.io.*;
import java.rmi.*;
import java.rmi.activation.*;
import java.util.*;
import javax.naming.*;

/**
 * This server program instantiates a remote warehouse object, registers it with the naming
 * service, and waits for clients to invoke methods.
 * @version 1.13 2012-01-26
 * @author Cay Horstmann
 */
public class WarehouseActivator
{
   public static void main(String[] args) throws RemoteException, NamingException,
         ActivationException, IOException
   {
      System.out.println("Constructing activation descriptors...");

      Properties props = new Properties();
      // use the server.policy file in the current directory
      props.put("java.security.policy", new File("server.policy").getCanonicalPath());
      ActivationGroupDesc group = new ActivationGroupDesc(props, null);
      ActivationGroupID id = ActivationGroup.getSystem().registerGroup(group);

      Map<String, Double> prices = new HashMap<>();
      prices.put("Blackwell Toaster", 24.95);
      prices.put("ZapXpress Microwave Oven", 49.95);

      MarshalledObject<Map<String, Double>> param = new MarshalledObject<Map<String, Double>>(
            prices);

      String codebase = "http://localhost:8080/";

      ActivationDesc desc = new ActivationDesc(id, "WarehouseImpl", codebase, param);

      Warehouse centralWarehouse = (Warehouse) Activatable.register(desc);

      System.out.println("Binding activable implementation to registry...");
      Context namingContext = new InitialContext();
      namingContext.bind("rmi:central_warehouse", centralWarehouse);
      System.out.println("Exiting...");
   }
}
