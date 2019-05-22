import java.lang.annotation.*;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.io.*;
import java.util.*;

import org.apache.bcel.*;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.*;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.generic.*;
import org.apache.bcel.util.*;

/**
   Adds "entering" logs to all methods of a class that have the LogEntry annotation.
*/
public class EntryLogger
{
   /**
      Adds entry logging code to the given class
      @param args the name of the class file to patch
   */
   public static void main(String[] args)
   {
      try
      {
         if (args.length == 0) 
            System.out.println("USAGE: java EntryLogger classname");
         else
         {
            Attribute.addAttributeReader("RuntimeVisibleAnnotations", 
               new AnnotationsAttributeReader());
            JavaClass jc = Repository.lookupClass(args[0]);
            ClassGen cg = new ClassGen(jc);
            EntryLogger el = new EntryLogger(cg);
            el.convert();
            File f = new File(Repository.lookupClassFile(cg.getClassName()).getPath());
            cg.getJavaClass().dump(f.getPath());
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   /**
      Constructs an EntryLogger that inserts logging into annotated methods of a given class 
      @param cg the class
   */
   public EntryLogger(ClassGen cg)
   {
      this.cg = cg;
      cpg = cg.getConstantPool();
   }

   /**
      converts the class by inserting the logging calls.
   */
   public void convert() throws IOException
   {
      for (Method m : cg.getMethods())
      {
         AnnotationsAttribute attr 
            = (AnnotationsAttribute) getAttribute(m, "RuntimeVisibleAnnotations");
         if (attr != null)
         {
            LogEntry logEntry = attr.getAnnotation(LogEntry.class);
            if (logEntry != null)
            {
               String loggerName = logEntry.logger();
               if (loggerName == null) loggerName = "";
               cg.replaceMethod(m, insertLogEntry(m, loggerName));               
            }
         }
      }
   }

   /**
      Adds an "entering" call to the beginning of a method.
      @param m the method
      @param loggerName the name of the logger to call
   */
   private Method insertLogEntry(Method m, String loggerName)
   {
      MethodGen mg = new MethodGen(m, cg.getClassName(), cpg);
      String className = cg.getClassName();
      String methodName = mg.getMethod().getName();
      System.out.printf("Adding logging instructions to %s.%s%n", className, methodName);

     
      int getLoggerIndex = cpg.addMethodref(
            "java.util.logging.Logger",
            "getLogger",
            "(Ljava/lang/String;)Ljava/util/logging/Logger;");
      int enteringIndex = cpg.addMethodref(
            "java.util.logging.Logger",
            "entering",
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

   /**
      Gets the attribute of a field or method with the given name.
      @param fm the field or method
      @param name the attribute name
      @return the attribute, or null, if no attribute with the given name was found
   */
   public static Attribute getAttribute(FieldOrMethod fm, String name)
   {
      for (Attribute attr : fm.getAttributes())
      {
         int nameIndex = attr.getNameIndex();
         ConstantPool cp = attr.getConstantPool();
         String attrName = cp.constantToString(cp.getConstant(nameIndex));
         if (attrName.equals(name))
            return attr;
      }
      return null;
   }

   private ClassGen cg;
   private ConstantPoolGen cpg;
}

/**
   This is a pluggable reader for an annotations attribute for the BCEL framework.
*/
class AnnotationsAttributeReader implements org.apache.bcel.classfile.AttributeReader
{
   public Attribute createAttribute(int nameIndex, int length, DataInputStream in,
      ConstantPool constantPool)
   {
      AnnotationsAttribute attribute = new AnnotationsAttribute(nameIndex, length, constantPool);
      try 
      {
         attribute.read(in, constantPool);
         return attribute;
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return null;
      }
   }
}

/**
   This attribute describes a set of annotations. 
   Only String-valued annotation attributes are supported.
*/
class AnnotationsAttribute extends Attribute
{
   /**
      Reads this annotation.
      @param nameIndex the index for the name of this attribute
      @param length the number of bytes in this attribute
      @param cp the constant pool
   */
   public AnnotationsAttribute (int nameIndex, int length, ConstantPool cp)
   {
      super(Constants.ATTR_UNKNOWN, nameIndex, length, cp);
      annotations = new HashMap<String, Map<String, String>>();
   }

   /**
      Reads this annotation.
      @param in the input stream
      @param cp the constant pool
   */
   public void read(DataInputStream in, ConstantPool cp)
      throws IOException
   {
      short numAnnotations = in.readShort();
      for (int i = 0; i < numAnnotations; i++) 
      {
         short typeIndex = in.readShort();
         String type = cp.constantToString(cp.getConstant(typeIndex));
         Map<String, String> nvPairs = new HashMap<String, String>();
         annotations.put(type, nvPairs);
         short numElementValuePairs = in.readShort();
         for (int j = 0; j < numElementValuePairs; j++) 
         {
            short nameIndex = in.readShort();
            String name = cp.constantToString(cp.getConstant(nameIndex));
            byte tag = in.readByte();
            if (tag == 's') 
            {
               short constValueIndex = in.readShort();
               String value = cp.constantToString(cp.getConstant(constValueIndex));
               nvPairs.put(name, value);
            }
            else
               throw new UnsupportedOperationException("Can only handle String attributes");
         }                        
      }
   }

   public void dump(DataOutputStream out)
      throws IOException
   {
      ConstantPoolGen cpg = new ConstantPoolGen(getConstantPool());

      out.writeShort(getNameIndex());
      out.writeInt(getLength());
      out.writeShort(annotations.size());
      for (Map.Entry<String, Map<String, String>> entry : annotations.entrySet()) 
      {
         String type = entry.getKey();
         Map<String, String> nvPairs = entry.getValue();
         out.writeShort(cpg.lookupUtf8(type));         
         out.writeShort(nvPairs.size());
         for (Map.Entry<String, String> nv : nvPairs.entrySet()) 
         {
            out.writeShort(cpg.lookupUtf8(nv.getKey()));
            out.writeByte('s');
            out.writeShort(cpg.lookupUtf8(nv.getValue()));
         }
      }
   }

   /**
      Gets an annotation from this set of annotations.
      @param annotationClass the class of the annotation to get
      @return the annotation object, or null if no matching annotation is present
   */
   public <A extends Annotation> A getAnnotation(Class<A> annotationClass)
   {
      String key = "L" + annotationClass.getName() + ";";
      final Map<String, String> nvPairs = annotations.get(key);
      if (nvPairs == null) return null;

      InvocationHandler handler = new
         InvocationHandler()
         {
            public Object invoke(Object proxy, java.lang.reflect.Method m, Object[] args) 
               throws Throwable
            {
               return nvPairs.get(m.getName());
            }
         };
   
      return (A) Proxy.newProxyInstance(
         getClass().getClassLoader(), 
         new Class[] { annotationClass }, 
         handler);
   }

   public void accept(org.apache.bcel.classfile.Visitor v)
   {
      throw new UnsupportedOperationException();
   }
   
   public Attribute copy(ConstantPool cp)
   {
      throw new UnsupportedOperationException();
   }
   
   public String toString ()
   {
      return annotations.toString();
   }
   
   private Map<String, Map<String, String>> annotations;
}
