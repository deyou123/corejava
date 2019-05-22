package image;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.34 2015-05-12
 * @author Cay Horstmann
 */
public class ImageTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            JFrame frame = new ImageFrame();
            frame.setTitle("ImageTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * A frame with an image component
 */
class ImageFrame extends JFrame
{
   public ImageFrame()
   {
      add(new ImageComponent());
      pack();
   }
}

/**
 * A component that displays a tiled image
 */
class ImageComponent extends JComponent
{
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;

   private Image image;

   public ImageComponent()
   {
      image = new ImageIcon("blue-ball.gif").getImage();
   }

   public void paintComponent(Graphics g)
   {
      if (image == null) return;

      int imageWidth = image.getWidth(null);
      int imageHeight = image.getHeight(null);

      // draw the image in the upper-left corner

      g.drawImage(image, 0, 0, null);
      // tile the image across the component

      for (int i = 0; i * imageWidth <= getWidth(); i++)
         for (int j = 0; j * imageHeight <= getHeight(); j++)
            if (i + j > 0) 
               g.copyArea(0, 0, imageWidth, imageHeight, i * imageWidth, j * imageHeight);
   }
   
   public Dimension getPreferredSize() { return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); }
}
