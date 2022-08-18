package rsa;

import java.io.*;
import java.security.*;
import javax.crypto.*;

public class Util
{
   /**
    * Uses a cipher to transform the bytes in an input stream and sends the transformed bytes to an
    * output stream.
    * @param in the input stream
    * @param out the output stream
    * @param cipher the cipher that transforms the bytes
    */
   public static void crypt(InputStream in, OutputStream out, Cipher cipher) 
         throws IOException, GeneralSecurityException
   {
      int blockSize = cipher.getBlockSize();
      int outputSize = cipher.getOutputSize(blockSize);
      var inBytes = new byte[blockSize];
      var outBytes = new byte[outputSize];

      int inLength = 0;
      
      boolean done = false;
      while (!done)
      {
         inLength = in.read(inBytes);
         if (inLength == blockSize)
         {
            int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
            out.write(outBytes, 0, outLength);
         }
         else done = true;
      }
      if (inLength > 0) outBytes = cipher.doFinal(inBytes, 0, inLength);
      else outBytes = cipher.doFinal();
      out.write(outBytes);
   }
}
