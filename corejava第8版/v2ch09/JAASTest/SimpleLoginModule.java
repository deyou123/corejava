import java.io.*;
import java.security.*;
import java.util.*;
import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;

/**
 * This login module authenticates users by reading usernames, passwords, and roles from a text
 * file.
 * @version 1.0 2004-09-14
 * @author Cay Horstmann
 */
public class SimpleLoginModule implements LoginModule
{
   public void initialize(Subject subject, CallbackHandler callbackHandler,
         Map<String, ?> sharedState, Map<String, ?> options)
   {
      this.subject = subject;
      this.callbackHandler = callbackHandler;
      this.options = options;
   }

   public boolean login() throws LoginException
   {
      if (callbackHandler == null) throw new LoginException("no handler");

      NameCallback nameCall = new NameCallback("username: ");
      PasswordCallback passCall = new PasswordCallback("password: ", false);
      try
      {
         callbackHandler.handle(new Callback[] { nameCall, passCall });
      }
      catch (UnsupportedCallbackException e)
      {
         LoginException e2 = new LoginException("Unsupported callback");
         e2.initCause(e);
         throw e2;
      }
      catch (IOException e)
      {
         LoginException e2 = new LoginException("I/O exception in callback");
         e2.initCause(e);
         throw e2;
      }

      return checkLogin(nameCall.getName(), passCall.getPassword());
   }

   /**
    * Checks whether the authentication information is valid. If it is, the subject acquires
    * principals for the user name and role.
    * @param username the user name
    * @param password a character array containing the password
    * @return true if the authentication information is valid
    */
   private boolean checkLogin(String username, char[] password) throws LoginException
   {
      try
      {
         Scanner in = new Scanner(new FileReader("" + options.get("pwfile")));
         while (in.hasNextLine())
         {
            String[] inputs = in.nextLine().split("\\|");
            if (inputs[0].equals(username) && Arrays.equals(inputs[1].toCharArray(), password))
            {
               String role = inputs[2];
               Set<Principal> principals = subject.getPrincipals();
               principals.add(new SimplePrincipal("username", username));
               principals.add(new SimplePrincipal("role", role));
               return true;
            }
         }
         in.close();
         return false;
      }
      catch (IOException e)
      {
         LoginException e2 = new LoginException("Can't open password file");
         e2.initCause(e);
         throw e2;
      }
   }

   public boolean logout()
   {
      return true;
   }

   public boolean abort()
   {
      return true;
   }

   public boolean commit()
   {
      return true;
   }

   private Subject subject;
   private CallbackHandler callbackHandler;
   private Map<String, ?> options;
}
