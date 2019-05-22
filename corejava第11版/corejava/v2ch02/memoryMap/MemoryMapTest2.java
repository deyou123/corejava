package memoryMap;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.zip.*;

/**
 * This program computes the CRC checksum of a file in four ways. <br>
 * Usage: java memoryMap.MemoryMapTest filename
 * @version 1.02 2018-05-01
 * @author Cay Horstmann
 */
public class MemoryMapTest2
{
   public static int BLOCK_SIZE = 1024;
   
   public static long checksumInputStream(Path filename) throws IOException
   {
      try (InputStream in = Files.newInputStream(filename))
      {
         var crc = new CRC32();
   
         var bytes = new byte[BLOCK_SIZE];
         int n;
         while ((n = in.read(bytes)) != -1)
            crc.update(bytes, 0, n);
         return crc.getValue();
      }
   }

   public static long checksumBufferedInputStream(Path filename) 
         throws IOException
   {
      try (var in = new BufferedInputStream(Files.newInputStream(filename)))
      {
         var crc = new CRC32();
   
         var bytes = new byte[BLOCK_SIZE];
         int n;
         while ((n = in.read(bytes)) != -1)
            crc.update(bytes, 0, n);
         return crc.getValue();
      }
   }

   public static long checksumRandomAccessFile(Path filename) throws IOException
   {
      try (var file = new RandomAccessFile(filename.toFile(), "r"))
      {
         long length = file.length();
         var crc = new CRC32();
   
         var bytes = new byte[BLOCK_SIZE];
         for (long p = 0; p < length; p += BLOCK_SIZE)
         {
            file.seek(p);
            int n = file.read(bytes);
            crc.update(bytes, 0, n);
         }
         return crc.getValue();
      }
   }

   public static long checksumMappedFile(Path filename) throws IOException
   {
      try (FileChannel channel = FileChannel.open(filename))
      {
         var crc = new CRC32();
         int length = (int) channel.size();
         MappedByteBuffer buffer = channel.map(
               FileChannel.MapMode.READ_ONLY, 0, length);
   
         var bytes = new byte[BLOCK_SIZE];
         for (int p = 0; p < length; p += BLOCK_SIZE)
         {
            int n = Math.min(BLOCK_SIZE, length - p);
            buffer.get(bytes, 0, n);
            crc.update(bytes, 0, n);
         }
         return crc.getValue();
      }
   }

   public static void main(String[] args) throws IOException
   {
      System.out.println("Input Stream:");
      long start = System.currentTimeMillis();
      Path filename = Paths.get(args[0]);
      long crcValue = checksumInputStream(filename);
      long end = System.currentTimeMillis();
      System.out.println(Long.toHexString(crcValue));
      System.out.println((end - start) + " milliseconds");

      System.out.println("Buffered Input Stream:");
      start = System.currentTimeMillis();
      crcValue = checksumBufferedInputStream(filename);
      end = System.currentTimeMillis();
      System.out.println(Long.toHexString(crcValue));
      System.out.println((end - start) + " milliseconds");

      System.out.println("Random Access File:");
      start = System.currentTimeMillis();
      crcValue = checksumRandomAccessFile(filename);
      end = System.currentTimeMillis();
      System.out.println(Long.toHexString(crcValue));
      System.out.println((end - start) + " milliseconds");

      System.out.println("Mapped File:");
      start = System.currentTimeMillis();
      crcValue = checksumMappedFile(filename);
      end = System.currentTimeMillis();
      System.out.println(Long.toHexString(crcValue));
      System.out.println((end - start) + " milliseconds");
   }
}
