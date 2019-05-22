package dndImage;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import javax.imageio.*;
import javax.swing.*;

public class ImageListDnDFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 500;

   private ImageList list1;
   private ImageList list2;

   public ImageListDnDFrame()
   {     
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      list1 = new ImageList(Paths.get(getClass().getPackage().getName(), "images1"));
      list2 = new ImageList(Paths.get(getClass().getPackage().getName(), "images2"));
      
      setLayout(new GridLayout(2, 1));
      add(new JScrollPane(list1));
      add(new JScrollPane(list2));
   }
}

class ImageList extends JList<ImageIcon>
{
   public ImageList(Path dir)
   {
      DefaultListModel<ImageIcon> model = new DefaultListModel<>();
      try (DirectoryStream<Path> entries = Files.newDirectoryStream(dir))
      {
         for (Path entry : entries) 
            model.addElement(new ImageIcon(entry.toString()));       
      }         
      catch (IOException ex)
      {
         ex.printStackTrace();
      }

      setModel(model);
      setVisibleRowCount(0);
      setLayoutOrientation(JList.HORIZONTAL_WRAP);
      setDragEnabled(true);
      setDropMode(DropMode.ON_OR_INSERT);
      setTransferHandler(new ImageListTransferHandler());
   }
}

class ImageListTransferHandler extends TransferHandler
{
   // support for drag

   public int getSourceActions(JComponent source)
   {
      return COPY_OR_MOVE;
   }

   protected Transferable createTransferable(JComponent source)
   {
      ImageList list = (ImageList) source;
      int index = list.getSelectedIndex();
      if (index < 0) return null;
      ImageIcon icon = list.getModel().getElementAt(index);
      return new ImageTransferable(icon.getImage());
   }

   protected void exportDone(JComponent source, Transferable data, int action)
   {
      if (action == MOVE)
      {
         ImageList list = (ImageList) source;
         int index = list.getSelectedIndex();
         if (index < 0) return;
         DefaultListModel<?> model = (DefaultListModel<?>) list.getModel();
         model.remove(index);
      }
   }

   // support for drop

   public boolean canImport(TransferSupport support)
   {
      if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
      {
         if (support.getUserDropAction() == MOVE) support.setDropAction(COPY);
         return true;
      }
      else return support.isDataFlavorSupported(DataFlavor.imageFlavor);
   }

   public boolean importData(TransferSupport support)
   {
      ImageList list = (ImageList) support.getComponent();
      DefaultListModel<ImageIcon> model = (DefaultListModel<ImageIcon>) list.getModel();

      Transferable transferable = support.getTransferable();
      List<DataFlavor> flavors = Arrays.asList(transferable.getTransferDataFlavors());

      List<Image> images = new ArrayList<>();

      try
      {
         if (flavors.contains(DataFlavor.javaFileListFlavor))
         {
            @SuppressWarnings("unchecked") List<File> fileList 
               = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            for (File f : fileList)
            {
               try
               {
                  images.add(ImageIO.read(f));
               }
               catch (IOException ex)
               {
                  // couldn't read image--skip
               }
            }
         }
         else if (flavors.contains(DataFlavor.imageFlavor))
         {
            images.add((Image) transferable.getTransferData(DataFlavor.imageFlavor));
         }

         int index;
         if (support.isDrop())
         {
            JList.DropLocation location = (JList.DropLocation) support.getDropLocation();
            index = location.getIndex();
            if (!location.isInsert()) model.remove(index); // replace location
         }
         else index = model.size();
         for (Image image : images)
         {
            model.add(index, new ImageIcon(image));
            index++;
         }
         return true;
      }
      catch (IOException | UnsupportedFlavorException ex)
      {
         return false;
      }
   }
}
