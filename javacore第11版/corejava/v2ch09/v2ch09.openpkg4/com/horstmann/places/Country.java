package com.horstmann.places;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Country {
   @XmlElement private String name;
   @XmlElement private double area;

   public Country() {}
   
   public Country(String name, double area) {
      this.name = name;
      this.area = area;
   }

   public String getName() {
      return name;
   }

   public double getArea() {
      return area;
   }
}
