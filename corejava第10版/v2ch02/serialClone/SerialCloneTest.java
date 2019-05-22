package serialClone;

/**
 * @version 1.21 13 Jul 2016
 * @author Cay Horstmann
 */

import java.io.*;
import java.util.*;
import java.time.*;

public class SerialCloneTest
{  
   public static void main(String[] args) throws CloneNotSupportedException
   {  
      Employee harry = new Employee("Harry Hacker", 35000, 1989, 10, 1);
      // clone harry
      Employee harry2 = (Employee) harry.clone();

      // mutate harry
      harry.raiseSalary(10);

      // now harry and the clone are different
      System.out.println(harry);
      System.out.println(harry2);
   }
}

/**
 * A class whose clone method uses serialization.
 */
class SerialCloneable implements Cloneable, Serializable
{  
   public Object clone() throws CloneNotSupportedException
   {
      try {
         // save the object to a byte array
         ByteArrayOutputStream bout = new ByteArrayOutputStream();
         try (ObjectOutputStream out = new ObjectOutputStream(bout))
         {
            out.writeObject(this);
         }

         // read a clone of the object from the byte array
         try (InputStream bin = new ByteArrayInputStream(bout.toByteArray()))
         {
            ObjectInputStream in = new ObjectInputStream(bin);
            return in.readObject();
         }
      }
      catch (IOException | ClassNotFoundException e)
      {  
         CloneNotSupportedException e2 = new CloneNotSupportedException();
         e2.initCause(e);
         throw e2;
      }
   }
}

/**
 * The familiar Employee class, redefined to extend the
 * SerialCloneable class. 
 */
class Employee extends SerialCloneable
{  
   private String name;
   private double salary;
   private LocalDate hireDay;

   public Employee(String n, double s, int year, int month, int day)
   {  
      name = n;
      salary = s;
      hireDay = LocalDate.of(year, month, day);
   }

   public String getName()
   {
      return name;
   }

   public double getSalary()
   {
      return salary;
   }

   public LocalDate getHireDay()
   {
      return hireDay;
   }

   /**
      Raises the salary of this employee.
      @byPercent the percentage of the raise
   */
   public void raiseSalary(double byPercent)
   {  
      double raise = salary * byPercent / 100;
      salary += raise;
   }

   public String toString()
   {  
      return getClass().getName()
         + "[name=" + name
         + ",salary=" + salary
         + ",hireDay=" + hireDay
         + "]";
   }
}
