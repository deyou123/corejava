package com.horstmann.places;

import java.io.*;
import org.apache.commons.csv.*;

public class CSVDemo 
{
   public static void main(String[] args) throws IOException 
   {
      Reader in = new FileReader("countries.csv");
      Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(';').withHeader().parse(in);
      for (CSVRecord record : records) 
      {
         String name = record.get("Name");
         double area = Double.parseDouble(record.get("Area"));
         System.out.println(name + " has area " + area);
      }
   }
}
