package editorPane;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * This frame contains an editor pane, a text field and button to enter a URL and load a document,
 * and a Back button to return to a previously loaded document.
 */
public class EditorPaneFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 400;

   public EditorPaneFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      final Stack<String> urlStack = new Stack<>();
      final JEditorPane editorPane = new JEditorPane();
      final JTextField url = new JTextField(30);

      // set up hyperlink listener

      editorPane.setEditable(false);
      editorPane.addHyperlinkListener(new HyperlinkListener()
         {
            public void hyperlinkUpdate(HyperlinkEvent event)
            {
               if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
               {
                  try
                  {
                     // remember URL for back button
                     urlStack.push(event.getURL().toString());
                     // show URL in text field
                     url.setText(event.getURL().toString());
                     editorPane.setPage(event.getURL());
                  }
                  catch (IOException e)
                  {
                     editorPane.setText("Exception: " + e);
                  }
               }
            }
         });

      // set up checkbox for toggling edit mode

      final JCheckBox editable = new JCheckBox();
      editable.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               editorPane.setEditable(editable.isSelected());
            }
         });

      // set up load button for loading URL

      ActionListener listener = new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               try
               {
                  // remember URL for back button
                  urlStack.push(url.getText());
                  editorPane.setPage(url.getText());
               }
               catch (IOException e)
               {
                  editorPane.setText("Exception: " + e);
               }
            }
         };

      JButton loadButton = new JButton("Load");
      loadButton.addActionListener(listener);
      url.addActionListener(listener);

      // set up back button and button action

      JButton backButton = new JButton("Back");
      backButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               if (urlStack.size() <= 1) return;
               try
               {
                  // get URL from back button
                  urlStack.pop();
                  // show URL in text field
                  String urlString = urlStack.peek();
                  url.setText(urlString);
                  editorPane.setPage(urlString);
               }
               catch (IOException e)
               {
                  editorPane.setText("Exception: " + e);
               }
            }
         });

      add(new JScrollPane(editorPane), BorderLayout.CENTER);

      // put all control components in a panel

      JPanel panel = new JPanel();
      panel.add(new JLabel("URL"));
      panel.add(url);
      panel.add(loadButton);
      panel.add(backButton);
      panel.add(new JLabel("Editable"));
      panel.add(editable);

      add(panel, BorderLayout.SOUTH);
   }
}
