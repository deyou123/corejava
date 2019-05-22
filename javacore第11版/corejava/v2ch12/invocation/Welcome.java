/**
 * @version 1.21 2018-05-01
 * @author Cay Horstmann
 */
public class Welcome
{
   public static void main(String[] args)
   {
      var greeting = new String[3];
      greeting[0] = "Welcome to Core Java";
      greeting[1] = "by Cay Horstmann";
      greeting[2] = "and Gary Cornell";

      for (String g : greeting)
         System.out.println(g);
   }
}
