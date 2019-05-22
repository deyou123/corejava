/**
   @version 1.31 2004-05-07
   @author Cay Horstmann
*/

import java.awt.*;
import javax.swing.*;

public class CalculatorApplet extends JApplet
{  
   public void init()
   {  
      CalculatorPanel panel = new CalculatorPanel();
      add(panel);
   }
}




