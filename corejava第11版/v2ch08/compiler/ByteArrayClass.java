package compiler;

import java.io.*;
import java.net.*;

import javax.tools.*;

public class ByteArrayClass extends SimpleJavaFileObject
{
   private ByteArrayOutputStream out;

   ByteArrayClass(String name)
   {
      super(URI.create("bytes:///" + name.replace('.', '/') + ".class"),
            Kind.CLASS);
   }

   public byte[] getCode()
   {
      return out.toByteArray();
   }

   public OutputStream openOutputStream() throws IOException
   {
      out = new ByteArrayOutputStream();
      return out;
   }
}
