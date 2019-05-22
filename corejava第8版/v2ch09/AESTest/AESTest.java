import java.io.*;
import java.security.*;
import javax.crypto.*;

/**
 * This program tests the AES cipher. Usage:<br>
 * java AESTest -genkey keyfile<br>
 * java AESTest -encrypt plaintext encrypted keyfile<br>
 * java AESTest -decrypt encrypted decrypted keyfile<br>
 * @author Cay Horstmann
 * @version 1.0 2004-09-14
 */
public class AESTest
{
   public static void main(String[] args)
   {
      try
      {
         if (args[0].equals("-genkey"))
         {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keygen.init(random);
            SecretKey key = keygen.generateKey();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[1]));
            out.writeObject(key);
            out.close();
         }
         else
         {
            int mode;
            if (args[0].equals("-encrypt")) mode = Cipher.ENCRYPT_MODE;
            else mode = Cipher.DECRYPT_MODE;

            ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(args[3]));
            Key key = (Key) keyIn.readObject();
            keyIn.close();

            InputStream in = new FileInputStream(args[1]);
            OutputStream out = new FileOutputStream(args[2]);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, key);

            crypt(in, out, cipher);
            in.close();
            out.close();
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      catch (GeneralSecurityException e)
      {
         e.printStackTrace();
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Uses a cipher to transform the bytes in an input stream and sends the transformed bytes to an
    * output stream.
    * @param in the input stream
    * @param out the output stream
    * @param cipher the cipher that transforms the bytes
    */
   public static void crypt(InputStream in, OutputStream out, Cipher cipher) throws IOException,
         GeneralSecurityException
   {
      int blockSize = cipher.getBlockSize();
      int outputSize = cipher.getOutputSize(blockSize);
      byte[] inBytes = new byte[blockSize];
      byte[] outBytes = new byte[outputSize];

      int inLength = 0;
      boolean more = true;
      while (more)
      {
         inLength = in.read(inBytes);
         if (inLength == blockSize)
         {
            int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
            out.write(outBytes, 0, outLength);
         }
         else more = false;
      }
      if (inLength > 0) outBytes = cipher.doFinal(inBytes, 0, inLength);
      else outBytes = cipher.doFinal();
      out.write(outBytes);
   }
}
