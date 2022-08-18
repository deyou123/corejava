package numberFormat;

import java.text.*;
import java.util.*;
import util.*;

/**
 * This program demonstrates formatting numbers under various locales.
 * @version 2.0 2021-09-22
 * @author Cay Horstmann
 */
public class NumberFormatTest2
{
   public static void main(String[] args)
   {
      Scanner in = new Scanner(System.in);
      var locales = (Locale[]) NumberFormat.getAvailableLocales().clone();
      Arrays.sort(locales, Comparator.comparing(Locale::getDisplayName));
      Locale loc = Choices.choose(in, locales, Locale::getDisplayName);
      
      var formatters = new LinkedHashMap<NumberFormat, String>();
      formatters.put(NumberFormat.getNumberInstance(loc), "Number");
      formatters.put(NumberFormat.getCompactNumberInstance(loc, NumberFormat.Style.SHORT), "Compact Short");
      formatters.put(NumberFormat.getCompactNumberInstance(loc, NumberFormat.Style.LONG), "Compact Long");
      formatters.put(NumberFormat.getPercentInstance(loc), "Percent");
      formatters.put(NumberFormat.getCurrencyInstance(loc), "Currency");
      NumberFormat formatter = Choices.choose(in, formatters);
      
      String operation = Choices.choose(in, "Format", "Parse");
      if (operation.equals("Format"))
      {
         System.out.print("Enter a floating-point number to format: ");
         double number = in.nextDouble();
         System.out.println(formatter.format(number));
      }
      else
      {
         System.out.print("Enter a floating-point number to parse: ");
         String text = in.next();
         try 
         {
            System.out.println(formatter.parse(text));
         } 
         catch (ParseException e) 
         {
            System.out.println("ParseException " + e.getMessage());
         }
      }
   }
}
