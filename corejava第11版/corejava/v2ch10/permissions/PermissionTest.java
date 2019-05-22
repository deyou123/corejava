'package permissions;

import java.awt.*;

import javax.swing.*;

/**
 * This class demonstrates the custom WordCheckPermission.
 * @version 1.05 2018-05-01
 * @author Cay Horstmann
 */
public class PermissionTest
{
   public static void main(String[] args)
   {
      System.setProperty("java.security.policy", "permissions/PermissionTest.policy");      
      System.setSecurityManager(new SecurityManager());
      EventQueue.invokeLater(() ->
         {
            var frame = new PermissionTestFrame();
            frame.setTitle("PermissionTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * This frame contains a text field for inserting words into a text area that is protected
 * from "bad words".
 */
class PermissionTestFrame extends JFrame
{
   private JTextField textField;
   private WordCheckTextArea textArea;
   private static final int TEXT_ROWS = 20;
   private static final int TEXT_COLUMNS = 60;

   public PermissionTestFrame()
   {
      textField = new JTextField(20);
      var panel = new JPanel();
      panel.add(textField);
      var openButton = new JButton("Insert");
      panel.add(openButton);
      openButton.addActionListener(event -> insertWords(textField.getText()));

      add(panel, BorderLayout.NORTH);

      textArea = new WordCheckTextArea();
      textArea.setRows(TEXT_ROWS);
      textArea.setColumns(TEXT_COLUMNS);
      add(new JScrollPane(textArea), BorderLayout.CENTER);
      pack();
   }

   /**
    * Tries to insert words into the text area. Displays a dialog if the attempt fails.
    * @param words the words to insert
    */
   public void insertWords(String words)
   {
      try
      {
         textArea.append(words + "\n");
      }
      catch (SecurityException ex)
      {
         JOptionPane.showMessageDialog(this, "I am sorry, but I cannot do that.");
         ex.printStackTrace();
      }
   }
}

/**
 * A text area whose append method makes a security check to see that no bad words are added.
 */
class WordCheckTextArea extends JTextArea
{
   public void append(String text)
   {
      var p = new WordCheckPermission(text, "insert");
      SecurityManager manager = System.getSecurityManager();
      if (manager != null) manager.checkPermission(p);
      super.append(text);
   }
}
