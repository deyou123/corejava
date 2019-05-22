package compiler;

import java.io.*;
import java.net.*;
import javax.tools.*;

/**
 * A Java class that holds the bytecodes in a byte array.
 * @version 1.00 2007-11-02
 * @author Cay Horstmann
 */
public class ByteArrayJavaClass extends SimpleJavaFileObject
{
   private ByteArrayOutputStream stream;

   /**
    * Constructs a new ByteArrayJavaClass.
    * @param name the name of the class file represented by this file object
    */
   public ByteArrayJavaClass(String name)
   {
      super(URI.create("bytes:///" + name), Kind.CLASS);
      stream = new ByteArrayOutputStream();
   }

   public OutputStream openOutputStream() throws IOException
   {
      return stream;
   }
   
   public byte[] getBytes()
   {
      return stream.toByteArray();
   }
}
