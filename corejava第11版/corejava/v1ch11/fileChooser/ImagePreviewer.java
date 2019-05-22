package fileChooser;

import java.awt.*;
import java.io.*;

import javax.swing.*;

/**
 * A file chooser accessory that previews images.
 */
public class ImagePreviewer extends JLabel
{
   /**
    * Constructs an ImagePreviewer.
    * @param chooser the file chooser whose property changes trigger an image
    *        change in this previewer
    */
   public ImagePreviewer(JFileChooser chooser)
   {
      setPreferredSize(new Dimension(100, 100));
      setBorder(BorderFactory.createEtchedBorder());

      chooser.addPropertyChangeListener(event -> {
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
            var icon = new ImageIcon(f.getPath());

            // if the icon is too large to fit, scale it
            if (icon.getIconWidth() > getWidth())
               icon = new ImageIcon(icon.getImage().getScaledInstance(
                     getWidth(), -1, Image.SCALE_DEFAULT));

            setIcon(icon);
         }
      });
   }
}
