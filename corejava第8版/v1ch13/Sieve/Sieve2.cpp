/**
   @version 1.21 2004-08-03
   @author Cay Horstmann
*/

/** 
    This program runs the Sieve of Erathostenes benchmark,
    using a handwritten BitSet class instead of the library class.
    It computes all primes up to 2,000,000. 
*/

#include <iostream>
#include <ctime>

using namespace std;

template<int N>
class bitset
{
public:
   bitset() : bits(new char[(N - 1) / 8 + 1]) {}
   bool test(int n) { return (bits[n >> 3] & (1 << (n & 7))) != 0; }
   void set(int n) { bits[n >> 3] |= 1 << (n & 7); }
   void reset(int n) { bits[n >> 3] &= ~(1 << (n & 7)); }
private:
   char* bits;
};

int main()
{  
   const int N = 2000000;
   clock_t cstart = clock();

   bitset<N + 1> b;
   int count = 0;
   int i;
   for (i = 2; i <= N; i++)
      b.set(i);
   i = 2;
   while (i * i <= N)
   {  
      if (b.test(i))
      {  
         count++;
         int k = 2 * i;
         while (k <= N)
         {  
            b.reset(k);
            k += i;
         }
      }
      i++;
   }
   while (i <= N)
   {  
      if (b.test(i))
         count++;
      i++;
   }

   clock_t cend = clock();
   double millis = 1000.0
      * (cend - cstart) / CLOCKS_PER_SEC;

   cout << count << " primes\n"
      << millis << " milliseconds\n";

   return 0;
}
