package persistentFrame;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import javax.swing.*;

/**
 * This program demonstrates the use of an XML encoder and decoder to save and restore a frame.
 * @version 1.01 2007-10-03
 * @author Cay Horstmann
 */
public class PersistentFrameTest
{
   private static JFileChooser chooser;
   private JFrame frame;

   public static void main(String[] args)
   {
      chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));      
      PersistentFrameTest test = new PersistentFrameTest();
      test.init();
   }

   public void init()
   {
      frame = new JFrame();
      frame.setLayout(new FlowLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("PersistentFrameTest");
      frame.setSize(400, 200);

      JButton loadButton = new JButton("Load");
      frame.add(loadButton);
      loadButton.addActionListener(EventHandler.create(ActionListener.class, this, "load"));

      JButton saveButton = new JButton("Save");
      frame.add(saveButton);
      saveButton.addActionListener(EventHandler.create(ActionListener.class, this, "save"));
     
      frame.setVisible(true);
   }

   public void load()
   {
      // show file chooser dialog
      int r = chooser.showOpenDialog(null);

      // if file selected, open
      if(r == JFileChooser.APPROVE_OPTION)
      {
         try
         {
            File file = chooser.getSelectedFile();
            XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
            decoder.readObject(); 
            decoder.close();
         }
         catch (IOException e)
         {
            JOptionPane.showMessageDialog(null, e);
         }
      }
   }

   public void save()
   {
      if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
      {
         try
         {
            File file = chooser.getSelectedFile();
            XMLEncoder encoder = new XMLEncoder(new FileOutputStream(file));
            encoder.writeObject(frame);
            encoder.close();
         }
         catch (IOException e)
         {
            JOptionPane.showMessageDialog(null, e);
         }
      }
   }
}
