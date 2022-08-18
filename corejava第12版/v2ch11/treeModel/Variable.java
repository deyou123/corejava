package treeModel;

import java.lang.reflect.*;
import java.util.*;

/**
 * A variable with a type, name, and value.
 */
public class Variable
{
   private Class<?> type;
   private String name;
   private Object value;
   private ArrayList<Field> fields;

   /**
    * Construct a variable.
    * @param aType the type
    * @param aName the name
    * @param aValue the value
    */
   public Variable(Class<?> aType, String aName, Object aValue)
   {
      type = aType;
      name = aName;
      value = aValue;
      fields = new ArrayList<>();

      // find all fields if we have a class type except we don't expand strings and 
      // null values

      if (!type.isPrimitive() && !type.isArray() && !type.equals(String.class) 
            && value != null)
      {
         // get fields from the class and all superclasses
         for (Class<?> c = value.getClass(); c != null; c = c.getSuperclass())
         {
            Field[] fs = c.getDeclaredFields();
            AccessibleObject.setAccessible(fs, true);

            // get all nonstatic fields
            for (Field f : fs)
               if ((f.getModifiers() & Modifier.STATIC) == 0) fields.add(f);
         }
      }
   }

   /**
    * Gets the value of this variable.
    * @return the value
    */
   public Object getValue()
   {
      return value;
   }

   /**
    * Gets all nonstatic fields of this variable.
    * @return an array list of variables describing the fields
    */
   public ArrayList<Field> getFields()
   {
      return fields;
   }

   public String toString()
   {
      String r = type + " " + name;
      if (type.isPrimitive()) r += "=" + value;
      else if (type.equals(String.class)) r += "=" + value;
      else if (value == null) r += "=null";
      return r;
   }
}
