package persistenceDelegate;

import java.awt.geom.*;
import java.beans.*;
import java.net.*;
import java.util.*;

/**
 * This program demonstrates various persistence delegates.
 * @version 1.01 2007-10-03
 * @author Cay Horstmann
 */
public class PersistenceDelegateTest
{
   public static class Point
   {
      private final int x, y;

      @ConstructorProperties( { "x", "y" })
      public Point(int x, int y)
      {
         this.x = x;
         this.y = y;
      }

      public int getX()
      {
         return x;
      }

      public int getY()
      {
         return y;
      }

   }

   public static void main(String[] args) throws Exception
   {
      PersistenceDelegate delegate = new PersistenceDelegate()
         {
            protected Expression instantiate(Object oldInstance, Encoder out)
            {
               Employee e = (Employee) oldInstance;
               GregorianCalendar c = new GregorianCalendar();
               c.setTime(e.getHireDay());
               return new Expression(oldInstance, Employee.class, "new", new Object[] {
                     e.getName(), e.getSalary(), c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                     c.get(Calendar.DATE) });
            }
         };
      BeanInfo info = Introspector.getBeanInfo(Employee.class);
      info.getBeanDescriptor().setValue("persistenceDelegate", delegate);

      XMLEncoder out = new XMLEncoder(System.out);
      out.setExceptionListener(new ExceptionListener()
         {
            public void exceptionThrown(Exception e)
            {
               e.printStackTrace();
            }
         });

      out.setPersistenceDelegate(Rectangle2D.Double.class, new DefaultPersistenceDelegate(
            new String[] { "x", "y", "width", "height" }));

      out.setPersistenceDelegate(Inet4Address.class, new DefaultPersistenceDelegate()
         {
            protected Expression instantiate(Object oldInstance, Encoder out)
            {
               return new Expression(oldInstance, InetAddress.class, "getByAddress",
                     new Object[] { ((InetAddress) oldInstance).getAddress() });
            }
         });

      out.setPersistenceDelegate(BitSet.class, new DefaultPersistenceDelegate()
         {
            protected void initialize(Class<?> type, Object oldInstance, Object newInstance,
                  Encoder out)
            {
               super.initialize(type, oldInstance, newInstance, out);
               BitSet bs = (BitSet) oldInstance;
               for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1))
                  out.writeStatement(new Statement(bs, "set", new Object[] { i, i + 1, true }));
            }
         });

      out.writeObject(new Employee("Harry Hacker", 50000, 1989, 10, 1));
      out.writeObject(new Point(17, 29));
      out.writeObject(new java.awt.geom.Rectangle2D.Double(5, 10, 20, 30));
      out.writeObject(InetAddress.getLocalHost());
      BitSet bs = new BitSet();
      bs.set(1, 4);
      bs.clear(2, 3);
      out.writeObject(bs);
      out.close();
   }
}
