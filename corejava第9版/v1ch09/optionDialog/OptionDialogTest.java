package optionDialog;

import java.awt.*;
import javax.swing.*;

/**
 * @version 1.33 2007-04-28
 * @author Cay Horstmann
 */
public class OptionDialogTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new OptionDialogFrame();
               frame.setTitle("OptionDialogTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

