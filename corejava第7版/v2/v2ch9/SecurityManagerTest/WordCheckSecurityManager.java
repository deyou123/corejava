/**
   @version 1.20 2004-09-11
   @author Cay Horstmann
*/

import java.io.*;
import java.security.*;
import java.util.*;

/**
   This security manager checks whether bad words are
   encountered when reading a file.
*/
public class WordCheckSecurityManager extends SecurityManager
{  
   public void checkPermission(Permission p)
   {  
      if (p instanceof FilePermission && p.getActions().equals("read"))
      {  
         if (inSameManager())
            return;
         String fileName = p.getName();
         if (containsBadWords(fileName))
            throw new SecurityException("Bad words in " + fileName);
      }
      else super.checkPermission(p);
   }

   /**
      Returns true if this manager is called while there
      is another call to itself pending.
      @return true if there are multiple calls to this manager
   */
   public boolean inSameManager()
   {  
      Class[] cc = getClassContext();

      // skip past current set of calls to this manager
      int i = 0;
      while (i < cc.length && cc[0] == cc[i])
         i++;

      // check if there is another call to this manager
      while (i < cc.length)
      {  
         if (cc[0] == cc[i]) return true;
         i++;
      }
      return false;
   }

   /**
      Checks if a file contains bad words.
      @param fileName the name of the file
      @return true if the file name ends with .txt and it 
      contains at least one bad word.
   */
   boolean containsBadWords(String fileName)
   {  
      if (!fileName.toLowerCase().endsWith(".txt")) return false;
         // only check text files
      Scanner in; 
      try
      {
         in = new Scanner(new FileReader(fileName));
      }
      catch (IOException e)
      {
         return false;
      }
      while (in.hasNext())
         if (badWords.contains(in.next().toLowerCase()))
         {
            in.close();
            System.out.println(fileName);
            return true;
         }
      return false;
   }

   private List badWords = Arrays.asList(new String[] { "sex", "drugs", "c++" });
}
