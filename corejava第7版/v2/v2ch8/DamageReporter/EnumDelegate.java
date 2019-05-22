/**
   @version 1.21 2004-08-30
   @author Cay Horstmann
*/

import java.beans.*;

public class EnumDelegate extends DefaultPersistenceDelegate 
{
   protected Expression instantiate(Object oldInstance, Encoder out) 
   {
      return new Expression(Enum.class,
         "valueOf", 
         new Object[] { oldInstance.getClass(), ((Enum) oldInstance).name() });
   }            

   protected boolean mutatesTo(Object oldInstance, Object newInstance) 
   {
      return oldInstance == newInstance;
   }
}
