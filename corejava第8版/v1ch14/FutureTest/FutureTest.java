import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @version 1.0 2004-08-01
 * @author Cay Horstmann
 */
public class FutureTest
{
   public static void main(String[] args)
   {
      Scanner in = new Scanner(System.in);
      System.out.print("Enter base directory (e.g. /usr/local/jdk5.0/src): ");
      String directory = in.nextLine();
      System.out.print("Enter keyword (e.g. volatile): ");
      String keyword = in.nextLine();

      MatchCounter counter = new MatchCounter(new File(directory), keyword);
      FutureTask<Integer> task = new FutureTask<Integer>(counter);
      Thread t = new Thread(task);
      t.start();
      try
      {
         System.out.println(task.get() + " matching files.");
      }
      catch (ExecutionException e)
      {
         e.printStackTrace();
      }
      catch (InterruptedException e)
      {
      }
   }
}

/**
 * This task counts the files in a directory and its subdirectories that contain a given keyword.
 */
class MatchCounter implements Callable<Integer>
{
   /**
    * Constructs a MatchCounter.
    * @param directory the directory in which to start the search
    * @param keyword the keyword to look for
    */
   public MatchCounter(File directory, String keyword)
   {
      this.directory = directory;
      this.keyword = keyword;
   }

   public Integer call()
   {
      count = 0;
      try
      {
         File[] files = directory.listFiles();
         ArrayList<Future<Integer>> results = new ArrayList<Future<Integer>>();

         for (File file : files)
            if (file.isDirectory())
            {
               MatchCounter counter = new MatchCounter(file, keyword);
               FutureTask<Integer> task = new FutureTask<Integer>(counter);
               results.add(task);
               Thread t = new Thread(task);
               t.start();
            }
            else
            {
               if (search(file)) count++;
            }

         for (Future<Integer> result : results)
            try
            {
               count += result.get();
            }
            catch (ExecutionException e)
            {
               e.printStackTrace();
            }
      }
      catch (InterruptedException e)
      {
      }
      return count;
   }

   /**
    * Searches a file for a given keyword.
    * @param file the file to search
    * @return true if the keyword is contained in the file
    */
   public boolean search(File file)
   {
      try
      {
         Scanner in = new Scanner(new FileInputStream(file));
         boolean found = false;
         while (!found && in.hasNextLine())
         {
            String line = in.nextLine();
            if (line.contains(keyword)) found = true;
         }
         in.close();
         return found;
      }
      catch (IOException e)
      {
         return false;
      }
   }

   private File directory;
   private String keyword;
   private int count;
}
