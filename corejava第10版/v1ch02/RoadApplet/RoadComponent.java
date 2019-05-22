import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class RoadComponent extends JComponent
{  
   private final int DOTSIZE = 2;   
   private final int XDOTDIST = 1;
   private Road freeway;
   private BufferedImage buffer;
   private int row;
   private int xsize;
   private int ysize;

   public RoadComponent()
   {  
      freeway = new Road();
      row = 0;      
   }

   public void update(double slowdown, double arrival)
   {  
      freeway.update(slowdown, arrival);
      Graphics g;
      if (buffer == null) 
      {  
         xsize = getWidth();
         ysize = getHeight();
         if (xsize == 0 || ysize == 0) return;
         buffer = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_ARGB);
         g = buffer.getGraphics();
         g.setColor(Color.WHITE);
         g.fillRect(0, 0, xsize, ysize);
      }
      else
         g = buffer.getGraphics();
      
      if (row < ysize - 2 * DOTSIZE) 
         row += DOTSIZE;
      else
      {  
         g.copyArea(0, DOTSIZE, xsize, ysize - DOTSIZE, 0, -DOTSIZE);
      }
      freeway.paint(g, row, XDOTDIST, DOTSIZE);
      g.dispose();
      repaint();
   }

   public void paintComponent(Graphics g)
   {  
      if (buffer != null) 
         g.drawImage(buffer, 0, 0, null);  
   }
}

class Road
{
   public static final int LENGTH = 400;
   public static final int MAXSPEED = 5;

   private int[] speed;
   private Color[] colors;
   private int count;

   public Road()
   {  
      speed = new int[LENGTH];
      colors = new Color[LENGTH];
      for (int i = 0; i < LENGTH; i++) speed[i] = -1;
   }
   
   public void update(double prob_slowdown, double prob_create)
   {  
      int i = 0;
      while(i < LENGTH && speed[i] == -1) 
         i++;
      while (i < LENGTH)
      {  
         if (Math.random() <= prob_slowdown && speed[i] > 0)
            speed[i]--;
         else if (speed[i] < MAXSPEED)
            speed[i]++;
         int inext = i + 1;
         while(inext < LENGTH && speed[inext] == -1) 
            inext++;
         if (inext < LENGTH)
         {  
            if (speed[i] >= inext - i) 
               speed[i] = inext - i - 1;
         }
         if (speed[i] > 0)
         {  
            if (i + speed[i] < LENGTH)
            {
               int ni = i + speed[i];
               speed[ni] = speed[i];
               colors[ni] = colors[i];
            }
            speed[i] = -1;
         }
         i = inext;
      }
      if (Math.random() <= prob_create && speed[0] == -1)
      {
         speed[0] = (int)(5.99 * Math.random());
         colors[0] = ++count % 10 == 0 ? Color.red : Color.black;
      }
   }
   
   public void paint(Graphics g, int row, int dotdist, int dotsize)
   {  
      g.setColor(Color.WHITE);
      g.fillRect(0, row, LENGTH * dotsize, dotsize);
      for (int i = 0; i < LENGTH; i++)
      {
         if (speed[i] >= 0) 
         {
            g.setColor(colors[i]);
            g.fillRect(i * dotdist, row, dotsize, dotsize);
         }
      }
   }
}
