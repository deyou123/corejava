/**
   @version 1.01 2004-05-06
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SpringLayoutTest
{
   public static void main(String[] args)
   {  
      FontDialogFrame frame = new FontDialogFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame that uses a spring layout to arrange font
   selection components.
*/
class FontDialogFrame extends JFrame
{  
   public FontDialogFrame()
   {  
      setTitle("FontDialog");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JPanel panel = new JPanel();
      SpringLayout layout = new SpringLayout();
      panel.setLayout(layout);

      ActionListener listener = new FontAction();

      // construct components
      
      JLabel faceLabel = new JLabel("Font Face: ");

      face = new JComboBox(new String[] 
         {  
            "Serif", "SansSerif", "Monospaced", 
            "Dialog", "DialogInput" 
         });
      
      face.addActionListener(listener);

      JLabel sizeLabel = new JLabel("Size: ");

      size = new JComboBox(new String[]
         {
            "8", "10", "12", "15", "18", "24", "36", "48"
         });

      size.addActionListener(listener);

      bold = new JCheckBox("Bold");
      bold.addActionListener(listener);

      italic = new JCheckBox("Italic");
      italic.addActionListener(listener);

      sample = new JTextArea();
      sample.setText("The quick brown fox jumps over the lazy dog");
      sample.setEditable(false);
      sample.setLineWrap(true);
      sample.setBorder(BorderFactory.createEtchedBorder());

      panel.add(faceLabel);
      panel.add(sizeLabel);
      panel.add(face);
      panel.add(size);
      panel.add(bold);
      panel.add(italic);
      panel.add(sample);

      // add springs to lay out components
      Spring strut = Spring.constant(10);

      Spring labelsEast = Spring.sum(strut,
         Spring.max(
            layout.getConstraints(faceLabel).getWidth(),
            layout.getConstraints(sizeLabel).getWidth()));

      layout.putConstraint(SpringLayout.EAST, faceLabel, labelsEast, SpringLayout.WEST, panel);
      layout.putConstraint(SpringLayout.EAST, sizeLabel, labelsEast, SpringLayout.WEST, panel);

      layout.putConstraint(SpringLayout.NORTH, faceLabel, strut, SpringLayout.NORTH, panel);
      layout.putConstraint(SpringLayout.NORTH, face, strut, SpringLayout.NORTH, panel);

      Spring secondRowNorth = Spring.sum(strut,
         Spring.max(
            layout.getConstraint(SpringLayout.SOUTH, faceLabel),
            layout.getConstraint(SpringLayout.SOUTH, face)));

      layout.putConstraint(SpringLayout.NORTH, sizeLabel, secondRowNorth, SpringLayout.NORTH, 
         panel);
      layout.putConstraint(SpringLayout.NORTH, size, secondRowNorth, SpringLayout.NORTH, panel);

      layout.putConstraint(SpringLayout.WEST, face, strut, SpringLayout.EAST, faceLabel);
      layout.putConstraint(SpringLayout.WEST, size, strut, SpringLayout.EAST, sizeLabel);

      layout.putConstraint(SpringLayout.WEST, bold, strut, SpringLayout.WEST, panel);
      layout.putConstraint(SpringLayout.WEST, italic, strut, SpringLayout.WEST, panel);

      Spring s = Spring.constant(10, 10000, 10000);

      Spring thirdRowNorth = Spring.sum(s,
         Spring.max(
            layout.getConstraint(SpringLayout.SOUTH, sizeLabel),
            layout.getConstraint(SpringLayout.SOUTH, size)));

      layout.putConstraint(SpringLayout.NORTH, bold, thirdRowNorth, SpringLayout.NORTH, panel);
      layout.putConstraint(SpringLayout.NORTH, italic, s, SpringLayout.SOUTH, bold);    
      layout.putConstraint(SpringLayout.SOUTH, panel, s, SpringLayout.SOUTH, italic);

      Spring secondColumnWest = Spring.sum(strut,
         Spring.max(
            layout.getConstraint(SpringLayout.EAST, face),
            layout.getConstraint(SpringLayout.EAST, size)));

      layout.putConstraint(SpringLayout.WEST, sample, secondColumnWest, SpringLayout.WEST, panel);
      layout.putConstraint(SpringLayout.SOUTH, sample, Spring.minus(strut), SpringLayout.SOUTH, 
         panel);
      layout.putConstraint(SpringLayout.NORTH, sample, strut, SpringLayout.NORTH, panel);
      layout.putConstraint(SpringLayout.EAST, panel, strut, SpringLayout.EAST, sample);

      add(panel);
   }
  
   public static final int DEFAULT_WIDTH = 400;
   public static final int DEFAULT_HEIGHT = 200;  

   private JComboBox face;
   private JComboBox size;
   private JCheckBox bold;
   private JCheckBox italic;
   private JTextArea sample;

   /**
      An action listener that changes the font of the 
      sample text.
   */
   private class FontAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {  
         String fontFace = (String) face.getSelectedItem();
         int fontStyle = (bold.isSelected() ? Font.BOLD : 0)
            + (italic.isSelected() ? Font.ITALIC : 0);
         int fontSize = Integer.parseInt((String) size.getSelectedItem());
         Font font = new Font(fontFace, fontStyle, fontSize);
         sample.setFont(font);
         sample.repaint();
      }
   }
}
