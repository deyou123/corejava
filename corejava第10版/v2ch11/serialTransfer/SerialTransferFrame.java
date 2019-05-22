package serialTransfer;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * This frame contains a color chooser, and copy and paste buttons.
 */
class SerialTransferFrame extends JFrame
{
   private JColorChooser chooser;

   public SerialTransferFrame()
   {
      chooser = new JColorChooser();
      add(chooser, BorderLayout.CENTER);
      JPanel panel = new JPanel();

      JButton copyButton = new JButton("Copy");
      panel.add(copyButton);
      copyButton.addActionListener(event -> copy());

      JButton pasteButton = new JButton("Paste");
      panel.add(pasteButton);
      pasteButton.addActionListener(event ->  paste());

      add(panel, BorderLayout.SOUTH);
      pack();
   }

   /**
    * Copies the chooser's color into the system clipboard.
    */
   private void copy()
   {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      Color color = chooser.getColor();
      SerialTransferable selection = new SerialTransferable(color);
      clipboard.setContents(selection, null);
   }

   /**
    * Pastes the color from the system clipboard into the chooser.
    */
   private void paste()
   {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      try
      {
         DataFlavor flavor = new DataFlavor(
               "application/x-java-serialized-object;class=java.awt.Color");
         if (clipboard.isDataFlavorAvailable(flavor))
         {
            Color color = (Color) clipboard.getData(flavor);
            chooser.setColor(color);
         }
      }
      catch (ClassNotFoundException | UnsupportedFlavorException | IOException ex)
      {
         JOptionPane.showMessageDialog(this, ex);
      }
   }
}

/**
 * This class is a wrapper for the data transfer of serialized objects.
 */
class SerialTransferable implements Transferable
{
   private Serializable obj;

   /**
    * Constructs the selection.
    * @param o any serializable object
    */
   SerialTransferable(Serializable o)
   {
      obj = o;
   }

   public DataFlavor[] getTransferDataFlavors()
   {
      DataFlavor[] flavors = new DataFlavor[2];
      Class<?> type = obj.getClass();
      String mimeType = "application/x-java-serialized-object;class=" + type.getName();
      try
      {
         flavors[0] = new DataFlavor(mimeType);
         flavors[1] = DataFlavor.stringFlavor;
         return flavors;
      }
      catch (ClassNotFoundException e)
      {
         return new DataFlavor[0];
      }
   }

   public boolean isDataFlavorSupported(DataFlavor flavor)
   {
      return DataFlavor.stringFlavor.equals(flavor)
            || "application".equals(flavor.getPrimaryType())
            && "x-java-serialized-object".equals(flavor.getSubType())
            && flavor.getRepresentationClass().isAssignableFrom(obj.getClass());
   }

   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
   {
      if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);

      if (DataFlavor.stringFlavor.equals(flavor)) return obj.toString();

      return obj;
   }
}
