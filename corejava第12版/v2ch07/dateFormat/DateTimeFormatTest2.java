package dateFormat;

import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import util.*;

/**
 * This program demonstrates formatting dates under various locales.
 * @version 2.0 2021-09-23
 * @author Cay Horstmann
 */
public class DateTimeFormatTest2
{
   public static void main(String[] args)
   {
      Scanner in = new Scanner(System.in);
      var locales = (Locale[]) NumberFormat.getAvailableLocales().clone();
      Arrays.sort(locales, Comparator.comparing(Locale::getDisplayName));
      Locale loc = Choices.choose(in, locales, Locale::getDisplayName);
      FormatStyle style = Choices.choose(in, FormatStyle.class, 
            "Short", "Medium", "Long", "Full");
      String type = Choices.choose(in, "Date", "Time", "Date and Time");

      if (type.equals("Date"))
      {
         DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(
               style).withLocale(loc);
         System.out.println(formatter.format(LocalDate.now()));
         System.out.print("Enter another date: ");
         String input = in.nextLine();
         LocalDate date = LocalDate.parse(input, formatter);
         System.out.println(formatter.format(date));         
      }
      else if (type.equals("Time"))
      {
         DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(
               style).withLocale(loc);
         System.out.println(formatter.format(LocalTime.now()));
         System.out.print("Enter another time: ");
         String input = in.nextLine();
         LocalTime time = LocalTime.parse(input, formatter);
         System.out.println(formatter.format(time));         
      }
      else
      {
         DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(
               style).withLocale(loc);
         System.out.println(formatter.format(ZonedDateTime.now()));
         System.out.print("Enter another date and time: ");
         String input = in.nextLine();
         ZonedDateTime dateTime = ZonedDateTime.parse(input, formatter);
         System.out.println(formatter.format(dateTime));         
      }         
   }
}