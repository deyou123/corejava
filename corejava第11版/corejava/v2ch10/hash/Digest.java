package hash;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

/**
 * This program computes the message digest of a file.
 * @version 1.21 2018-04-10
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
      var in = new Scanner(System.in);
      String filename;
      if (args.length >= 1)
         filename = args[0];
      else
      {
         System.out.print("File name: ");
         filename = in.nextLine();
      }
      String algname;
      if (args.length >= 2)
         algname = args[1];                     
      else 
      {
         System.out.println("Select one of the following algorithms: ");
         for (Provider p : Security.getProviders()) 
            for (Provider.Service s : p.getServices()) 
               if (s.getType().equals("MessageDigest")) 
                  System.out.println(s.getAlgorithm());
         System.out.print("Algorithm: ");
         algname = in.nextLine();
      }
      MessageDigest alg = MessageDigest.getInstance(algname);
      byte[] input = Files.readAllBytes(Paths.get(filename));
      byte[] hash = alg.digest(input);
      for (int i = 0; i < hash.length; i++)
         System.out.printf("%02X ", hash[i] & 0xFF);
      System.out.println();
   }
}
