package filePicker;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * A bean for picking file names.
 * @version 1.31 2012-06-10
 * @author Cay Horstmann
 */
public class FilePickerBean extends JPanel
{
   private static final int XPREFSIZE = 200;
   private static final int YPREFSIZE = 20;

   private JButton dialogButton;
   private JTextField nameField;
   private JFileChooser chooser;
   private String[] extensions = { "gif", "png" };

   public FilePickerBean()
   {
      dialogButton = new JButton("...");
      nameField = new JTextField(30);

      chooser = new JFileChooser();
      setPreferredSize(new Dimension(XPREFSIZE, YPREFSIZE));

      setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.weightx = 100;
      gbc.weighty = 100;
      gbc.anchor = GridBagConstraints.WEST;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.gridwidth = 1;
      gbc.gridheight = 1;
      add(nameField, gbc);

      dialogButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               chooser.setFileFilter(new FileNameExtensionFilter(Arrays.toString(extensions),
                     extensions));
               int r = chooser.showOpenDialog(null);
               if (r == JFileChooser.APPROVE_OPTION)
               {
                  File f = chooser.getSelectedFile();
                  String name = f.getAbsolutePath();
                  setFileName(name);
               }
            }
         });
      nameField.setEditable(false);

      gbc.weightx = 0;
      gbc.anchor = GridBagConstraints.EAST;
      gbc.fill = GridBagConstraints.NONE;
      gbc.gridx = 1;
      add(dialogButton, gbc);
   }

   /**
    * Sets the fileName property.
    * @param newValue the new file name
    */
   public void setFileName(String newValue)
   {
      String oldValue = nameField.getText();
      nameField.setText(newValue);
      firePropertyChange("fileName", oldValue, newValue);
   }

   /**
    * Gets the fileName property.
    * @return the name of the selected file
    */
   public String getFileName()
   {
      return nameField.getText();
   }

   /**
    * Gets the extensions property.
    * @return the default extensions in the file chooser
    */
   public String[] getExtensions()
   {
      return extensions;
   }

   /**
    * Sets the extensions property.
    * @param newValue the new default extensions
    */
   public void setExtensions(String[] newValue)
   {
      extensions = newValue;
   }

   /**
    * Gets one of the extensions property values.
    * @param i the index of the property value
    * @return the value at the given index
    */
   public String getExtensions(int i)
   {
      if (0 <= i && i < extensions.length) return extensions[i];
      else return "";
   }

   /**
    * Sets one of the extensions property values.
    * @param i the index of the property value
    * @param newValue the new value at the given index
    */
   public void setExtensions(int i, String newValue)
   {
      if (0 <= i && i < extensions.length) extensions[i] = newValue;
   }
}
