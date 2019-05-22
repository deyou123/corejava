import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This program makes a socket connection to the atomic clock in Boulder, Colorado, and prints the
 * time that the server sends.
 * @version 1.20 2004-08-03
 * @author Cay Horstmann
 */
public class SocketTest
{
   public static void main(String[] args)
   {
      try
      {
         Socket s = new Socket("time-A.timefreq.bldrdoc.gov", 13);
         try
         {
            InputStream inStream = s.getInputStream();
            Scanner in = new Scanner(inStream);

            while (in.hasNextLine())
            {
               String line = in.nextLine();
               System.out.println(line);
            }
         }
         finally
         {
            s.close();
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
