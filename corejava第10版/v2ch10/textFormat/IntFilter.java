package textFormat;

import javax.swing.text.*;

/**
 * A filter that restricts input to digits and a '-' sign.
 */
public class IntFilter extends DocumentFilter
{
   public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
         throws BadLocationException
   {
      StringBuilder builder = new StringBuilder(string);
      for (int i = builder.length() - 1; i >= 0; i--)
      {
         int cp = builder.codePointAt(i);
         if (!Character.isDigit(cp) && cp != '-')
         {
            builder.deleteCharAt(i);
            if (Character.isSupplementaryCodePoint(cp))
            {
               i--;
               builder.deleteCharAt(i);
            }
         }
      }
      super.insertString(fb, offset, builder.toString(), attr);
   }

   public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
         throws BadLocationException
   {
      if (string != null)
      {
         StringBuilder builder = new StringBuilder(string);
         for (int i = builder.length() - 1; i >= 0; i--)
         {
            int cp = builder.codePointAt(i);
            if (!Character.isDigit(cp) && cp != '-')
            {
               builder.deleteCharAt(i);
               if (Character.isSupplementaryCodePoint(cp))
               {
                  i--;
                  builder.deleteCharAt(i);
               }
            }
         }
         string = builder.toString();
      }
      super.replace(fb, offset, length, string, attr);
   }
}
