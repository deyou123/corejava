package compiler;

import java.net.*;
import javax.tools.*;

public class StringSource extends SimpleJavaFileObject
{
   private String code;

   StringSource(String name, String code)
   {
      super(URI.create("string:///" + name.replace('.', '/') + ".java"), Kind.SOURCE);
      this.code = code;
   }

   public CharSequence getCharContent(boolean ignoreEncodingErrors)
   {
      return code;
   }
}
