package read;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * This program shows how to use an XML file to describe a gridbag layout
 * @version 1.12 2016-04-27
 * @author Cay Horstmann
 */
public class GridBagTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFileChooser chooser = new JFileChooser(".");
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            JFrame frame = new FontFrame(file);
            frame.setTitle("GridBagTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
      });
   }
}

/**
 * This frame contains a font selection dialog that is described by an XML file.
 * @param filename the file containing the user interface components for the dialog.
 */
class FontFrame extends JFrame
{
   private GridBagPane gridbag;
   private JComboBox<String> face;
   private JComboBox<String> size;
   private JCheckBox bold;
   private JCheckBox italic;

   @SuppressWarnings("unchecked")
   public FontFrame(File file)
   {
      gridbag = new GridBagPane(file);
      add(gridbag);

      face = (JComboBox<String>) gridbag.get("face");
      size = (JComboBox<String>) gridbag.get("size");
      bold = (JCheckBox) gridbag.get("bold");
      italic = (JCheckBox) gridbag.get("italic");

      face.setModel(new DefaultComboBoxModel<String>(new String[] { "Serif", 
            "SansSerif", "Monospaced", "Dialog", "DialogInput" }));

      size.setModel(new DefaultComboBoxModel<String>(new String[] { "8", 
            "10", "12", "15", "18", "24", "36", "48" }));

      ActionListener listener = event -> setSample();

      face.addActionListener(listener);
      size.addActionListener(listener);
      bold.addActionListener(listener);
      italic.addActionListener(listener);

      setSample();
      pack();
   }

   /**
    * This method sets the text sample to the selected font.
    */
   public void setSample()
   {
      String fontFace = face.getItemAt(face.getSelectedIndex());
      int fontSize = Integer.parseInt(size.getItemAt(size.getSelectedIndex()));
      JTextArea sample = (JTextArea) gridbag.get("sample");
      int fontStyle = (bold.isSelected() ? Font.BOLD : 0)
            + (italic.isSelected() ? Font.ITALIC : 0);

      sample.setFont(new Font(fontFace, fontStyle, fontSize));
      sample.repaint();
   }
}
