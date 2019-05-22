/**
   @version 1.02 2004-05-11
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class RobotTest
{
   public static void main(String[] args)
   {  
      // make frame with a button panel

      ButtonFrame frame = new ButtonFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);

      // attach a robot to the screen device

      GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice screen = environment.getDefaultScreenDevice();
      
      try
      {
         Robot robot = new Robot(screen);
         run(robot);
      }
      catch (AWTException e)
      {
         e.printStackTrace();
      }
   }

   /**
      Runs a sample test procedure
      @param robot the robot attached to the screen device
   */
   public static void run(Robot robot)
   {
      // simulate a space bar press
      robot.keyPress(' ');
      robot.keyRelease(' ');
      
      // simulate a tab key followed by a space
      robot.delay(2000);
      robot.keyPress(KeyEvent.VK_TAB);
      robot.keyRelease(KeyEvent.VK_TAB);
      robot.keyPress(' ');
      robot.keyRelease(' ');
      
      // simulate a mouse click over the rightmost button
      robot.delay(2000);
      robot.mouseMove(200, 50);
      robot.mousePress(InputEvent.BUTTON1_MASK);
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
      
      // capture the screen and show the resulting image
      robot.delay(2000);
      BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, 400, 300));
      
      ImageFrame frame = new ImageFrame(image);
      frame.setVisible(true);
   }
}

/**
   A frame to display a captured image
*/
class ImageFrame extends JFrame
{
   /**
      @param image the image to display
   */
   public ImageFrame(Image image)
   {
      setTitle("Capture");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      
      JLabel label = new JLabel(new ImageIcon(image));
      add(label);
   }

   public static final int DEFAULT_WIDTH = 450;
   public static final int DEFAULT_HEIGHT = 350;  
}
