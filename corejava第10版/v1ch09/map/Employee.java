package map;

/**
 * A minimalist employee class for testing purposes.
 */
public class Employee
{
   private String name;
   private double salary;

   /**
    * Constructs an employee with $0 salary.
    * @param n the employee name
    */
   public Employee(String name)
   {
      this.name = name;
      salary = 0;
   }

   public String toString()
   {
      return "[name=" + name + ", salary=" + salary + "]";
   }
}
