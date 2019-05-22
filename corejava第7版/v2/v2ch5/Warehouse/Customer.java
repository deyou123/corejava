/**
   @version 1.10 1999-08-23
   @author Cay Horstmann
*/

import java.io.*;

/**
   Description of a customer. Note that customer objects are not 
   remote--the class does not implement a remote interface.
*/
public class Customer implements Serializable
{  
   /**
      Constructs a customer.
      @param theAge the customer's age
      @param theSex the customer's sex (MALE or FEMALE)
      @param theHobbies the customer's hobbies
   */
   public Customer(int theAge, int theSex, String[] theHobbies)
   {  
      age = theAge;
      sex = theSex;
      hobbies = theHobbies;
   }

   /**
      Gets the customer's age.
      @return the age
   */
   public int getAge() { return age; }

   /**
      Gets the customer's sex
      @return MALE or FEMALE
   */
   public int getSex() { return sex; }

   /**
      Tests whether this customer has a particular hobby.
      @param aHobby the hobby to test
      @return true if this customer has the hobby
   */
   public boolean hasHobby(String aHobby)
   {  
      if (aHobby == "") return true;
      for (int i = 0; i < hobbies.length; i++)
         if (hobbies[i].equals(aHobby)) return true;

      return false;
   }

   /**
      Resets this customer record to default values.
   */
   public void reset()
   {  
      age = 0;
      sex = 0;
      hobbies = null;
   }

   public String toString()
   {  
      String result = "Age: " + age + ", Sex: ";
      if (sex == Product.MALE) result += "Male";
      else if (sex == Product.FEMALE) result += "Female";
      else result += "Male or Female";
      result += ", Hobbies:";
      for (int i = 0; i < hobbies.length; i++)
         result += " " + hobbies[i];
      return result;
   }

   private int age;
   private int sex;
   private String[] hobbies;
}
