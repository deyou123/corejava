import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import sun.security.x509.X509CertInfo;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X500Name;
import sun.security.x509.CertificateIssuerName;

/**
 * This program signs a certificate, using the private key of another certificate in a keystore.
 * @version 1.01 2007-10-07
 * @author Cay Horstmann
 */
public class CertificateSigner
{
   public static void main(String[] args)
   {
      String ksname = null; // the keystore name
      String alias = null; // the private key alias
      String inname = null; // the input file name
      String outname = null; // the output file name
      for (int i = 0; i < args.length; i += 2)
      {
         if (args[i].equals("-keystore")) ksname = args[i + 1];
         else if (args[i].equals("-alias")) alias = args[i + 1];
         else if (args[i].equals("-infile")) inname = args[i + 1];
         else if (args[i].equals("-outfile")) outname = args[i + 1];
         else usage();
      }

      if (ksname == null || alias == null || inname == null || outname == null) usage();

      try
      {
         Console console = System.console();
         if (console == null) error("No console");
         char[] password = console.readPassword("Keystore password: ");
         KeyStore store = KeyStore.getInstance("JKS", "SUN");
         InputStream in = new FileInputStream(ksname);
         store.load(in, password);
         Arrays.fill(password, ' ');
         in.close();

         char[] keyPassword = console.readPassword("Key password for %s: ", alias);
         PrivateKey issuerPrivateKey = (PrivateKey) store.getKey(alias, keyPassword);
         Arrays.fill(keyPassword, ' ');

         if (issuerPrivateKey == null) error("No such private key");

         in = new FileInputStream(inname);

         CertificateFactory factory = CertificateFactory.getInstance("X.509");

         X509Certificate inCert = (X509Certificate) factory.generateCertificate(in);
         in.close();
         byte[] inCertBytes = inCert.getTBSCertificate();

         X509Certificate issuerCert = (X509Certificate) store.getCertificate(alias);
         Principal issuer = issuerCert.getSubjectDN();
         String issuerSigAlg = issuerCert.getSigAlgName();

         FileOutputStream out = new FileOutputStream(outname);

         X509CertInfo info = new X509CertInfo(inCertBytes);
         info.set(X509CertInfo.ISSUER, new CertificateIssuerName((X500Name) issuer));

         X509CertImpl outCert = new X509CertImpl(info);
         outCert.sign(issuerPrivateKey, issuerSigAlg);
         outCert.derEncode(out);

         out.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Prints an error message and exits.
    * @param message the error message
    */
   public static void error(String message)
   {
      System.out.println(message);
      System.exit(1);
   }

   /**
    * Prints a usage message and exits.
    */
   public static void usage()
   {
      System.out.println("Usage: java CertificateSigner"
            + " -keystore keyStore -alias issuerKeyAlias"
            + " -infile inputFile -outfile outputFile");
      System.exit(1);
   }
}
