package serializationTweaks;

public class Manager extends Employee
{
   private Employee secretary;

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
}
