/**
   @version 1.11 2004-05-11
   @author Cay Horstmann
*/


import java.io.*;
import java.util.*;

class ObjectFileTest
{  
   public static void main(String[] args)
   {  
      Manager boss = new Manager("Carl Cracker", 80000, 1987, 12, 15);
      boss.setBonus(5000);

      Employee[] staff = new Employee[3];

      staff[0] = boss;
      staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
      staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);
      
      try
      {  
         // save all employee records to the file employee.dat
         ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employee.dat"));
         out.writeObject(staff);
         out.close();

         // retrieve all records into a new array
         ObjectInputStream in =  new ObjectInputStream(new FileInputStream("employee.dat"));
         Employee[] newStaff = (Employee[]) in.readObject();
         in.close();

         // print the newly read employee records
         for (Employee e : newStaff)
            System.out.println(e);
      }
      catch (Exception e)
      {  
         e.printStackTrace(); 
      }
   }
}

class Employee implements Serializable
{
   public Employee() {}

   public Employee(String n, double s, int year, int month, int day)
   {  
      name = n;
      salary = s;
      GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day);
      hireDay = calendar.getTime();
   }

   public String getName()
   {  
      return name;
   }

   public double getSalary()
   {  
      return salary;
   }

   public Date getHireDay()
   {  
      return hireDay;
   }

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

   private String name;
   private double salary;
   private Date hireDay;
}

class Manager extends Employee
{  
   /**
      @param n the employee's name
      @param s the salary
      @param year the hire year
      @param month the hire month
      @param day the hire day
   */
   public Manager(String n, double s, int year, int month, int day)
   {  
      super(n, s, year, month, day);
      bonus = 0;
   }

   public double getSalary()
   { 
      double baseSalary = super.getSalary();
      return baseSalary + bonus;
   }

   public void setBonus(double b)
   {  
      bonus = b;
   }

   public String toString()
   {
      return super.toString()
        + "[bonus=" + bonus
        + "]";
   }

   private double bonus;
}
