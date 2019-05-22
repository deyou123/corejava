/**
   @version 1.32 2004-05-05
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TextAreaTest 
{
   public static void main(String[] args)
   {  
      TextAreaFrame frame = new TextAreaFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame with a text area and buttons for text editing
*/
class TextAreaFrame extends JFrame
{  
   public TextAreaFrame()
   {  
      setTitle("TextAreaTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      buttonPanel = new JPanel();

      // add button to append text into the text area

      JButton insertButton = new JButton("Insert");
      buttonPanel.add(insertButton);
      insertButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               textArea.append("The quick brown fox jumps over the lazy dog. ");
            }
         });

      // add button to turn line wrapping on and off

      wrapButton = new JButton("Wrap");
      buttonPanel.add(wrapButton);
      wrapButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {  
               boolean wrap = !textArea.getLineWrap();
               textArea.setLineWrap(wrap);
               wrapButton.setText(wrap ? "No Wrap" : "Wrap");
            }
         });
    
      add(buttonPanel, BorderLayout.SOUTH);

      // add a text area with scroll bars
      
      textArea = new JTextArea(8, 40);
      scrollPane = new JScrollPane(textArea);

      add(scrollPane, BorderLayout.CENTER);
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 300;  

   private JTextArea textArea;
   private JScrollPane scrollPane;
   private JPanel buttonPanel;
   private JButton wrapButton;
}
