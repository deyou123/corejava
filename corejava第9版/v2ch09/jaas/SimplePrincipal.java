package jaas;

import java.security.*;
import java.util.*;

/**
 * A principal with a named value (such as "role=HR" or "username=harry").
 */
public class SimplePrincipal implements Principal
{
   private String descr;
   private String value;

   /**
    * Constructs a SimplePrincipal to hold a description and a value.
    * @param descr the description 
    * @param value the associated value
    */
   public SimplePrincipal(String descr, String value)
   {
      this.descr = descr;
      this.value = value;
   }

   /**
    * Returns the role name of this principal.
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
      return Objects.equals(getName(), other.getName());
   }

   public int hashCode()
   {
      return Objects.hashCode(getName());
   }
}
