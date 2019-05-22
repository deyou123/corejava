/**
   @version 1.31 2004-05-03
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CenteredFrameTest
{  
   public static void main(String[] args)
   {  
      CenteredFrame frame = new CenteredFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);  
   }
}

class CenteredFrame extends JFrame
{
   public CenteredFrame()
   {
      // get screen dimensions

      Toolkit kit = Toolkit.getDefaultToolkit();
      Dimension screenSize = kit.getScreenSize();
      int screenHeight = screenSize.height;
      int screenWidth = screenSize.width;

      // center frame in screen

      setSize(screenWidth / 2, screenHeight / 2);
      setLocation(screenWidth / 4, screenHeight / 4);

      // set frame icon and title

      Image img = kit.getImage("icon.gif");
      setIconImage(img);
      setTitle("CenteredFrame");
   }
}

