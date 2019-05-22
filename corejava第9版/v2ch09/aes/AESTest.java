package aes;

import java.io.*;
import java.security.*;
import javax.crypto.*;

/**
 * This program tests the AES cipher. Usage:<br>
 * java aes.AESTest -genkey keyfile<br>
 * java aes.AESTest -encrypt plaintext encrypted keyfile<br>
 * java aes.AESTest -decrypt encrypted decrypted keyfile<br>
 * @author Cay Horstmann
 * @version 1.01 2012-06-10
 */
public class AESTest
{
   public static void main(String[] args) 
      throws IOException, GeneralSecurityException, ClassNotFoundException
   {
      if (args[0].equals("-genkey"))
      {
         KeyGenerator keygen = KeyGenerator.getInstance("AES");
         SecureRandom random = new SecureRandom();
         keygen.init(random);
         SecretKey key = keygen.generateKey();
         try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[1])))
         {
            out.writeObject(key);
         }
      }
      else
      {
         int mode;
         if (args[0].equals("-encrypt")) mode = Cipher.ENCRYPT_MODE;
         else mode = Cipher.DECRYPT_MODE;

         try (ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(args[3]));
            InputStream in = new FileInputStream(args[1]);
            OutputStream out = new FileOutputStream(args[2]))
         {
            Key key = (Key) keyIn.readObject();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, key);
            Util.crypt(in, out, cipher);
         }
      }
   }
}

