import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

/**
 * @version 1.23 2007-06-12
 * @author Cay Horstmann
 */
public class FileChooserTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               ImageViewerFrame frame = new ImageViewerFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame that has a menu for loading an image and a display area for the loaded image.
 */
class ImageViewerFrame extends JFrame
{
   public ImageViewerFrame()
   {
      setTitle("FileChooserTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // set up menu bar
      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu menu = new JMenu("File");
      menuBar.add(menu);

      JMenuItem openItem = new JMenuItem("Open");
      menu.add(openItem);
      openItem.addActionListener(new FileOpenListener());

      JMenuItem exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });

      // use a label to display the images
      label = new JLabel();
      add(label);

      // set up file chooser
      chooser = new JFileChooser();

      // accept all image files ending with .jpg, .jpeg, .gif
      /*
      final ExtensionFileFilter filter = new ExtensionFileFilter();
      filter.addExtension("jpg");
      filter.addExtension("jpeg");
      filter.addExtension("gif");
      filter.setDescription("Image files");
      */
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "gif");
      chooser.setFileFilter(filter);

      chooser.setAccessory(new ImagePreviewer(chooser));

      chooser.setFileView(new FileIconView(filter, new ImageIcon("palette.gif")));
   }

   /**
    * This is the listener for the File->Open menu item.
    */
   private class FileOpenListener implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         chooser.setCurrentDirectory(new File("."));

         // show file chooser dialog
         int result = chooser.showOpenDialog(ImageViewerFrame.this);

         // if image file accepted, set it as icon of the label
         if (result == JFileChooser.APPROVE_OPTION)
         {
            String name = chooser.getSelectedFile().getPath();
            label.setIcon(new ImageIcon(name));
         }
      }
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 400;

   private JLabel label;
   private JFileChooser chooser;
}

/**
 * A file view that displays an icon for all files that match a file filter.
 */
class FileIconView extends FileView
{
   /**
    * Constructs a FileIconView.
    * @param aFilter a file filter--all files that this filter accepts will be shown with the icon.
    * @param anIcon--the icon shown with all accepted files.
    */
   public FileIconView(FileFilter aFilter, Icon anIcon)
   {
      filter = aFilter;
      icon = anIcon;
   }

   public Icon getIcon(File f)
   {
      if (!f.isDirectory() && filter.accept(f)) return icon;
      else return null;
   }

   private FileFilter filter;
   private Icon icon;
}

/**
 * A file chooser accessory that previews images.
 */
class ImagePreviewer extends JLabel
{
   /**
    * Constructs an ImagePreviewer.
    * @param chooser the file chooser whose property changes trigger an image change in this
    * previewer
    */
   public ImagePreviewer(JFileChooser chooser)
   {
      setPreferredSize(new Dimension(100, 100));
      setBorder(BorderFactory.createEtchedBorder());

      chooser.addPropertyChangeListener(new PropertyChangeListener()
         {
            public void propertyChange(PropertyChangeEvent event)
            {
               if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)
               {
                  // the user has selected a new file
                  File f = (File) event.getNewValue();
                  if (f == null)
                  {
                     setIcon(null);
                     return;
                  }

                  // read the image into an icon
                  ImageIcon icon = new ImageIcon(f.getPath());

                  // if the icon is too large to fit, scale it
                  if (icon.getIconWidth() > getWidth()) icon = new ImageIcon(icon.getImage()
                        .getScaledInstance(getWidth(), -1, Image.SCALE_DEFAULT));

                  setIcon(icon);
               }
            }
         });
   }
}
