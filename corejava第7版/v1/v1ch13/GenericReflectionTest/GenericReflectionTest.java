import java.lang.reflect.*;
import java.util.*;

public class GenericReflectionTest
{
   public static void main(String[] args)
   {
      // read class name from command line args or user input
      String name;
      if (args.length > 0) 
         name = args[0];
      else 
      {
         Scanner in = new Scanner(System.in);
         System.out.println("Enter class name (e.g. java.util.Date): ");
         name = in.next();
      }

      try
      {  
         // print generic info for class and public methods
         Class cl = Class.forName(name);
         printClass(cl);
         for (Method m : cl.getDeclaredMethods())
            printMethod(m);
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }
   }

   public static void printClass(Class cl)
   {
      System.out.print(cl);
      printTypes(cl.getTypeParameters(), "<", ", ", ">");
      Type sc = cl.getGenericSuperclass(); 
      if (sc != null) 
      {
         System.out.print(" extends ");
         printType(sc);
      }
      printTypes(cl.getGenericInterfaces(), " implements ", ", ", "");
      System.out.println();
   }

   public static void printMethod(Method m)
   {
      String name = m.getName();
      System.out.print(Modifier.toString(m.getModifiers()));
      System.out.print(" ");
      printTypes(m.getTypeParameters(), "<", ", ", "> ");

      printType(m.getGenericReturnType());
      System.out.print(" ");
      System.out.print(name);
      System.out.print("(");
      printTypes(m.getGenericParameterTypes(), "", ", ", "");
      System.out.println(")");
   }
 
   public static void printTypes(Type[] types, String pre, String sep, String suf)
   {
      if (types.length > 0) System.out.print(pre);
      for (int i = 0; i < types.length; i++)
      {
         if (i > 0) System.out.print(sep);
         printType(types[i]);
      }
      if (types.length > 0) System.out.print(suf);
   }

   public static void printType(Type type)
   {
      if (type instanceof Class) 
      {
         Class t = (Class) type;
         System.out.print(t.getName());
      }
      else if (type instanceof TypeVariable)
      {
         TypeVariable t = (TypeVariable) type;
         System.out.print(t.getName());
         printTypes(t.getBounds(), " extends ", " & ", "");
      }
      else if (type instanceof WildcardType)
      {
         WildcardType t = (WildcardType) type;
         System.out.print("?");
         printTypes(t.getLowerBounds(), " extends ", " & ", "");
         printTypes(t.getUpperBounds(), " super ", " & ", "");
      }
      else if (type instanceof ParameterizedType)
      {
         ParameterizedType t = (ParameterizedType) type;
         Type owner = t.getOwnerType();
         if (owner != null) { printType(owner); System.out.print("."); }
         printType(t.getRawType());
         printTypes(t.getActualTypeArguments(), "<", ", ", ">");         
      }
      else if (type instanceof GenericArrayType)
      {
         GenericArrayType t = (GenericArrayType) type;
         System.out.print("");
         printType(t.getGenericComponentType());
         System.out.print("[]");
      }
         
   }
}
