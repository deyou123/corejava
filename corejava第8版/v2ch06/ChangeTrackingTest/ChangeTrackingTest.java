import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * @version 1.40 2007-08-05
 * @author Cay Horstmann
 */
public class ChangeTrackingTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               ColorFrame frame = new ColorFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame with three text fields to set the background color.
 */
class ColorFrame extends JFrame
{
   public ColorFrame()
   {
      setTitle("ChangeTrackingTest");

      DocumentListener listener = new DocumentListener()
         {
            public void insertUpdate(DocumentEvent event)
            {
               setColor();
            }

            public void removeUpdate(DocumentEvent event)
            {
               setColor();
            }

            public void changedUpdate(DocumentEvent event)
            {
            }
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

   private JPanel panel;
   private JTextField redField;
   private JTextField greenField;
   private JTextField blueField;
}

