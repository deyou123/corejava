package com.horstmann.hello;

/*

javac v2ch09.hellomod/module-info.java v2ch09.hellomod/com/horstmann/hello/HelloWorld.java 
java --module-path v2ch09.hellomod --module v2ch09.hellomod/com.horstmann.hello.HelloWorld

*/

public class HelloWorld
{
   public static void main(String[] args)
   {
      System.out.println("Hello, Modular World!");
   }
}
