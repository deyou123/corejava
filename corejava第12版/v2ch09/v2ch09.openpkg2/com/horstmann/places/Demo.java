package com.horstmann.places;

/*
 
javac -p jakarta.json.bind-api-2.0.0.jar \
    v2ch09.openpkg2/module-info.java \
    v2ch09.openpkg2/com/horstmann/places/Demo.java \
    v2ch09.openpkg2/com/horstmann/places/Country.java
 
java -p jakarta.json-api-2.0.1.jar:jakarta.json.bind-api-2.0.0.jar:jakarta.json-2.0.1-module.jar:yasson-2.0.3.jar:v2ch09.openpkg2 -m v2ch09.openpkg2/com.horstmann.places.Demo
 
*/

import jakarta.json.bind.*;
import jakarta.json.bind.config.*;

import java.lang.reflect.*;

public class Demo
{
   public static void main(String[] args)
   {
      Country belgium = new Country("Belgium", 30510);

      JsonbConfig config = new JsonbConfig()
         .withPropertyVisibilityStrategy(
            new PropertyVisibilityStrategy()
            {
               public boolean isVisible(Field field) { return true; }
               public boolean isVisible(Method method) { return false; }
            });
      Jsonb jsonb = JsonbBuilder.create(config);
      String json = jsonb.toJson(belgium);
      System.out.println(json);
   }
}
