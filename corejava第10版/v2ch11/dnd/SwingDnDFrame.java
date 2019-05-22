package dnd;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class SwingDnDFrame extends JFrame
{
   public SwingDnDFrame()
   {
      JTabbedPane tabbedPane = new JTabbedPane();

      JList<String> list = SampleComponents.list();
      tabbedPane.addTab("List", list);
      JTable table = SampleComponents.table();
      tabbedPane.addTab("Table", table);
      JTree tree = SampleComponents.tree();
      tabbedPane.addTab("Tree", tree);
      JFileChooser fileChooser = new JFileChooser();
      tabbedPane.addTab("File Chooser", fileChooser);
      JColorChooser colorChooser = new JColorChooser();
      tabbedPane.addTab("Color Chooser", colorChooser);

      final JTextArea textArea = new JTextArea(4, 40);
      JScrollPane scrollPane = new JScrollPane(textArea);
      scrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Drag text here"));

      JTextField textField = new JTextField("Drag color here");
      textField.setTransferHandler(new TransferHandler("background"));
      
      tabbedPane.addChangeListener(event -> textArea.setText(""));             

      tree.setDragEnabled(true);
      table.setDragEnabled(true);
      list.setDragEnabled(true);
      fileChooser.setDragEnabled(true);
      colorChooser.setDragEnabled(true);
      textField.setDragEnabled(true);

      add(tabbedPane, BorderLayout.NORTH);
      add(scrollPane, BorderLayout.CENTER);
      add(textField, BorderLayout.SOUTH);
      pack();
   }
}
