package imageTransfer;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;

/**
 * This frame has an image label and buttons for copying and pasting an image.
 */
class ImageTransferFrame extends JFrame
{
   private JLabel label;
   private Image image;
   private static final int IMAGE_WIDTH = 300;
   private static final int IMAGE_HEIGHT = 300;

   public ImageTransferFrame()
   {
      label = new JLabel();
      image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
      Graphics g = image.getGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
      g.setColor(Color.RED);
      g.fillOval(IMAGE_WIDTH / 4, IMAGE_WIDTH / 4, IMAGE_WIDTH / 2, IMAGE_HEIGHT / 2);

      label.setIcon(new ImageIcon(image));
      add(new JScrollPane(label), BorderLayout.CENTER);
      JPanel panel = new JPanel();

      JButton copyButton = new JButton("Copy");
      panel.add(copyButton);
      copyButton.addActionListener(event -> copy());

      JButton pasteButton = new JButton("Paste");
      panel.add(pasteButton);
      pasteButton.addActionListener(event -> paste());

      add(panel, BorderLayout.SOUTH);
      pack();
   }

   /**
    * Copies the current image to the system clipboard.
    */
   private void copy()
   {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      ImageTransferable selection = new ImageTransferable(image);
      clipboard.setContents(selection, null);
   }

   /**
    * Pastes the image from the system clipboard into the image label.
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
         catch (UnsupportedFlavorException | IOException ex)
         {
            JOptionPane.showMessageDialog(this, ex);
         }
      }
   }
}