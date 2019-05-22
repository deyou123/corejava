import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.List;

/**
 * This program demonstrates drag and drop in an image list.
 * @version 1.00 2007-09-20
 * @author Cay Horstmann
 */
public class ImageListDnDTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ImageListDnDFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

class ImageListDnDFrame extends JFrame
{
   public ImageListDnDFrame()
   {
      setTitle("ImageListDnDTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      list1 = new ImageList(new File("images1").listFiles());
      list2 = new ImageList(new File("images2").listFiles());
      setLayout(new GridLayout(2, 1));
      add(new JScrollPane(list1));
      add(new JScrollPane(list2));
   }

   private ImageList list1;
   private ImageList list2;
   private static final int DEFAULT_WIDTH = 600;
   private static final int DEFAULT_HEIGHT = 500;
}

class ImageList extends JList
{
   public ImageList(File[] imageFiles)
   {
      DefaultListModel model = new DefaultListModel();
      for (File f : imageFiles)
         model.addElement(new ImageIcon(f.getPath()));

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
   // Support for drag

   public int getSourceActions(JComponent source)
   {
      return COPY_OR_MOVE;
   }

   protected Transferable createTransferable(JComponent source)
   {
      JList list = (JList) source;
      int index = list.getSelectedIndex();
      if (index < 0) return null;
      ImageIcon icon = (ImageIcon) list.getModel().getElementAt(index);
      return new ImageTransferable(icon.getImage());
   }

   protected void exportDone(JComponent source, Transferable data, int action)
   {
      if (action == MOVE)
      {
         JList list = (JList) source;
         int index = list.getSelectedIndex();
         if (index < 0) return;
         DefaultListModel model = (DefaultListModel) list.getModel();
         model.remove(index);
      }
   }

   // Support for drop

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
      JList list = (JList) support.getComponent();
      DefaultListModel model = (DefaultListModel) list.getModel();

      Transferable transferable = support.getTransferable();
      List<DataFlavor> flavors = Arrays.asList(transferable.getTransferDataFlavors());

      List<Image> images = new ArrayList<Image>();

      try
      {
         if (flavors.contains(DataFlavor.javaFileListFlavor))
         {
            List<File> fileList = (List<File>) transferable
                  .getTransferData(DataFlavor.javaFileListFlavor);
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
      catch (IOException ex)
      {
         return false;
      }
      catch (UnsupportedFlavorException ex)
      {
         return false;
      }
   }
}
