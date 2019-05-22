package regex;

import java.util.*;
import java.util.regex.*;

/**
 * This program tests regular expression matching. Enter a pattern and strings to match, 
 * or hit Cancel to exit. If the pattern contains groups, the group boundaries are displayed 
 * in the match.
 * @version 1.03 2018-05-01
 * @author Cay Horstmann
 */
public class RegexTest
{
   public static void main(String[] args) throws PatternSyntaxException
   {
      var in = new Scanner(System.in);
      System.out.println("Enter pattern: ");
      String patternString = in.nextLine();

      Pattern pattern = Pattern.compile(patternString);
      
      while (true)
      {
         System.out.println("Enter string to match: ");
         String input = in.nextLine();        
         if (input == null || input.equals("")) return;
         Matcher matcher = pattern.matcher(input);
         if (matcher.matches())
         {
            System.out.println("Match");
            int g = matcher.groupCount();
            if (g > 0)
            {
               for (int i = 0; i < input.length(); i++)
               {
                  // Print any empty groups
                  for (int j = 1; j <= g; j++)
                     if (i == matcher.start(j) && i == matcher.end(j)) 
                        System.out.print("()");        
                  // Print ( for non-empty groups starting here
                  for (int j = 1; j <= g; j++)
                     if (i == matcher.start(j) && i != matcher.end(j)) 
                        System.out.print('(');
                  System.out.print(input.charAt(i));
                  // Print ) for non-empty groups ending here
                  for (int j = 1; j <= g; j++)
                     if (i + 1 != matcher.start(j) && i + 1 == matcher.end(j)) 
                        System.out.print(')');
               }
               System.out.println();
            }
         }
         else
            System.out.println("No match");
      }
   }
}
