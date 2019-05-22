/**
   @version 1.0 2004-08-03
   @author Cay Horstmann
*/

/** 
    This program runs the Sieve of Erathostenes benchmark,
    using a handwritten BitSet class instead of the library class.
    It computes all primes up to 2,000,000. 
*/
public class Sieve2
{  
   public static void main(String[] s)
   {  
      int n = 2000000;
      long start = System.currentTimeMillis();
      BitSet b = new BitSet(n + 1);
      int count = 0;
      int i;
      for (i = 2; i <= n; i++)
         b.set(i);
      i = 2;
      while (i * i <= n)
      {  
         if (b.get(i))
         {  
            count++;
            int k = 2 * i;
            while (k <= n)
            {  
               b.clear(k);
               k += i;
            }
         }
         i++;
      }
      while (i <= n)
      {  
         if (b.get(i))
            count++;
         i++;
      }
      long end =  System.currentTimeMillis();
      System.out.println(count + " primes");
      System.out.println((end - start) + " milliseconds");
   }
}

class BitSet
{
   public BitSet(int N) { bits = new char[(N - 1) / 8 + 1]; }
   public boolean get(int n) { return (bits[n >> 3] & (1 << (n & 7))) != 0; }   
   public void set(int n) { bits[n >> 3] |= 1 << (n & 7); }
   public void clear(int n) { bits[n >> 3] &= ~(1 << (n & 7)); }

   private char[] bits;
};


