package serial;

import java.io.*;

/**
 * @version 1.12 2021-09-10
 * @author Cay Horstmann
 */
class ObjectStreamTest
{
   public static void main(String[] args) throws IOException, ClassNotFoundException
   {
      var harry = new Employee("Harry Hacker", 50000, 1989, 10, 1);
      var carl = new Manager("Carl Cracker", 80000, 1987, 12, 15);
      carl.setSecretary(harry);
      var tony = new Manager("Tony Tester", 40000, 1990, 3, 15);
      tony.setSecretary(harry);

      var staff = new Employee[3];

      staff[0] = carl;
      staff[1] = harry;
      staff[2] = tony;

      // save all employee records to the file employee.ser         
      try (var out = new ObjectOutputStream(new FileOutputStream("employee.ser"))) 
      {
         out.writeObject(staff);
      }

      try (var in = new ObjectInputStream(new FileInputStream("employee.ser")))
      {
         // retrieve all records into a new array
         
         var newStaff = (Employee[]) in.readObject();

         // raise secretary's salary
         newStaff[1].raiseSalary(10);

         // print the newly read employee records
         for (Employee e : newStaff)
            System.out.println(e);
      }
   }
}
