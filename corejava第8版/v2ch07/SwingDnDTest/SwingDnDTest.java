import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * This program demonstrates the basic Swing support for drag and drop.
 * @version 1.10 2007-09-20
 * @author Cay Horstmann
 */
public class SwingDnDTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new SwingDnDFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

class SwingDnDFrame extends JFrame
{
   public SwingDnDFrame()
   {
      setTitle("SwingDnDTest");
      JTabbedPane tabbedPane = new JTabbedPane();

      JList list = SampleComponents.list();
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
      
      tabbedPane.addChangeListener(new ChangeListener()
         {
            public void stateChanged(ChangeEvent e)
            {
               textArea.setText("");             
            }
         });

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
