package dataExchange;

/**
 * A user has a name and password. For security reasons, the password is stored as a char[], not a
 * String.
 */
public class User
{
   private String name;
   private char[] password;

   public User(String aName, char[] aPassword)
   {
      name = aName;
      password = aPassword;
   }

   public String getName()
   {
      return name;
   }

   public char[] getPassword()
   {
      return password;
   }

   public void setName(String aName)
   {
      name = aName;
   }

   public void setPassword(char[] aPassword)
   {
      password = aPassword;
   }
}
