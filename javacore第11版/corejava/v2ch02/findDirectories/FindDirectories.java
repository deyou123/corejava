package findDirectories;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

/**
 * @version 1.1 2012-05-31
 * @author Cay Horstmann
 */
public class FindDirectories
{
   public static void main(String[] args) throws IOException
   {
      Path dir = Paths.get(args.length == 0 ? System.getProperty("user.home") : args[0]);
      Files.walkFileTree(dir, new SimpleFileVisitor<Path>()
         {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
                  throws IOException
            {               
               if (attrs.isDirectory()) 
                  System.out.println(file);
               return FileVisitResult.CONTINUE;
            }
            public FileVisitResult visitFileFailed(Path file, IOException exc) 
                  throws IOException
            {
               return FileVisitResult.CONTINUE;
            }
         });
   }
}
