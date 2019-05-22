import java.awt.*;
import java.awt.event.*;
import javax.security.auth.*;
import javax.security.auth.login.*;
import javax.swing.*;

/**
 * This program authenticates a user via a custom login and then executes the SysPropAction with the
 * user's privileges.
 * @version 1.0 2004-09-14
 * @author Cay Horstmann
 */
public class JAASTest
{
   public static void main(final String[] args)
   {
      System.setSecurityManager(new SecurityManager());
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new JAASFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame has text fields for user name and password, a field for the name of the requested
 * system property, and a field to show the property value.
 */
class JAASFrame extends JFrame
{
   public JAASFrame()
   {
      setTitle("JAASTest");

      username = new JTextField(20);
      password = new JPasswordField(20);
      propertyName = new JTextField(20);
      propertyValue = new JTextField(20);
      propertyValue.setEditable(false);

      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0, 2));
      panel.add(new JLabel("username:"));
      panel.add(username);
      panel.add(new JLabel("password:"));
      panel.add(password);
      panel.add(propertyName);
      panel.add(propertyValue);
      add(panel, BorderLayout.CENTER);

      JButton getValueButton = new JButton("Get Value");
      getValueButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               getValue();
            }
         });
      JPanel buttonPanel = new JPanel();
      buttonPanel.add(getValueButton);
      add(buttonPanel, BorderLayout.SOUTH);
      pack();
   }

   public void getValue()
   {
      try
      {
         LoginContext context = new LoginContext("Login1", new SimpleCallbackHandler(username
               .getText(), password.getPassword()));
         context.login();
         Subject subject = context.getSubject();
         propertyValue.setText(""
               + Subject.doAsPrivileged(subject, new SysPropAction(propertyName.getText()), null));
         context.logout();
      }
      catch (LoginException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   private JTextField username;
   private JPasswordField password;
   private JTextField propertyName;
   private JTextField propertyValue;
}
