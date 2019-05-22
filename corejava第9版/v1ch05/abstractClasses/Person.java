package abstractClasses;

public abstract class Person
{
   public abstract String getDescription();
   private String name;

   public Person(String n)
   {
      name = n;
   }

   public String getName()
   {
      return name;
   }
}
