package verifier;

import java.applet.*;
import java.awt.*;

/**
 * This application demonstrates the bytecode verifier of the virtual machine. If you use a hex
 * editor to modify the class file, then the virtual machine should detect the tampering.
 * @version 1.00 1997-09-10
 * @author Cay Horstmann
 */
public class VerifierTest extends Applet
{
   public static void main(String[] args)
   {
      System.out.println("1 + 2 == " + fun());
   }

   /**
    * A function that computes 1 + 2.
    * @return 3, if the code has not been corrupted
    */
   public static int fun()
   {
      int m;
      int n;
      m = 1;
      n = 2;
      // use hex editor to change to "m = 2" in class file
      int r = m + n;
      return r;
   }

   public void paint(Graphics g)
   {
      g.drawString("1 + 2 == " + fun(), 20, 20);
   }
}
