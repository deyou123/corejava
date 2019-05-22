import java.security.*;

/**
 * A principal with a named value (such as "role=HR" or "username=harry").
 * @version 1.0 2004-09-14
 * @author Cay Horstmann
 */
public class SimplePrincipal implements Principal
{
   /**
    * Constructs a SimplePrincipal to hold a description and a value.
    * @param roleName the role name
    */
   public SimplePrincipal(String descr, String value)
   {
      this.descr = descr;
      this.value = value;
   }

   /**
    * Returns the role name of this principal
    * @return the role name
    */
   public String getName()
   {
      return descr + "=" + value;
   }

   public boolean equals(Object otherObject)
   {
      if (this == otherObject) return true;
      if (otherObject == null) return false;
      if (getClass() != otherObject.getClass()) return false;
      SimplePrincipal other = (SimplePrincipal) otherObject;
      return getName().equals(other.getName());
   }

   public int hashCode()
   {
      return getName().hashCode();
   }

   private String descr;
   private String value;
}
