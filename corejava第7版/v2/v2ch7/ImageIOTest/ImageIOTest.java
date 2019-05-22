/**
   @version 1.01 2004-08-24
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.swing.*;

/**
   This program lets you read and write image files in the 
   formats that the JDK supports. Multi-file images are 
   supported.
*/
public class ImageIOTest
{ 
   public static void main(String[] args)
   {  
      JFrame frame = new ImageIOFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame displays the loaded images. The menu has items
   for loading and saving files.
*/
class ImageIOFrame extends JFrame
{  
   public ImageIOFrame()
   {  
      setTitle("ImageIOTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JMenu fileMenu = new JMenu("File");
      JMenuItem openItem = new JMenuItem("Open");
      openItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               openFile();
            }
         });
      fileMenu.add(openItem);

      JMenu saveMenu = new JMenu("Save");
      fileMenu.add(saveMenu);      
      Iterator<String> iter = writerFormats.iterator();
      while (iter.hasNext())
      {
         final String formatName = iter.next();
         JMenuItem formatItem = new JMenuItem(formatName);
         saveMenu.add(formatItem);
         formatItem.addActionListener(new
            ActionListener()
            {
               public void actionPerformed(ActionEvent event)
               {
                  saveFile(formatName);
               }
            });         
      }

      JMenuItem exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });
      fileMenu.add(exitItem);


      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);
      setJMenuBar(menuBar);
   }

   /**
      Open a file and load the images.
   */
   public void openFile()
   {  
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      chooser.setFileFilter(new
         javax.swing.filechooser.FileFilter()
         {  
            public boolean accept(File f)
            {  
               if (f.isDirectory()) return true;
               String name = f.getName();
               int p = name.lastIndexOf('.');
               if (p == -1) return false;
               String suffix = name.substring(p + 1).toLowerCase();
               return readerSuffixes.contains(suffix);
            }
            public String getDescription()
            {  
               return "Image files";
            }
         });
      int r = chooser.showOpenDialog(this);
      if (r != JFileChooser.APPROVE_OPTION) return;
      File f = chooser.getSelectedFile();
      Box box = Box.createVerticalBox();
      try
      {
         String name = f.getName();
         String suffix = name.substring(name.lastIndexOf('.') + 1);
         Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
         ImageReader reader = iter.next();
         ImageInputStream imageIn = ImageIO.createImageInputStream(f);
         reader.setInput(imageIn);
         int count = reader.getNumImages(true);
         images = new BufferedImage[count];
         for (int i = 0; i < count; i++)
         {
            images[i] = reader.read(i);
            box.add(new JLabel(new ImageIcon(images[i])));
         }
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      setContentPane(new JScrollPane(box));
      validate();
   }

   /**
      Save the current image in a file
      @param formatName the file format
   */
   public void saveFile(final String formatName)
   {
      if (images == null) return;
      Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(formatName);
      ImageWriter writer = iter.next();
      final List<String> writerSuffixes 
         = Arrays.asList(writer.getOriginatingProvider().getFileSuffixes());
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      chooser.setFileFilter(new
         javax.swing.filechooser.FileFilter()
         {  
            public boolean accept(File f)
            {  
               if (f.isDirectory()) return true;
               String name = f.getName();
               int p = name.lastIndexOf('.');
               if (p == -1) return false;
               String suffix = name.substring(p + 1).toLowerCase();
               return writerSuffixes.contains(suffix);
            }
            public String getDescription()
            {  
               return formatName + " files";
            }
         });

      int r = chooser.showSaveDialog(this);
      if (r != JFileChooser.APPROVE_OPTION) return;
      File f = chooser.getSelectedFile();
      try
      {
         ImageOutputStream imageOut = ImageIO.createImageOutputStream(f);
         writer.setOutput(imageOut);
                   
         writer.write(new IIOImage(images[0], null, null));      
         for (int i = 1; i < images.length; i++)
         {
            IIOImage iioImage = new IIOImage(images[i], null, null);
            if (writer.canInsertImage(i))
               writer.writeInsert(i, iioImage, null);            
         }
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   /**
      Gets a set of all file suffixes that are recognized by image readers.
      @return the file suffix set
   */
   public static Set<String> getReaderSuffixes()
   {
      TreeSet<String> readerSuffixes = new TreeSet<String>();
      for (String name : ImageIO.getReaderFormatNames())
      {
         Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName(name);
         while (iter.hasNext())
         {
            ImageReader reader = iter.next();
            String[] s = reader.getOriginatingProvider().getFileSuffixes();
            readerSuffixes.addAll(Arrays.asList(s));
         }
      }  
      return readerSuffixes;
   }

   /**
      Gets a set of "preferred" format names of all image writers. The preferred format name is 
      the first format name that a writer specifies.
      @return the format name set
   */
   public static Set<String> getWriterFormats()
   {
      TreeSet<String> writerFormats = new TreeSet<String>();
      TreeSet<String> formatNames 
         = new TreeSet<String>(Arrays.asList(ImageIO.getWriterFormatNames()));
      while (formatNames.size() > 0)
      {
         String name = formatNames.iterator().next();
         Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(name);
         ImageWriter writer = iter.next();
         String[] names = writer.getOriginatingProvider().getFormatNames();
         writerFormats.add(names[0]);
         formatNames.removeAll(Arrays.asList(names));
      }
      return writerFormats;
   }

   private BufferedImage[] images;
   private static Set<String> readerSuffixes = getReaderSuffixes();
   private static Set<String> writerFormats = getWriterFormats();
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 400;
}
