package reflection;

import java.util.*;
import java.lang.reflect.*;
/**
 * This program uses reflection to print all features of a class.
 * @version 1.12 2021-06-15
 * @author Cay Horstmann
 */
public class ReflectionTest
{
   public static void main(String[] args)
         throws ReflectiveOperationException      
   {
      // read class name from command line args or user input
      String name;
      if (args.length > 0) name = args[0];
      else
      {
         var in = new Scanner(System.in);
         System.out.println("Enter class name (e.g. java.util.Date): ");
         name = in.next();
      }

      // print class modifiers, name, and superclass name (if != Object)
      Class cl = Class.forName(name);
      String modifiers = Modifier.toString(cl.getModifiers());
      if (modifiers.length() > 0) System.out.print(modifiers + " ");
      if (cl.isSealed())
         System.out.print("sealed ");
      if (cl.isEnum())
         System.out.print("enum " + name);
      else if (cl.isRecord())
         System.out.print("record " + name);
      else if (cl.isInterface())
         System.out.print("interface " + name);
      else
         System.out.print("class " + name);
      Class supercl = cl.getSuperclass();
      if (supercl != null && supercl != Object.class) System.out.print(" extends "
            + supercl.getName());

      printInterfaces(cl);
      printPermittedSubclasses(cl);
      
      System.out.print("\n{\n");
      printConstructors(cl);
      System.out.println();
      printMethods(cl);
      System.out.println();
      printFields(cl);
      System.out.println("}");
   }

   /**
    * Prints all constructors of a class
    * @param cl a class
    */
   public static void printConstructors(Class cl)
   {
      Constructor[] constructors = cl.getDeclaredConstructors();

      for (Constructor c : constructors)
      {
         String name = c.getName();
         System.out.print("   ");
         String modifiers = Modifier.toString(c.getModifiers());
         if (modifiers.length() > 0) System.out.print(modifiers + " ");         
         System.out.print(name + "(");

         // print parameter types
         Class[] paramTypes = c.getParameterTypes();
         for (int j = 0; j < paramTypes.length; j++)
         {
            if (j > 0) System.out.print(", ");
            System.out.print(paramTypes[j].getName());
         }
         System.out.println(");");
      }
   }

   /**
    * Prints all methods of a class
    * @param cl a class
    */
   public static void printMethods(Class cl)
   {
      Method[] methods = cl.getDeclaredMethods();

      for (Method m : methods)
      {
         Class retType = m.getReturnType();
         String name = m.getName();

         System.out.print("   ");
         // print modifiers, return type and method name
         String modifiers = Modifier.toString(m.getModifiers());
         if (modifiers.length() > 0) System.out.print(modifiers + " ");         
         System.out.print(retType.getName() + " " + name + "(");

         // print parameter types
         Class[] paramTypes = m.getParameterTypes();
         for (int j = 0; j < paramTypes.length; j++)
         {
            if (j > 0) System.out.print(", ");
            System.out.print(paramTypes[j].getName());
         }
         System.out.println(");");
      }
   }

   /**
    * Prints all fields of a class
    * @param cl a class
    */
   public static void printFields(Class cl)
   {
      Field[] fields = cl.getDeclaredFields();

      for (Field f : fields)
      {
         Class type = f.getType();
         String name = f.getName();
         System.out.print("   ");
         String modifiers = Modifier.toString(f.getModifiers());
         if (modifiers.length() > 0) System.out.print(modifiers + " ");         
         System.out.println(type.getName() + " " + name + ";");
      }
   }

   /**
    * Prints all permitted subtypes of a sealed class
    * @param cl a class
    */
   public static void printPermittedSubclasses(Class cl)
   {
      if (cl.isSealed())
      {
         Class<?>[] permittedSubclasses = cl.getPermittedSubclasses();
         for (int i = 0; i < permittedSubclasses.length; i++)
         {
            if (i == 0)
               System.out.print(" permits ");
            else
               System.out.print(", ");
            System.out.print(permittedSubclasses[i].getName());
         }         
      }               
   }

   /**
    * Prints all directly implemented interfaces of a class
    * @param cl a class
    */
   public static void printInterfaces(Class cl)
   {
      Class<?>[] interfaces = cl.getInterfaces();
      for (int i = 0; i < interfaces.length; i++)
      {
         if (i == 0)
            System.out.print(cl.isInterface() ? " extends " : " implements ");
         else
            System.out.print(", ");
         System.out.print(interfaces[i].getName());
      }         
   }      
}   
