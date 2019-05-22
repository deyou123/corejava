package com.horstmann.places;

/*
 
javac -p gson-2.8.4-SNAPSHOT.jar \
    v2ch09.openpkg3/module-info.java \
    v2ch09.openpkg3/com/horstmann/places/Demo.java \
    v2ch09.openpkg3/com/horstmann/places/Country.java
 
java -p gson-2.8.4-SNAPSHOT.jar:v2ch09.openpkg3 \
    -m v2ch09.openpkg3/com.horstmann.places.Demo
 
*/

import com.google.gson.*;

public class Demo
{
   public static void main(String[] args)
   {
      try {
      System.out.println("Unsafe=" + Class.forName("sun.misc.Unsafe"));
      } catch (Exception e) { e.printStackTrace(); }
      Country belgium = new Country("Belgium", 30510);
      Gson gson = new Gson();
      String json = gson.toJson(belgium);
      System.out.println(json);
   }
}
