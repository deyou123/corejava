package compiler;

import java.net.*;
import javax.tools.*;

/**
 * A Java source that holds the code in a string builder.
 * @version 1.00 2007-11-02
 * @author Cay Horstmann
 */
public class StringBuilderJavaSource extends SimpleJavaFileObject
{
   private StringBuilder code;

   /**
    * Constructs a new StringBuilderJavaSource.
    * @param name the name of the source file represented by this file object
    */
   public StringBuilderJavaSource(String name)
   {
      super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), 
         Kind.SOURCE);
      code = new StringBuilder();
   }

   public CharSequence getCharContent(boolean ignoreEncodingErrors)
   {
      return code;
   }

   public void append(String str)
   {
      code.append(str);
      code.append('\n');
   }
}
