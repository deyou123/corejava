import java.awt.*;

/**
 * These are the English non-string resources for the retirement calculator.
 * @version 1.21 2001-08-27
 * @author Cay Horstmann
 */
public class RetireResources extends java.util.ListResourceBundle
{
   public Object[][] getContents()
   {
      return contents;
   }

   static final Object[][] contents = {
   // BEGIN LOCALIZE
         { "colorPre", Color.blue }, { "colorGain", Color.white }, { "colorLoss", Color.red }
   // END LOCALIZE
   };
}
