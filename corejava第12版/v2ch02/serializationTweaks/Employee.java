package serializationTweaks;

import java.io.*;
import java.time.*;

public class Employee implements Serializable, ObjectInputValidation
{
   private String name;
   private double salary;
   private LocalDate hireDay;
   private static final String serialVersionUID = "Fred";

   public Employee() {}

   public Employee(String n, double s, int year, int month, int day)
   {
      if (s < 0)
         throw new IllegalArgumentException("s < 0");
      name = n;
      salary = s;
      hireDay = LocalDate.of(year, month, day);
   }

   public void validateObject() throws InvalidObjectException
   {
      System.out.println("validateObject");
      if (salary < 0)
         throw new InvalidObjectException("salary < 0");
   }

   @Serial private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      System.out.println("readObject");
      in.registerValidation(this, 0);
      in.defaultReadObject();
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
      @param byPercent the percentage of the raise
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
