package bytecodeAnnotations;

import java.io.*;
import org.apache.bcel.*;
import org.apache.bcel.classfile.*;
import org.apache.bcel.generic.*;

/**
 * Adds "entering" logs to all methods of a class that have the LogEntry annotation.
 * @version 1.10 2007-10-27
 * @author Cay Horstmann
 */
public class EntryLogger
{
   private ClassGen cg;
   private ConstantPoolGen cpg;

   /**
    * Adds entry logging code to the given class.
    * @param args the name of the class file to patch
    */
   public static void main(String[] args)
   {
      try
      {
         if (args.length == 0) 
            System.out.println("USAGE: java bytecodeAnnotations.EntryLogger classname");
         else
         {
            JavaClass jc = Repository.lookupClass(args[0]);
            ClassGen cg = new ClassGen(jc);
            EntryLogger el = new EntryLogger(cg);
            el.convert();
            String f = Repository.lookupClassFile(cg.getClassName()).getPath();
            System.out.println("Dumping " + f);
            cg.getJavaClass().dump(f);
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Constructs an EntryLogger that inserts logging into annotated methods of a given class.
    * @param cg the class
    */
   public EntryLogger(ClassGen cg)
   {
      this.cg = cg;
      cpg = cg.getConstantPool();
   }

   /**
    * converts the class by inserting the logging calls.
    */
   public void convert() throws IOException
   {
      for (Method m : cg.getMethods())
      {
         AnnotationEntry[] annotations = m.getAnnotationEntries();
         for (AnnotationEntry a : annotations)
         {
            if (a.getAnnotationType().equals("LbytecodeAnnotations/LogEntry;"))
            {
               for (ElementValuePair p : a.getElementValuePairs())
               {
                  if (p.getNameString().equals("logger"))
                  {
                     String loggerName = p.getValue().stringifyValue();
                     cg.replaceMethod(m, insertLogEntry(m, loggerName));
                  }
               }
            }
         }
      }
   }

   /**
    * Adds an "entering" call to the beginning of a method.
    * @param m the method
    * @param loggerName the name of the logger to call
    */
   private Method insertLogEntry(Method m, String loggerName)
   {
      MethodGen mg = new MethodGen(m, cg.getClassName(), cpg);
      String className = cg.getClassName();
      String methodName = mg.getMethod().getName();
      System.out.printf("Adding logging instructions to %s.%s%n", className, methodName);

      int getLoggerIndex = cpg.addMethodref("java.util.logging.Logger", "getLogger",
            "(Ljava/lang/String;)Ljava/util/logging/Logger;");
      int enteringIndex = cpg.addMethodref("java.util.logging.Logger", "entering",
            "(Ljava/lang/String;Ljava/lang/String;)V");

      InstructionList il = mg.getInstructionList();
      InstructionList patch = new InstructionList();
      patch.append(new PUSH(cpg, loggerName));
      patch.append(new INVOKESTATIC(getLoggerIndex));
      patch.append(new PUSH(cpg, className));
      patch.append(new PUSH(cpg, methodName));
      patch.append(new INVOKEVIRTUAL(enteringIndex));
      InstructionHandle[] ihs = il.getInstructionHandles();
      il.insert(ihs[0], patch);

      mg.setMaxStack();
      return mg.getMethod();
   }
}
