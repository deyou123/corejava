package fileChooser;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

/**
 * A file view that displays an icon for all files that match a file filter.
 */
public class FileIconView extends FileView
{
   private FileFilter filter;
   private Icon icon;

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
}
