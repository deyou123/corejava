import java.awt.*;
import java.awt.geom.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
   This program demonstrates various persistence delegates. 
*/
public class PersistenceDelegateTest
{
   public enum Mood { SAD, HAPPY };

   public static void main(String[] args) throws Exception
   {
      XMLEncoder out = new XMLEncoder(System.out);
      out.setExceptionListener(new
         ExceptionListener()
         {
            public void exceptionThrown(Exception e)
            {
               e.printStackTrace();
            }
         });

      PersistenceDelegate delegate = new 
         DefaultPersistenceDelegate()
         {
            protected Expression instantiate(Object oldInstance, Encoder out) 
            {
               Employee e = (Employee) oldInstance;
               GregorianCalendar c = new GregorianCalendar();
               c.setTime(e.getHireDay());
               return new Expression(oldInstance, Employee.class, "new",
                  new Object[] 
                  { 
                     e.getName(), 
                     e.getSalary(), 
                     c.get(Calendar.YEAR), 
                     c.get(Calendar.MONTH), 
                     c.get(Calendar.DATE)
                  });
            }
         };

      out.setPersistenceDelegate(Employee.class, delegate);

      out.setPersistenceDelegate(Rectangle2D.Double.class,
         new DefaultPersistenceDelegate(new String[] { "x", "y", "width", "height" }));

      out.setPersistenceDelegate(Inet4Address.class, new
         DefaultPersistenceDelegate()
         {
            protected Expression instantiate(Object oldInstance, Encoder out) 
            {
               return new Expression(oldInstance, InetAddress.class, "getByAddress", 
                  new Object[] { ((InetAddress) oldInstance).getAddress() });
            }           
         });

      out.setPersistenceDelegate(BitSet.class, new
         DefaultPersistenceDelegate()
         {
            protected void initialize(Class type, Object oldInstance, Object newInstance, 
               Encoder out) 
            {
               super.initialize(type, oldInstance, newInstance, out);
               BitSet bs = (BitSet) oldInstance;        
               for(int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) 
                  out.writeStatement(new Statement(bs, "set", new Object[]{ i, i + 1, true }));
            }
         });

      out.setPersistenceDelegate(Mood.class, new EnumDelegate());

      out.writeObject(new Employee("Harry Hacker", 50000, 1989, 10, 1));
      out.writeObject(new java.awt.geom.Rectangle2D.Double(5, 10, 20, 30));
      out.writeObject(InetAddress.getLocalHost());
      out.writeObject(Mood.SAD); 
      BitSet bs = new BitSet(); bs.set(1, 4); bs.clear(2, 3);
      out.writeObject(bs); 
      out.writeObject(Color.PINK);
      out.writeObject(new GregorianCalendar());
      out.close();
   }

   static
   {
      try 
      {
         BeanInfo info = Introspector.getBeanInfo(GregorianCalendar.class);
         for (PropertyDescriptor desc : info.getPropertyDescriptors()) 
            if (desc.getName().equals("gregorianChange")) 
               desc.setValue("transient", Boolean.TRUE);
      } 
      catch (IntrospectionException e) 
      { 
         e.printStackTrace(); 
      }      
   }
}

