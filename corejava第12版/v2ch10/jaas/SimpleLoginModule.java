package jaas;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import javax.security.auth.*;
import javax.security.auth.callback.*;
import javax.security.auth.login.*;
import javax.security.auth.spi.*;

/**
 * This login module authenticates users by reading usernames, passwords, and roles from
 * a text file.
 */
public class SimpleLoginModule implements LoginModule
{
   private Subject subject;
   private CallbackHandler callbackHandler;
   private Map<String, ?> options;

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

      var nameCall = new NameCallback("username: ");
      var passCall = new PasswordCallback("password: ", false);
      try
      {
         callbackHandler.handle(new Callback[] { nameCall, passCall });
      }
      catch (UnsupportedCallbackException e)
      {
         var e2 = new LoginException("Unsupported callback");
         e2.initCause(e);
         throw e2;
      }
      catch (IOException e)
      {
         var e2 = new LoginException("I/O exception in callback");
         e2.initCause(e);
         throw e2;
      }

      try
      {
         return checkLogin(nameCall.getName(), passCall.getPassword());
      }
      catch (IOException e)
      {
         var e2 = new LoginException();
         e2.initCause(e);
         throw e2;
      }
   }

   /**
    * Checks whether the authentication information is valid. If it is, the subject acquires
    * principals for the user name and role.
    * @param username the user name
    * @param password a character array containing the password
    * @return true if the authentication information is valid
    */
   private boolean checkLogin(String username, char[] password) 
         throws LoginException, IOException
   {
      try (var in = new Scanner(
            Path.of("" + options.get("pwfile")), StandardCharsets.UTF_8))
      {
         while (in.hasNextLine())
         {
            String[] inputs = in.nextLine().split("\\|");
            if (inputs[0].equals(username) 
                  && Arrays.equals(inputs[1].toCharArray(), password))
            {
               String role = inputs[2];
               Set<Principal> principals = subject.getPrincipals();
               principals.add(new SimplePrincipal("username", username));
               principals.add(new SimplePrincipal("role", role));
               return true;
            }
         }
         return false;
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
}
