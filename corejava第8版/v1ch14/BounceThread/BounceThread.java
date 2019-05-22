import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Shows animated bouncing balls.
 * @version 1.33 2007-05-17
 * @author Cay Horstmann
 */
public class BounceThread
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new BounceFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A runnable that animates a bouncing ball.
 */
class BallRunnable implements Runnable
{
   /**
    * Constructs the runnable.
    * @aBall the ball to bounce
    * @aPanel the component in which the ball bounces
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
 * The frame with panel and buttons.
 */
class BounceFrame extends JFrame
{
   /**
    * Constructs the frame with the component for showing the bouncing ball and Start and Close
    * buttons
    */
   public BounceFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      setTitle("BounceThread");

      comp = new BallComponent();
      add(comp, BorderLayout.CENTER);
      JPanel buttonPanel = new JPanel();
      addButton(buttonPanel, "Start", new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               addBall();
            }
         });

      addButton(buttonPanel, "Close", new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });
      add(buttonPanel, BorderLayout.SOUTH);
   }

   /**
    * Adds a button to a container.
    * @param c the container
    * @param title the button title
    * @param listener the action listener for the button
    */
   public void addButton(Container c, String title, ActionListener listener)
   {
      JButton button = new JButton(title);
      c.add(button);
      button.addActionListener(listener);
   }

   /**
    * Adds a bouncing ball to the canvas and starts a thread to make it bounce
    */
   public void addBall()
   {
      Ball b = new Ball();
      comp.add(b);
      Runnable r = new BallRunnable(b, comp);
      Thread t = new Thread(r);
      t.start();
   }

   private BallComponent comp;
   public static final int DEFAULT_WIDTH = 450;
   public static final int DEFAULT_HEIGHT = 350;
   public static final int STEPS = 1000;
   public static final int DELAY = 3;
}
