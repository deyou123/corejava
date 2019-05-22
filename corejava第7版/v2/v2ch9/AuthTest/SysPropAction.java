import java.security.*;

/**
   This action looks up a system property.
*/
public class SysPropAction implements PrivilegedAction
{
   /**
      Constructs an action for looking up a given property.
      @param propertyName the property name (such as "user.home")
   */
   public SysPropAction(String propertyName) { this.propertyName = propertyName; }

   public Object run()
   {
      return System.getProperty(propertyName);
   }

   private String propertyName;
}
