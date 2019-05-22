import java.util.*;

/**
 * This program demonstrates the use of a map with key type String and value type Employee.
 * @version 1.10 2004-08-02
 * @author Cay Horstmann
 */
public class MapTest
{
   public static void main(String[] args)
   {
      Map<String, Employee> staff = new HashMap<String, Employee>();
      staff.put("144-25-5464", new Employee("Amy Lee"));
      staff.put("567-24-2546", new Employee("Harry Hacker"));
      staff.put("157-62-7935", new Employee("Gary Cooper"));
      staff.put("456-62-5527", new Employee("Francesca Cruz"));

      // print all entries

      System.out.println(staff);

      // remove an entry

      staff.remove("567-24-2546");

      // replace an entry

      staff.put("456-62-5527", new Employee("Francesca Miller"));

      // look up a value

      System.out.println(staff.get("157-62-7935"));

      // iterate through all entries

      for (Map.Entry<String, Employee> entry : staff.entrySet())
      {
         String key = entry.getKey();
         Employee value = entry.getValue();
         System.out.println("key=" + key + ", value=" + value);
      }
   }
}

/**
 * A minimalist employee class for testing purposes.
 */
class Employee
{
   /**
    * Constructs an employee with $0 salary.
    * @param n the employee name
    */
   public Employee(String n)
   {
      name = n;
      salary = 0;
   }

   public String toString()
   {
      return "[name=" + name + ", salary=" + salary + "]";
   }

   private String name;
   private double salary;
}
