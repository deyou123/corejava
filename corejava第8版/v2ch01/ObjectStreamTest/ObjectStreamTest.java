import java.io.*;
import java.util.*;

/**
 * @version 1.10 17 Aug 1998
 * @author Cay Horstmann
 */
class ObjectStreamTest
{
   public static void main(String[] args)
   {
      Employee harry = new Employee("Harry Hacker", 50000, 1989, 10, 1);
      Manager carl = new Manager("Carl Cracker", 80000, 1987, 12, 15);
      carl.setSecretary(harry);
      Manager tony = new Manager("Tony Tester", 40000, 1990, 3, 15);
      tony.setSecretary(harry);

      Employee[] staff = new Employee[3];

      staff[0] = carl;
      staff[1] = harry;
      staff[2] = tony;

      try
      {
         // save all employee records to the file employee.dat
         ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employee.dat"));
         out.writeObject(staff);
         out.close();

         // retrieve all records into a new array
         ObjectInputStream in = new ObjectInputStream(new FileInputStream("employee.dat"));
         Employee[] newStaff = (Employee[]) in.readObject();
         in.close();

         // raise secretary's salary
         newStaff[1].raiseSalary(10);

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
   public Employee()
   {
   }

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
      return getClass().getName() + "[name=" + name + ",salary=" + salary + ",hireDay=" + hireDay
            + "]";
   }

   private String name;
   private double salary;
   private Date hireDay;
}

class Manager extends Employee
{
   /**
    * Constructs a Manager without a secretary
    * @param n the employee's name
    * @param s the salary
    * @param year the hire year
    * @param month the hire month
    * @param day the hire day
    */
   public Manager(String n, double s, int year, int month, int day)
   {
      super(n, s, year, month, day);
      secretary = null;
   }

   /**
    * Assigns a secretary to the manager.
    * @param s the secretary
    */
   public void setSecretary(Employee s)
   {
      secretary = s;
   }

   public String toString()
   {
      return super.toString() + "[secretary=" + secretary + "]";
   }

   private Employee secretary;
}
