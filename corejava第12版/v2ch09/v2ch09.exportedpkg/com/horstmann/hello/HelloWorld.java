package com.horstmann.hello;

/*
 
javac com.horstmann.greet/module-info.java \
   com.horstmann.greet/com/horstmann/greet/Greeter.java \
   com.horstmann.greet/com/horstmann/greet/internal/GreeterImpl.java〉 

javac -p com.horstmann.greet v2ch09.exportedpkg/module-info.java \
   v2ch09.exportedpkg/com/horstmann/hello/HelloWorld.java〉 

java -p v2ch09.exportedpkg:com.horstmann.greet \
   -m v2ch09.exportedpkg/com.horstmann.hello.HelloWorld 
 
*/

import com.horstmann.greet.Greeter;

public class HelloWorld
{
   public static void main(String[] args)
   {
      Greeter greeter = Greeter.newInstance();
      System.out.println(greeter.greet("Modular World"));
   }
}
