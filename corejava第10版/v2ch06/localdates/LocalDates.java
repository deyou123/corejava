package localdates;

import java.time.*;
import java.time.temporal.*;

public class LocalDates
{
   public static void main(String[] args)
   {
      LocalDate today = LocalDate.now(); // Today’s date
      System.out.println("today: " + today);

      LocalDate alonzosBirthday = LocalDate.of(1903, 6, 14);
      alonzosBirthday = LocalDate.of(1903, Month.JUNE, 14);
      // Uses the Month enumeration
      System.out.println("alonzosBirthday: " + alonzosBirthday);

      LocalDate programmersDay = LocalDate.of(2018, 1, 1).plusDays(255);
      // September 13, but in a leap year it would be September 12
      System.out.println("programmersDay: " + programmersDay);

      LocalDate independenceDay = LocalDate.of(2018, Month.JULY, 4);
      LocalDate christmas = LocalDate.of(2018, Month.DECEMBER, 25);

      System.out
            .println("Until christmas: " + independenceDay.until(christmas));
      System.out.println("Until christmas: "
            + independenceDay.until(christmas, ChronoUnit.DAYS));

      System.out.println(LocalDate.of(2016, 1, 31).plusMonths(1));
      System.out.println(LocalDate.of(2016, 3, 31).minusMonths(1));

      DayOfWeek startOfLastMillennium = LocalDate.of(1900, 1, 1).getDayOfWeek();
      System.out.println("startOfLastMillennium: " + startOfLastMillennium);
      System.out.println(startOfLastMillennium.getValue());
      System.out.println(DayOfWeek.SATURDAY.plus(3));
   }
}
