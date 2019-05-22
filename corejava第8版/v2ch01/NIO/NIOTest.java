import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.zip.*;

/**
 * This program computes the CRC checksum of a file. <br>
 * Usage: java NIOTest filename
 * @version 1.01 2004-05-11
 * @author Cay Horstmann
 */
public class NIOTest
{
   public static long checksumInputStream(String filename) throws IOException
   {
      InputStream in = new FileInputStream(filename);
      CRC32 crc = new CRC32();

      int c;
      while ((c = in.read()) != -1)
         crc.update(c);
      return crc.getValue();
   }

   public static long checksumBufferedInputStream(String filename) throws IOException
   {
      InputStream in = new BufferedInputStream(new FileInputStream(filename));
      CRC32 crc = new CRC32();

      int c;
      while ((c = in.read()) != -1)
         crc.update(c);
      return crc.getValue();
   }

   public static long checksumRandomAccessFile(String filename) throws IOException
   {
      RandomAccessFile file = new RandomAccessFile(filename, "r");
      long length = file.length();
      CRC32 crc = new CRC32();

      for (long p = 0; p < length; p++)
      {
         file.seek(p);
         int c = file.readByte();
         crc.update(c);
      }
      return crc.getValue();
   }

   public static long checksumMappedFile(String filename) throws IOException
   {
      FileInputStream in = new FileInputStream(filename);
      FileChannel channel = in.getChannel();

      CRC32 crc = new CRC32();
      int length = (int) channel.size();
      MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);

      for (int p = 0; p < length; p++)
      {
         int c = buffer.get(p);
         crc.update(c);
      }
      return crc.getValue();
   }

   public static void main(String[] args) throws IOException
   {
      System.out.println("Input Stream:");
      long start = System.currentTimeMillis();
      long crcValue = checksumInputStream(args[0]);
      long end = System.currentTimeMillis();
      System.out.println(Long.toHexString(crcValue));
      System.out.println((end - start) + " milliseconds");

      System.out.println("Buffered Input Stream:");
      start = System.currentTimeMillis();
      crcValue = checksumBufferedInputStream(args[0]);
      end = System.currentTimeMillis();
      System.out.println(Long.toHexString(crcValue));
      System.out.println((end - start) + " milliseconds");

      System.out.println("Random Access File:");
      start = System.currentTimeMillis();
      crcValue = checksumRandomAccessFile(args[0]);
      end = System.currentTimeMillis();
      System.out.println(Long.toHexString(crcValue));
      System.out.println((end - start) + " milliseconds");

      System.out.println("Mapped File:");
      start = System.currentTimeMillis();
      crcValue = checksumMappedFile(args[0]);
      end = System.currentTimeMillis();
      System.out.println(Long.toHexString(crcValue));
      System.out.println((end - start) + " milliseconds");
   }
}
