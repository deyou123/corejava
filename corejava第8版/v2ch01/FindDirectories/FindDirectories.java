import java.io.*;

/**
 * @version 1.00 05 Sep 1997
 * @author Gary Cornell
 */
public class FindDirectories
{
   public static void main(String[] args)
   {
      // if no arguments provided, start at the parent directory
      if (args.length == 0) args = new String[] { ".." };

      try
      {
         File pathName = new File(args[0]);
         String[] fileNames = pathName.list();

         // enumerate all files in the directory
         for (int i = 0; i < fileNames.length; i++)
         {
            File f = new File(pathName.getPath(), fileNames[i]);

            // if the file is again a directory, call the main method recursively
            if (f.isDirectory())
            {
               System.out.println(f.getCanonicalPath());
               main(new String[] { f.getPath() });
            }
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
