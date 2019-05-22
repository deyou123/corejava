package zip;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.zip.*;

/**
 * @version 1.42 2018-03-17
 * @author Cay Horstmann
 */
public class ZipTest
{
   public static void main(String[] args) throws IOException
   {
      String zipname = args[0];
      showContents(zipname);
      System.out.println("---");
      showContents2(zipname);
   }
   
   public static void showContents(String zipname) throws IOException
   {
      // Here, we use the classic zip API
      try (var zin = new ZipInputStream(new FileInputStream(zipname)))
      {
         ZipEntry entry;
         while ((entry = zin.getNextEntry()) != null)
         {
            System.out.println(entry.getName());            
            var in = new Scanner(zin, StandardCharsets.UTF_8);
            while (in.hasNextLine())
               System.out.println("   " + in.nextLine());
            // DO NOT CLOSE in            
            zin.closeEntry();
         }
      }
   }
   
   public static void showContents2(String zipname) throws IOException
   {
      FileSystem fs = FileSystems.newFileSystem(Paths.get(zipname), null);
      Files.walkFileTree(fs.getPath("/"), new SimpleFileVisitor<Path>()
         {
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) 
                  throws IOException
            {               
               System.out.println(path);
               for (String line : Files.readAllLines(path, Charset.forName("UTF-8")))
                  System.out.println("   " + line);
               return FileVisitResult.CONTINUE;
            }
         });
   }
}
