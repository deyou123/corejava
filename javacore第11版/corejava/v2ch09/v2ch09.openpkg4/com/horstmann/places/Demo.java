package com.horstmann.places;

/*

javac -p jaxb-api.jar:javax.activation-1.2.0.jar \
    v2ch09.openpkg4/module-info.java \
    v2ch09.openpkg4/com/horstmann/places/Demo.java \
    v2ch09.openpkg4/com/horstmann/places/Country.java
 
java -p jaxb-api.jar:javax.activation-1.2.0.jar:v2ch09.openpkg4 \
    -m v2ch09.openpkg4/com.horstmann.places.Demo 

 */

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Demo {
   public static void main(String[] args) throws JAXBException {
      Country belgium = new Country("Belgium", 30510);
      JAXBContext context = JAXBContext.newInstance(Country.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      m.marshal(belgium, System.out);
   }
}
