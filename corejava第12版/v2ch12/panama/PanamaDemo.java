package panama;

import java.lang.reflect.*;
import java.lang.invoke.*;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.MemoryLayouts.*;

/*

javac --enable-preview --source 17 --add-modules jdk.incubator.foreign panama/PanamaDemo.java 

java --add-modules jdk.incubator.foreign --enable-native-access=ALL-UNNAMED panama.PanamaDemo

*/

public class PanamaDemo
{
   public static void main(String[] args) throws Throwable
   {
      CLinker linker = CLinker.getInstance();
      MethodHandle printf = linker.downcallHandle(
         CLinker.systemLookup().lookup("printf").get(),
         MethodType.methodType(int.class, MemoryAddress.class),
         FunctionDescriptor.of(CLinker.C_INT, CLinker.C_POINTER));      

      try (ResourceScope scope = ResourceScope.newConfinedScope())
      {
         var cString = CLinker.toCString("Hello, World!\n", scope);
         int result = (int) printf.invoke(cString.address());
         System.out.println("Printed %d characters.".formatted(result));
      }
   }
}

   

