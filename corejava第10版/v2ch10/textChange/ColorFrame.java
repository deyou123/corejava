package textChange;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A frame with three text fields to set the background color.
 */
public class ColorFrame extends JFrame
{
   private JPanel panel;
   private JTextField redField;
   private JTextField greenField;
   private JTextField blueField;

   public ColorFrame()
   {
      DocumentListener listener = new DocumentListener()
         {
            public void insertUpdate(DocumentEvent event) { setColor(); }
            public void removeUpdate(DocumentEvent event) { setColor(); }
            public void changedUpdate(DocumentEvent event) {}
         };

      panel = new JPanel();
      
      panel.add(new JLabel("Red:"));
      redField = new JTextField("255", 3);
      panel.add(redField);
      redField.getDocument().addDocumentListener(listener);

      panel.add(new JLabel("Green:"));
      greenField = new JTextField("255", 3);
      panel.add(greenField);
      greenField.getDocument().addDocumentListener(listener);

      panel.add(new JLabel("Blue:"));
      blueField = new JTextField("255", 3);
      panel.add(blueField);
      blueField.getDocument().addDocumentListener(listener);
      
      add(panel);
      pack();
   }

   /**
    * Set the background color to the values stored in the text fields.
    */
   public void setColor()
   {
      try
      {
         int red = Integer.parseInt(redField.getText().trim());
         int green = Integer.parseInt(greenField.getText().trim());
         int blue = Integer.parseInt(blueField.getText().trim());
         panel.setBackground(new Color(red, green, blue));
      }
      catch (NumberFormatException e)
      {
         // don't set the color if the input can't be parsed
      }
   }
}