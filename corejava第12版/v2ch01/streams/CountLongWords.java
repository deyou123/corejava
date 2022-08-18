package streams;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * @version 1.02 2019-08-28
 * @author Cay Horstmann
 */
public class CountLongWords
{
   public static void main(String[] args) throws IOException
   {
      var contents = Files.readString(
         Path.of("../gutenberg/alice30.txt"));
      List<String> words = List.of(contents.split("\\PL+"));

      long count = 0;
      for (String w : words)
      {
         if (w.length() > 12) count++;
      }
      System.out.println(count);

      count = words.stream().filter(w -> w.length() > 12).count();
      System.out.println(count);

      count = words.parallelStream().filter(w -> w.length() > 12).count();
      System.out.println(count);
   }
}
