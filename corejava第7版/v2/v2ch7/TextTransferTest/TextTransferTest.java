/**
   @version 1.12 2001-08-11
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
   This program demonstrates the transfer of text
   between a Java application and the system clipboard.
*/
public class TextTransferTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new TextTransferFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
      frame.setVisible(true);
   }
}

/**
   This frame has a text area and buttons for copying and 
   pasting text.
*/
class TextTransferFrame extends JFrame
{  
   public TextTransferFrame()
   {  
      setTitle("TextTransferTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      textArea = new JTextArea();
      add(new JScrollPane(textArea), BorderLayout.CENTER);
      JPanel panel = new JPanel();

      JButton copyButton = new JButton("Copy");
      panel.add(copyButton);
      copyButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               copy();
            }
         });

      JButton pasteButton = new JButton("Paste");
      panel.add(pasteButton);
      pasteButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               paste();
            }
         });

      add(panel, BorderLayout.SOUTH);
   }

   /**
      Copies the selected text to the system clipboard.
   */
   private void copy()
   {  
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      String text = textArea.getSelectedText();
      if (text == null) text = textArea.getText();
      StringSelection selection = new StringSelection(text);
      clipboard.setContents(selection, null);
   }

   /**
      Pastes the text from the system clipboard into the
      text area.
   */
   private void paste()
   {  
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      DataFlavor flavor = DataFlavor.stringFlavor;
      if (clipboard.isDataFlavorAvailable(flavor))
      {
         try
         {  
            String text = (String) clipboard.getData(flavor);
            textArea.replaceSelection(text);
         }
         catch (UnsupportedFlavorException e)
         {  
            JOptionPane.showMessageDialog(this, e);
         }
         catch (IOException e)
         {  
            JOptionPane.showMessageDialog(this, e);
         }
      }
   }

   private JTextArea textArea;

   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 300;
}
