/**
   @version 1.32 2004-07-27
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**
   Shows an animated bouncing ball.
*/
public class BounceThread
{
   public static void main(String[] args)
   {
      JFrame frame = new BounceFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A runnable that animates a bouncing ball.
*/
class BallRunnable implements Runnable
{
   /**
      Constructs the runnable.
      @aBall the ball to bounce
      @aPanel the component in which the ball bounces
   */
   public BallRunnable(Ball aBall, Component aComponent) 
   { 
      ball = aBall; 
      component = aComponent;
   }

   public void run()
   {
      try
      {
         for (int i = 1; i <= STEPS; i++)
         {
            ball.move(component.getBounds());
            component.repaint();
            Thread.sleep(DELAY);
         }
      }
      catch (InterruptedException e)
      {                    
      }
   }

   private Ball ball;
   private Component component;
   public static final int STEPS = 1000;
   public static final int DELAY = 5;
}

/**
   A ball that moves and bounces off the edges of a 
   rectangle
*/
class Ball
{
   /**
      Moves the ball to the next position, reversing direction
      if it hits one of the edges
   */
   public void move(Rectangle2D bounds)
   {
      x += dx;
      y += dy;
      if (x < bounds.getMinX())
      { 
         x = bounds.getMinX();
         dx = -dx;
      }
      if (x + XSIZE >= bounds.getMaxX())
      {
         x = bounds.getMaxX() - XSIZE; 
         dx = -dx; 
      }
      if (y < bounds.getMinY())
      {
         y = bounds.getMinY(); 
         dy = -dy;
      }
      if (y + YSIZE >= bounds.getMaxY())
      {
         y = bounds.getMaxY() - YSIZE;
         dy = -dy; 
      }
   }

   /**
      Gets the shape of the ball at its current position.
   */
   public Ellipse2D getShape()
   {
      return new Ellipse2D.Double(x, y, XSIZE, YSIZE);
   }

   private static final int XSIZE = 15;
   private static final int YSIZE = 15;
   private double x = 0;
   private double y = 0;
   private double dx = 1;
   private double dy = 1;
}

/**
   The panel that draws the balls.
*/
class BallPanel extends JPanel
{
   /**
      Add a ball to the panel.
      @param b the ball to add
   */
   public void add(Ball b)
   {
      balls.add(b);
   }

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      for (Ball b : balls)
      {
         g2.fill(b.getShape());
      }
   }

   private ArrayList<Ball> balls = new ArrayList<Ball>();
}

/**
   The frame with panel and buttons.
*/
class BounceFrame extends JFrame
{
   /**
      Constructs the frame with the panel for showing the
      bouncing ball and Start and Close buttons
   */
   public BounceFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      setTitle("BounceThread");

      panel = new BallPanel();
      add(panel, BorderLayout.CENTER);
      JPanel buttonPanel = new JPanel();
      addButton(buttonPanel, "Start",
         new ActionListener()
         {  
            public void actionPerformed(ActionEvent event)
            {
               addBall();
            }
         });
      
      addButton(buttonPanel, "Close",
         new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });
      add(buttonPanel, BorderLayout.SOUTH);
   }

   /**
      Adds a button to a container.
      @param c the container
      @param title the button title
      @param listener the action listener for the button
   */
   public void addButton(Container c, String title, ActionListener listener)
   {
      JButton button = new JButton(title);
      c.add(button);
      button.addActionListener(listener);
   }

   /**
      Adds a bouncing ball to the canvas and starts a thread
      to make it bounce
   */
   public void addBall()
   {
      Ball b = new Ball();
      panel.add(b);
      Runnable r = new BallRunnable(b, panel);
      Thread t = new Thread(r);
      t.start();
   }

   private BallPanel panel;
   public static final int DEFAULT_WIDTH = 450;
   public static final int DEFAULT_HEIGHT = 350;  
   public static final int STEPS = 1000;
   public static final int DELAY = 3;
}

















