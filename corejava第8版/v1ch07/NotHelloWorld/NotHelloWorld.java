import javax.swing.*;
import java.awt.*;

/**
 * @version 1.32 2007-06-12
 * @author Cay Horstmann
 */
public class NotHelloWorld
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               NotHelloWorldFrame frame = new NotHelloWorldFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame that contains a message panel
 */
class NotHelloWorldFrame extends JFrame
{
   public NotHelloWorldFrame()
   {
      setTitle("NotHelloWorld");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add panel to frame

      NotHelloWorldComponent comp = new NotHelloWorldComponent();
      add(comp);
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;
}

/**
 * A component that displays a message.
 */
class NotHelloWorldComponent extends JComponent
{
   public void paintComponent(Graphics g)
   {
      g.drawString("Not a Hello, World program", MESSAGE_X, MESSAGE_Y);
   }

   public static final int MESSAGE_X = 75;
   public static final int MESSAGE_Y = 100;
}
