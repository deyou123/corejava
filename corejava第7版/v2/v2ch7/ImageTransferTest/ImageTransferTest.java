/**
   @version 1.21 2004-08-25
   @author Cay Horstmann
*/

import java.io.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

/**
   This program demonstrates the transfer of images between a Java application 
   and the system clipboard.
*/
public class ImageTransferTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new ImageTransferFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame has an image label and buttons for copying and pasting an image.
*/
class ImageTransferFrame extends JFrame
{  
   public ImageTransferFrame()
   {  
      setTitle("ImageTransferTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      label = new JLabel();
      image = makeMandelbrot(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      label.setIcon(new ImageIcon(image));
      add(new JScrollPane(label), BorderLayout.CENTER);
      JPanel panel = new JPanel();

      JButton copyButton = new JButton("Copy");
      panel.add(copyButton);
      copyButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event) { copy(); }
         });

      JButton pasteButton = new JButton("Paste");
      panel.add(pasteButton);
      pasteButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event) { paste(); }
         });

      add(panel, BorderLayout.SOUTH);
   }

   /**
      Copies the current image to the system clipboard.
   */
   private void copy()
   {  
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      ImageSelection selection = new ImageSelection(image);
      clipboard.setContents(selection, null);
   }

   /**
      Pastes the image from the system clipboard into the
      image label.
   */
   private void paste()
   {  
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      DataFlavor flavor = DataFlavor.imageFlavor;
      if (clipboard.isDataFlavorAvailable(flavor))
      {
         try
         {  
            image = (Image) clipboard.getData(flavor);
            label.setIcon(new ImageIcon(image));
         }
         catch (UnsupportedFlavorException exception)
         {  
            JOptionPane.showMessageDialog(this, exception);
         }
         catch (IOException exception)
         {  
            JOptionPane.showMessageDialog(this, exception);
         }
      }
   }

   /**
      Makes the Mandelbrot image.
      @param width the width
      @parah height the height
      @return the image
   */
   public BufferedImage makeMandelbrot(int width, int height)
   {  
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      WritableRaster raster = image.getRaster();
      ColorModel model = image.getColorModel();

      Color fractalColor = Color.red;
      int argb = fractalColor.getRGB();
      Object colorData = model.getDataElements(argb, null);

      for (int i = 0; i < width; i++)
         for (int j = 0; j < height; j++)
         {  
            double a = XMIN + i * (XMAX - XMIN) / width;
            double b = YMIN + j * (YMAX - YMIN) / height;
            if (!escapesToInfinity(a, b))
               raster.setDataElements(i, j, colorData);
         }
      return image;
   }

   private boolean escapesToInfinity(double a, double b)
   {  
      double x = 0.0;
      double y = 0.0;
      int iterations = 0;
      do
      {  
         double xnew = x * x - y * y + a;
         double ynew = 2 * x * y + b;
         x = xnew;
         y = ynew;
         iterations++;
         if (iterations == MAX_ITERATIONS) return false;
      }
      while (x <= 2 && y <= 2);
      return true;
   }

   private JLabel label;
   private Image image;

   private static final double XMIN = -2;
   private static final double XMAX = 2;
   private static final double YMIN = -2;
   private static final double YMAX = 2;
   private static final int MAX_ITERATIONS = 16;

   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 300;
}

/**
   This class is a wrapper for the data transfer of image 
   objects.
*/
class ImageSelection implements Transferable
{  
   /**
      Constructs the selection.
      @param image an image
   */
   public ImageSelection(Image image)
   {  
      theImage = image;
   }

   public DataFlavor[] getTransferDataFlavors()
   {  
      return new DataFlavor[] { DataFlavor.imageFlavor };
   }

   public boolean isDataFlavorSupported(DataFlavor flavor)
   {  
      return flavor.equals(DataFlavor.imageFlavor);
   }

   public Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException
   {  
      if(flavor.equals(DataFlavor.imageFlavor))
      {  
         return theImage;
      }
      else
      {  
         throw new UnsupportedFlavorException(flavor);
      }
   }

   private Image theImage;
}
