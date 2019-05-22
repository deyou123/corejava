import java.lang.instrument.*;
import java.io.*;
import java.security.*;

import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

public class EntryLoggingAgent
{
   public static void premain(final String arg, Instrumentation instr)
   {
      System.out.println(instr);
      instr.addTransformer(new
         ClassFileTransformer()
         {
            public byte[] transform(ClassLoader loader, String className, Class cl,
               ProtectionDomain pd, byte[] data)
            {
               if (!className.equals(arg)) return null;
               try
               {
                  Attribute.addAttributeReader("RuntimeVisibleAnnotations", 
                     new AnnotationsAttributeReader());
                  ClassParser parser = new ClassParser(
                     new ByteArrayInputStream(data), className + ".java");
                  JavaClass jc = parser.parse();
                  ClassGen cg = new ClassGen(jc);
                  EntryLogger el = new EntryLogger(cg);
                  el.convert();
                  return cg.getJavaClass().getBytes();                     
               }
               catch (Exception e)
               {
                  e.printStackTrace();
                  return null;
               }
            }
         });
   }
}
