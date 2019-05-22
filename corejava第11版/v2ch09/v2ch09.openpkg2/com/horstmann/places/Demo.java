package com.horstmann.places;

/*
 
javac -p javax.json.bind-api-1.0.jar \
    v2ch09.openpkg2/module-info.java \
    v2ch09.openpkg2/com/horstmann/places/Demo.java \
    v2ch09.openpkg2/com/horstmann/places/Country.java
 
java -p javax.json-api-1.1.2.jar:javax.json-1.2-SNAPSHOT.jar:javax.json.bind-api-1.0.jar:yasson.jar:v2ch09.openpkg2 \
    -m v2ch09.openpkg2/com.horstmann.places.Demo
 
*/

import javax.json.bind.*;
import javax.json.bind.config.*;

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
