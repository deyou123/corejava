package hash;

import java.io.*;
import java.nio.file.*;
import java.security.*;

/**
 * This program computes the message digest of a file.
 * @version 1.20 2012-06-16
 * @author Cay Horstmann
 */
public class Digest
{
   /** 
    * @param args args[0] is the filename, args[1] is optionally the algorithm 
    * (SHA-1, SHA-256, or MD5)
    */
   public static void main(String[] args) throws IOException, GeneralSecurityException
   {
      String algname = args.length >= 2 ? args[1] : "SHA-1";                     
      MessageDigest alg = MessageDigest.getInstance(algname);
      byte[] input = Files.readAllBytes(Paths.get(args[0]));
      byte[] hash = alg.digest(input);
      String d = "";
      for (int i = 0; i < hash.length; i++)
      {
         int v = hash[i] & 0xFF;
         if (v < 16) d += "0";
         d += Integer.toString(v, 16).toUpperCase() + " ";
      }
      System.out.println(d);
   }
}
