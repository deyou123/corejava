package damageReporter;

import javax.swing.*;

/**
 * This program demonstrates the use of an XML encoder and decoder. All GUI and drawing code is
 * collected in this class. The only interesting pieces are the action listeners for openItem and
 * saveItem. Look inside the DamageReport class for encoder customizations.
 * @version 1.02 2012-01-26
 * @author Cay Horstmann
 */
public class DamageReporter extends JFrame
{
   public static void main(String[] args)
   {
      JFrame frame = new DamageReporterFrame();
      frame.setTitle("DamageReporter");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}
