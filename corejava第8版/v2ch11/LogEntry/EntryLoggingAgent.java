import java.lang.instrument.*;
import java.io.*;
import java.security.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

/**
 * @version 1.00 2004-08-17
 * @author Cay Horstmann
 */
public class EntryLoggingAgent
{
   public static void premain(final String arg, Instrumentation instr)
   {
      instr.addTransformer(new ClassFileTransformer()
         {
            public byte[] transform(ClassLoader loader, String className, Class<?> cl,
                  ProtectionDomain pd, byte[] data)
            {
               if (!className.equals(arg)) return null;
               try
               {
                  ClassParser parser = new ClassParser(new ByteArrayInputStream(data), className
                        + ".java");
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
