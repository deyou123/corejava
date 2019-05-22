/**
   @version 1.01 2004-08-24
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.print.*;
import javax.swing.*;

/**
   This program demonstrates the use of print services. The program lets you print a GIF image 
   to any of the print services that support the GIF document flavor.
*/
public class PrintServiceTest
{
   public static void main(String[] args)
   {
      JFrame frame = new PrintServiceFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame displays the image to be printed. It contains
   menus for opening an image file, printing, and selecting
   a print service.
*/
class PrintServiceFrame extends JFrame
{
   public PrintServiceFrame()
   {
      setTitle("PrintServiceTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // set up menu bar
      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu menu = new JMenu("File");
      menuBar.add(menu);

      JMenuItem openItem = new JMenuItem("Open");
      menu.add(openItem);
      openItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               openFile();
            }
         });

      JMenuItem printItem = new JMenuItem("Print");
      menu.add(printItem);
      printItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               printFile();
            }
         });

      JMenuItem exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });

      menu = new JMenu("Printer");
      menuBar.add(menu);
      DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;
      addPrintServices(menu, flavor);

      // use a label to display the images
      label = new JLabel();
      add(label);
   }

   /**
      Adds print services to a menu
      @param menu the menu to which to add the services
      @param flavor the flavor that the services need to support
   */
   public void addPrintServices(JMenu menu, DocFlavor flavor)
   {      
      PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);
      ButtonGroup group = new ButtonGroup();
      for (int i = 0; i < services.length; i++)
      {
         final PrintService service = services[i];
         JRadioButtonMenuItem item = new JRadioButtonMenuItem(service.getName());
         menu.add(item);
         if (i == 0) 
         {
            item.setSelected(true);
            currentService = service;
         }
         group.add(item);
         item.addActionListener(new
            ActionListener()
            {
               public void actionPerformed(ActionEvent event)
               {
                  currentService = service;
               }
            });
      }
   }

   /**
      Open a GIF file and display the image.
   */
   public void openFile()
   {
      // set up file chooser
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));
      
      // accept all files ending with .gif
      chooser.setFileFilter(new
         javax.swing.filechooser.FileFilter()
         {
            public boolean accept(File f)
            {
               return f.getName().toLowerCase().endsWith(".gif") || f.isDirectory();
            }

            public String getDescription() { return "GIF Images"; }
         });

      // show file chooser dialog
      int r = chooser.showOpenDialog(PrintServiceFrame.this);
      
      // if image file accepted, set it as icon of the label
      if(r == JFileChooser.APPROVE_OPTION)
      {
         fileName = chooser.getSelectedFile().getPath();
         label.setIcon(new ImageIcon(fileName));
      }
   }

   /**
      Print the current file using the current print service.
   */
   public void printFile()
   {
      try
      {
         if (fileName == null) return;
         if (currentService == null) return;
         FileInputStream in = new FileInputStream(fileName);
         DocFlavor flavor = DocFlavor.INPUT_STREAM.GIF;      
         Doc doc = new SimpleDoc(in, flavor, null);
         DocPrintJob job = currentService.createPrintJob();
         job.print(doc, null);      
      }
      catch (FileNotFoundException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (PrintException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }
   }

   private JLabel label;
   private String fileName;
   private PrintService currentService;
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 400;
}

