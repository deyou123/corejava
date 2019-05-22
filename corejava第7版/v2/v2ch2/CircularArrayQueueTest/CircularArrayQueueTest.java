import java.util.*;

public class CircularArrayQueueTest
{
   public static void main(String[] args)
   {
      Queue<String> q = new CircularArrayQueue<String>(5);
      q.add("Amy");
      q.add("Bob");
      q.add("Carl");
      q.add("Deedee");
      q.add("Emile");
      q.remove();
      q.add("Fifi");
      q.remove();
      for (String s : q) System.out.println(s);
   }
}

/** 
    A first-in, first-out bounded collection. 
*/ 
class CircularArrayQueue<E> extends AbstractQueue<E>
{ 
   /** 
       Constructs an empty queue. 
       @param capacity the maximum capacity of the queue 
   */ 
   public CircularArrayQueue(int capacity) 
   { 
      elements = (E[]) new Object[capacity]; 
      count = 0; 
      head = 0; 
      tail = 0; 
   } 

   public boolean offer(E newElement) 
   { 
      assert newElement != null;
      if (count < elements.length) 
      {
         elements[tail] = newElement; 
         tail = (tail + 1) % elements.length; 
         count++;
         modcount++;
         return true;
      }
      else 
         return false;
   } 

   public E poll() 
   { 
      if (count == 0) return null;
      E r = elements[head]; 
      head = (head + 1) % elements.length; 
      count--; 
      modcount++;
      return r; 
   } 

   public E peek() 
   { 
      if (count == 0) return null;
      return elements[head]; 
   } 

   public int size() 
   { 
      return count; 
   } 

   public Iterator<E> iterator()
   {
      return new QueueIterator();
         
   }

   private class QueueIterator implements Iterator<E>
   {
      public QueueIterator()
      {
         modcountAtConstruction = modcount;
      }

      public E next() 
      { 
         if (!hasNext()) throw new NoSuchElementException();
         E r = elements[(head + offset) % elements.length]; 
         offset++;
         return r;
      }

      public boolean hasNext() 
      { 
         if (modcount != modcountAtConstruction) 
            throw new ConcurrentModificationException();
         return offset < count;
      }

      public void remove() 
      { 
         throw new UnsupportedOperationException(); 
      }

      private int offset;
      private int modcountAtConstruction;
   }

   private E[] elements; 
   private int head; 
   private int tail; 
   private int count; 
   private int modcount;
}
