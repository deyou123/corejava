import java.io.*;
import java.security.*;

/**
 * This program demonstrates how to sign a message with a private DSA key and verify it with the
 * matching public key. Usage:<br>
 * java SignatureTest -genkeypair public private<br>
 * java SignatureTest -sign message signed private<br>
 * java SignatureTest -verify signed public<br>
 * @version 1.11 2007-10-06
 * @author Cay Horstmann
 */
public class SignatureTest
{
   public static void main(String[] args)
   {
      try
      {
         if (args[0].equals("-genkeypair"))
         {
            KeyPairGenerator pairgen = KeyPairGenerator.getInstance("DSA");
            SecureRandom random = new SecureRandom();
            pairgen.initialize(KEYSIZE, random);
            KeyPair keyPair = pairgen.generateKeyPair();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[1]));
            out.writeObject(keyPair.getPublic());
            out.close();
            out = new ObjectOutputStream(new FileOutputStream(args[2]));
            out.writeObject(keyPair.getPrivate());
            out.close();
         }
         else if (args[0].equals("-sign"))
         {
            ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(args[3]));
            PrivateKey privkey = (PrivateKey) keyIn.readObject();
            keyIn.close();

            Signature signalg = Signature.getInstance("DSA");
            signalg.initSign(privkey);

            File infile = new File(args[1]);
            InputStream in = new FileInputStream(infile);
            int length = (int) infile.length();
            byte[] message = new byte[length];
            in.read(message, 0, length);
            in.close();

            signalg.update(message);
            byte[] signature = signalg.sign();

            DataOutputStream out = new DataOutputStream(new FileOutputStream(args[2]));
            int signlength = signature.length;
            out.writeInt(signlength);
            out.write(signature, 0, signlength);
            out.write(message, 0, length);
            out.close();
         }
         else if (args[0].equals("-verify"))
         {
            ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(args[2]));
            PublicKey pubkey = (PublicKey) keyIn.readObject();
            keyIn.close();

            Signature verifyalg = Signature.getInstance("DSA");
            verifyalg.initVerify(pubkey);

            File infile = new File(args[1]);
            DataInputStream in = new DataInputStream(new FileInputStream(infile));
            int signlength = in.readInt();
            byte[] signature = new byte[signlength];
            in.read(signature, 0, signlength);

            int length = (int) infile.length() - signlength - 4;
            byte[] message = new byte[length];
            in.read(message, 0, length);
            in.close();

            verifyalg.update(message);
            if (!verifyalg.verify(signature)) System.out.print("not ");
            System.out.println("verified");
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   private static final int KEYSIZE = 512;
}
