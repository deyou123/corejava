package comboBox;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A frame with a sample text label and a combo box for selecting font faces.
 */
public class ComboBoxFrame extends JFrame
{
   private JComboBox<String> faceCombo;
   private JLabel label;
   private static final int DEFAULT_SIZE = 24;

   public ComboBoxFrame()
   {
      // add the sample text label

      label = new JLabel("The quick brown fox jumps over the lazy dog.");
      label.setFont(new Font("Serif", Font.PLAIN, DEFAULT_SIZE));
      add(label, BorderLayout.CENTER);

      // make a combo box and add face names

      faceCombo = new JComboBox<>();
      faceCombo.addItem("Serif");
      faceCombo.addItem("SansSerif");
      faceCombo.addItem("Monospaced");
      faceCombo.addItem("Dialog");
      faceCombo.addItem("DialogInput");

      // the combo box listener changes the label font to the selected face name

      faceCombo.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               label.setFont(new Font(faceCombo.getItemAt(faceCombo.getSelectedIndex()), Font.PLAIN,
                     DEFAULT_SIZE));
            }
         });

      // add combo box to a panel at the frame's southern border

      JPanel comboPanel = new JPanel();
      comboPanel.add(faceCombo);
      add(comboPanel, BorderLayout.SOUTH);
      pack();
   }
}
