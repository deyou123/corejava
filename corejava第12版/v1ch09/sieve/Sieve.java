package sieve;

import java.util.*;

/**
 * This program runs the Sieve of Erathostenes benchmark. It computes all primes 
 * up to 2,000,000.
 * @version 1.22 2021-06-17
 * @author Cay Horstmann
 */
public class Sieve
{
   public static void main(String[] s)
   {
      int n = 2000000;
      long start = System.currentTimeMillis();
      var bitSet = new BitSet(n + 1);
      int i;
      for (i = 2; i <= n; i++)
         bitSet.set(i);
      i = 2;
      while (i * i <= n)
      {
         if (bitSet.get(i))
         {
            int k = i * i;
            while (k <= n)
            {
               bitSet.clear(k);
               k += i;
            }
         }
         i++;
      }
      long end = System.currentTimeMillis();
      System.out.println(bitSet.cardinality() + " primes");
      System.out.println((end - start) + " milliseconds");
   }
}
