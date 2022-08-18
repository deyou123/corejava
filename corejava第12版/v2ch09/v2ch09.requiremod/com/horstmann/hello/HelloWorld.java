package com.horstmann.hello;

/*

javac v2ch09.requiremod/module-info.java v2ch09.hellomod/com/horstmann/hello/HelloWorld.java 
java -p v2ch09.requiremod -m v2ch09.requiremod/com.horstmann.hello.HelloWorld

Remove the requires clause in the module descriptor and recompile to
see the failure

*/

import javax.swing.JOptionPane;

public class HelloWorld
{
   public static void main(String[] args)
   {
      JOptionPane.showMessageDialog(null, "Hello, Modular World!");
   }
}
