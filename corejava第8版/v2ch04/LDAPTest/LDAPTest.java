import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.swing.*;

/**
 * This program demonstrates access to a hierarchical database through LDAP
 * @version 1.01 2007-06-28
 * @author Cay Horstmann
 */
public class LDAPTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new LDAPFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * The frame that holds the data panel and the navigation buttons.
 */
class LDAPFrame extends JFrame
{
   public LDAPFrame()
   {
      setTitle("LDAPTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JPanel northPanel = new JPanel();
      northPanel.setLayout(new java.awt.GridLayout(1, 2, 3, 1));
      northPanel.add(new JLabel("uid", SwingConstants.RIGHT));
      uidField = new JTextField();
      northPanel.add(uidField);
      add(northPanel, BorderLayout.NORTH);

      JPanel buttonPanel = new JPanel();
      add(buttonPanel, BorderLayout.SOUTH);

      findButton = new JButton("Find");
      findButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               findEntry();
            }
         });
      buttonPanel.add(findButton);

      saveButton = new JButton("Save");
      saveButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               saveEntry();
            }
         });
      buttonPanel.add(saveButton);

      deleteButton = new JButton("Delete");
      deleteButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               deleteEntry();
            }
         });
      buttonPanel.add(deleteButton);

      addWindowListener(new WindowAdapter()
         {
            public void windowClosing(WindowEvent event)
            {
               try
               {
                  if (context != null) context.close();
               }
               catch (NamingException e)
               {
                  e.printStackTrace();
               }
            }
         });
   }

   /**
    * Finds the entry for the uid in the text field.
    */
   public void findEntry()
   {
      try
      {
         if (scrollPane != null) remove(scrollPane);
         String dn = "uid=" + uidField.getText() + ",ou=people,dc=mycompany,dc=com";
         if (context == null) context = getContext();
         attrs = context.getAttributes(dn);
         dataPanel = new DataPanel(attrs);
         scrollPane = new JScrollPane(dataPanel);
         add(scrollPane, BorderLayout.CENTER);
         validate();
         uid = uidField.getText();
      }
      catch (NamingException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   /**
    * Saves the changes that the user made.
    */
   public void saveEntry()
   {
      try
      {
         if (dataPanel == null) return;
         if (context == null) context = getContext();
         if (uidField.getText().equals(uid)) // update existing entry
         {
            String dn = "uid=" + uidField.getText() + ",ou=people,dc=mycompany,dc=com";
            Attributes editedAttrs = dataPanel.getEditedAttributes();
            NamingEnumeration<? extends Attribute> attrEnum = attrs.getAll();
            while (attrEnum.hasMore())
            {
               Attribute attr = attrEnum.next();
               String id = attr.getID();
               Attribute editedAttr = editedAttrs.get(id);
               if (editedAttr != null && !attr.get().equals(editedAttr.get())) context
                     .modifyAttributes(dn, DirContext.REPLACE_ATTRIBUTE, new BasicAttributes(id,
                           editedAttr.get()));
            }
         }
         else
         // create new entry
         {
            String dn = "uid=" + uidField.getText() + ",ou=people,dc=mycompany,dc=com";
            attrs = dataPanel.getEditedAttributes();
            Attribute objclass = new BasicAttribute("objectClass");
            objclass.add("uidObject");
            objclass.add("person");
            attrs.put(objclass);
            attrs.put("uid", uidField.getText());
            context.createSubcontext(dn, attrs);
         }

         findEntry();
      }
      catch (NamingException e)
      {
         JOptionPane.showMessageDialog(LDAPFrame.this, e);
         e.printStackTrace();
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(LDAPFrame.this, e);
         e.printStackTrace();
      }
   }

   /**
    * Deletes the entry for the uid in the text field.
    */
   public void deleteEntry()
   {
      try
      {
         String dn = "uid=" + uidField.getText() + ",ou=people,dc=mycompany,dc=com";
         if (context == null) context = getContext();
         context.destroySubcontext(dn);
         uidField.setText("");
         remove(scrollPane);
         scrollPane = null;
         repaint();
      }
      catch (NamingException e)
      {
         JOptionPane.showMessageDialog(LDAPFrame.this, e);
         e.printStackTrace();
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(LDAPFrame.this, e);
         e.printStackTrace();
      }
   }

   /**
    * Gets a context from the properties specified in the file ldapserver.properties
    * @return the directory context
    */
   public static DirContext getContext() throws NamingException, IOException
   {
      Properties props = new Properties();
      FileInputStream in = new FileInputStream("ldapserver.properties");
      props.load(in);
      in.close();

      String url = props.getProperty("ldap.url");
      String username = props.getProperty("ldap.username");
      String password = props.getProperty("ldap.password");

      Hashtable<String, String> env = new Hashtable<String, String>();
      env.put(Context.SECURITY_PRINCIPAL, username);
      env.put(Context.SECURITY_CREDENTIALS, password);
      DirContext initial = new InitialDirContext(env);
      DirContext context = (DirContext) initial.lookup(url);

      return context;
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;

   private JButton findButton;
   private JButton saveButton;
   private JButton deleteButton;

   private JTextField uidField;
   private DataPanel dataPanel;
   private Component scrollPane;

   private DirContext context;
   private String uid;
   private Attributes attrs;
}

/**
 * This panel displays the contents of a result set.
 */
class DataPanel extends JPanel
{
   /**
    * Constructs the data panel.
    * @param attributes the attributes of the given entry
    */
   public DataPanel(Attributes attrs) throws NamingException
   {
      setLayout(new java.awt.GridLayout(0, 2, 3, 1));

      NamingEnumeration<? extends Attribute> attrEnum = attrs.getAll();
      while (attrEnum.hasMore())
      {
         Attribute attr = attrEnum.next();
         String id = attr.getID();

         NamingEnumeration<?> valueEnum = attr.getAll();
         while (valueEnum.hasMore())
         {
            Object value = valueEnum.next();
            if (id.equals("userPassword")) value = new String((byte[]) value);

            JLabel idLabel = new JLabel(id, SwingConstants.RIGHT);
            JTextField valueField = new JTextField("" + value);
            if (id.equals("objectClass")) valueField.setEditable(false);
            if (!id.equals("uid"))
            {
               add(idLabel);
               add(valueField);
            }
         }
      }
   }

   public Attributes getEditedAttributes()
   {
      Attributes attrs = new BasicAttributes();
      for (int i = 0; i < getComponentCount(); i += 2)
      {
         JLabel idLabel = (JLabel) getComponent(i);
         JTextField valueField = (JTextField) getComponent(i + 1);
         String id = idLabel.getText();
         String value = valueField.getText();
         if (id.equals("userPassword")) attrs.put("userPassword", value.getBytes());
         else if (!id.equals("") && !id.equals("objectClass")) attrs.put(id, value);
      }
      return attrs;
   }
}
