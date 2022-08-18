package compiler;

import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.tools.*;
import javax.tools.JavaFileObject.*;

/**
 * @version 1.10 2018-05-01
 * @author Cay Horstmann
 */
public class CompilerTest
{
   public static void main(final String[] args) 
         throws IOException, ReflectiveOperationException
   {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

      var classFileObjects = new ArrayList<ByteArrayClass>();

      var diagnostics = new DiagnosticCollector<JavaFileObject>();

      JavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
      fileManager = new ForwardingJavaFileManager<JavaFileManager>(fileManager)
         {
            public JavaFileObject getJavaFileForOutput(Location location, 
                  String className, Kind kind, FileObject sibling) throws IOException
            {
               if (kind == Kind.CLASS) 
               {
                  var fileObject = new ByteArrayClass(className);
                  classFileObjects.add(fileObject);
                  return fileObject;
               }
               else return super.getJavaFileForOutput(location, className, kind, sibling);
            }
         };


      String frameClassName = args.length == 0 ? "buttons2.ButtonFrame" : args[0];
      //compiler.run(null, null, null, frameClassName.replace(".",  "/") + ".java");
      
      StandardJavaFileManager fileManager2 = compiler.getStandardFileManager(null, null, null);
      var sources = new ArrayList<JavaFileObject>();
      for (JavaFileObject o : fileManager2.getJavaFileObjectsFromStrings(
            List.of(frameClassName.replace(".",  "/") + ".java")))
         sources.add(o);
      
      JavaFileObject source = buildSource(frameClassName);
      JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, 
         null, null, List.of(source));
      Boolean result = task.call();

      for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics())
         System.out.println(d.getKind() + ": " + d.getMessage(null));
      fileManager.close();
      if (!result)
      {
         System.out.println("Compilation failed.");
         System.exit(1);
      }

      var loader = new ByteArrayClassLoader(classFileObjects);
      var frame = (JFrame) loader.loadClass("x.Frame").getConstructor().newInstance();
      
      EventQueue.invokeLater(() ->
         {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("CompilerTest");
            frame.setVisible(true);
         });
   }

   /*
    * Builds the source for the subclass that implements the addEventHandlers method.
    * @return a file object containing the source in a string builder
    */
   static JavaFileObject buildSource(String superclassName) 
         throws IOException, ClassNotFoundException
   {
      var builder = new StringBuilder(); 
      builder.append("package x;\n\n");
      builder.append("public class Frame extends " + superclassName + " {\n");
      builder.append("protected void addEventHandlers() {\n");
      var props = new Properties();
      props.load(Files.newBufferedReader(
         Path.of(superclassName.replace(".", "/")).getParent().resolve("action.properties"),
         StandardCharsets.UTF_8));
      for (Map.Entry<Object, Object> e : props.entrySet())
      {
         var beanName = (String) e.getKey();
         var eventCode = (String) e.getValue();
         builder.append(beanName + ".addActionListener(event -> {\n");
         builder.append(eventCode);
         builder.append("\n} );\n");
      }
      builder.append("} }\n");
      return new StringSource("x.Frame", builder.toString());
   }
}
