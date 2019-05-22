/**
 * @version 1.10 1999-07-07
 * @author Cay Horstmann
 */

import java.io.*;
import java.util.*;

public class DirStack
{  public static void main(String[] args)
   {  Stack directoryStack = new Stack();
      File root = new File(File.separator + ".");
      directoryStack.push(root);
      while (directoryStack.size() > 0)
      {  File currentDirectory = (File)directoryStack.pop();
         System.out.println(currentDirectory);
         String[] subdirectories = currentDirectory.list();
         if (subdirectories != null)
         {  for (int i = 0; i < subdirectories.length; i++)
            {  try
               {  String fname
                     = currentDirectory.getCanonicalPath()
                        + File.separator + subdirectories[i];
                  File f = new File(fname);

                  if (f.isDirectory())
                     directoryStack.push(f);
               }
               catch(IOException e)
               {  System.out.println(e);
               }
            }
         }
      }
   }
}
