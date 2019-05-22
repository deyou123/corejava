package listFonts;

import java.awt.*;

/**
 * @version 1.11 2004-06-05
 * @author Cay Horstmann
 */
public class ListFonts
{
   public static void main(String[] args)
   {
      String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getAvailableFontFamilyNames();

      for (String fontName : fontNames)
         System.out.println(fontName);
   }
}
