/**
   @version 1.22 2004-05-11
   @author Cay Horstmann
*/

import java.awt.*;
import javax.swing.*;

public class BuggyButtonFrame extends JFrame
{
   public BuggyButtonFrame()
   {
      setTitle("BuggyButtonTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add panel to frame

      BuggyButtonPanel panel = new BuggyButtonPanel();
      add(panel);
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;  
}
