package jaas;

import java.awt.*;

import java.security.*;
import javax.security.auth.*;
import javax.security.auth.login.*;
import javax.swing.*;

/**
 * This frame has text fields for user name and password, and a field to show the role.
 */
public class JAASFrame extends JFrame
{
   private JTextField username;
   private JPasswordField password;
   private JTextArea principals;

   public JAASFrame()
   {
      username = new JTextField(20);
      password = new JPasswordField(20);
      principals = new JTextArea(1, 20);

      var panel = new JPanel();
      panel.setLayout(new GridLayout(0, 2));
      panel.add(new JLabel("username:"));
      panel.add(username);
      panel.add(new JLabel("password:"));
      panel.add(password);
      panel.add(new JLabel("principals:"));
      panel.add(principals);
      add(panel, BorderLayout.CENTER);

      var loginButton = new JButton("Login");
      loginButton.addActionListener(event -> login());
      var buttonPanel = new JPanel();
      buttonPanel.add(loginButton);
      add(buttonPanel, BorderLayout.SOUTH);
      pack();
   }

   public void login()
   {
      try
      {
         var context = new LoginContext("Login1", new SimpleCallbackHandler(
            username.getText(), password.getPassword()));
         context.login();
         Subject subject = context.getSubject();
         for (Principal p : subject.getPrincipals())
         {
            principals.append(p.getName() + " ");
         }
         context.logout();
      }
      catch (LoginException e)
      {
         e.printStackTrace();
         Throwable cause = e.getCause();
         if (cause != null) cause.printStackTrace();         
      }
   }
}
