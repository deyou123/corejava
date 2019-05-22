import java.beans.*;

/**
   This class can be used to save any enum type in a JavaBeans archive.
*/
public class EnumDelegate extends DefaultPersistenceDelegate 
{
   protected Expression instantiate(Object oldInstance, Encoder out) 
   {
      return new Expression(Enum.class,
         "valueOf", 
         new Object[] { oldInstance.getClass(), ((Enum) oldInstance).name() });
   }            
}
