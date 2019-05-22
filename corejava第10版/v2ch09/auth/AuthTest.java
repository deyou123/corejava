package auth;

import java.security.*;
import javax.security.auth.*;
import javax.security.auth.login.*;

/**
 * This program authenticates a user via a custom login and then executes the SysPropAction with the
 * user's privileges.
 * @version 1.01 2007-10-06
 * @author Cay Horstmann
 */
public class AuthTest
{
   public static void main(final String[] args)
   {
      System.setSecurityManager(new SecurityManager());
      try
      {
         LoginContext context = new LoginContext("Login1");
         context.login();
         System.out.println("Authentication successful.");
         Subject subject = context.getSubject();
         System.out.println("subject=" + subject);
         PrivilegedAction<String> action = new SysPropAction("user.home");
         String result = Subject.doAsPrivileged(subject, action, null);
         System.out.println(result);
         context.logout();
      }
      catch (LoginException e)
      {
         e.printStackTrace();
      }
   }
}
