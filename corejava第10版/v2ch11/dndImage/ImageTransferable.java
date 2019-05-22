package dndImage;

import java.awt.*;
import java.awt.datatransfer.*;

/**
 * This class is a wrapper for the data transfer of image objects.
 */
public class ImageTransferable implements Transferable
{
   private Image theImage;

   /**
    * Constructs the selection.
    * @param image an image
    */
   public ImageTransferable(Image image)
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

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
   {
      if (flavor.equals(DataFlavor.imageFlavor))
      {
         return theImage;
      }
      else
      {
         throw new UnsupportedFlavorException(flavor);
      }
   }
}
