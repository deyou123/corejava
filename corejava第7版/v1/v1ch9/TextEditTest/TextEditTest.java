/**
   @version 1.32 2004-05-05
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class TextEditTest 
{
   public static void main(String[] args)
   {  
      TextEditFrame frame = new TextEditFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame with a text area and components for search/replace.
*/
class TextEditFrame extends JFrame
{  
   public TextEditFrame()
   {  
      setTitle("TextEditTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JPanel panel = new JPanel();

      // add button, text fields and labels
      
      JButton replaceButton = new JButton("Replace");
      panel.add(replaceButton);
      replaceButton.addActionListener(new ReplaceAction());

      from = new JTextField("brown", 8);
      panel.add(from);

      panel.add(new JLabel("with"));

      to = new JTextField("purple", 8);
      panel.add(to);
      add(panel, BorderLayout.SOUTH);
    
      // add text area with scroll bars

      textArea = new JTextArea(8, 40);
      textArea.setText
         ("The quick brown fox jumps over the lazy dog.");
      JScrollPane scrollPane = new JScrollPane(textArea);
      add(scrollPane, BorderLayout.CENTER);
   }
   
   public static final int DEFAULT_WIDTH = 400;
   public static final int DEFAULT_HEIGHT = 200;  

   private JTextArea textArea;
   private JTextField from;
   private JTextField to;

   /**
      The action listener for the replace button.
   */
   private class ReplaceAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {  
         String f = from.getText();
         int n = textArea.getText().indexOf(f);
         if (n >= 0 && f.length() > 0)
            textArea.replaceRange(to.getText(), n, 
               n + f.length());
      }
   }
}

