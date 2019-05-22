import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @version 1.33 2007-06-12
 * @author Cay Horstmann
 */
public class ComboBoxTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {

               ComboBoxFrame frame = new ComboBoxFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame with a sample text label and a combo box for selecting font faces.
 */
class ComboBoxFrame extends JFrame
{
   public ComboBoxFrame()
   {
      setTitle("ComboBoxTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add the sample text label

      label = new JLabel("The quick brown fox jumps over the lazy dog.");
      label.setFont(new Font("Serif", Font.PLAIN, DEFAULT_SIZE));
      add(label, BorderLayout.CENTER);

      // make a combo box and add face names

      faceCombo = new JComboBox();
      faceCombo.setEditable(true);
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
               label.setFont(new Font((String) faceCombo.getSelectedItem(), Font.PLAIN,
                     DEFAULT_SIZE));
            }
         });

      // add combo box to a panel at the frame's southern border

      JPanel comboPanel = new JPanel();
      comboPanel.add(faceCombo);
      add(comboPanel, BorderLayout.SOUTH);
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;

   private JComboBox faceCombo;
   private JLabel label;
   private static final int DEFAULT_SIZE = 12;
}
