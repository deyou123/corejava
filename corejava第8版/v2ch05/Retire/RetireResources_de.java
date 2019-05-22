import java.awt.*;

/**
 * These are the German non-string resources for the retirement calculator.
 * @version 1.21 2001-08-27
 * @author Cay Horstmann
 */
public class RetireResources_de extends java.util.ListResourceBundle
{
   public Object[][] getContents()
   {
      return contents;
   }

   static final Object[][] contents = {
   // BEGIN LOCALIZE
         { "colorPre", Color.yellow }, { "colorGain", Color.black }, { "colorLoss", Color.red }
   // END LOCALIZE
   };
}
