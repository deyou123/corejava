package com.horstmann.places;

public class Country
{
   private String name;
   private double area;

   public Country(String name, double area)
   {
      this.name = name;
      this.area = area;
   }

   public String toString()
   {
      return "name=" + name + ",area=" + area;
   }
}
