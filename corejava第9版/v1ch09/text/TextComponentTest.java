package text;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.40 2007-04-27
 * @author Cay Horstmann
 */
public class TextComponentTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new TextComponentFrame();
               frame.setTitle("TextComponentTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

