package script;

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import javax.script.*;
import javax.swing.*;

/**
 * @version 1.04 2021-06-17
 * @author Cay Horstmann
 */
public class ScriptTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            try
            {
               var manager = new ScriptEngineManager();
               String language;
               if (args.length == 0) 
               {
                  System.out.println("Available factories: ");
                  for (ScriptEngineFactory factory : manager.getEngineFactories())
                     System.out.println(factory.getEngineName());
                  
                  language = "nashorn";
               }
               else language = args[0];

               final ScriptEngine engine = manager.getEngineByName(language);               
               if (engine == null)
               {
                  System.err.println("No engine for " + language);
                  System.exit(1);
               }               

               final String frameClassName 
                  = args.length < 2 ? "buttons1.ButtonFrame" : args[1]; 
               
               var frame 
                  = (JFrame) Class.forName(frameClassName).getConstructor().newInstance();
               InputStream in = frame.getClass().getResourceAsStream("init." + language);
               if (in != null) engine.eval(
                  new InputStreamReader(in, StandardCharsets.UTF_8));
               var components = new HashMap<String, Component>();
               getComponentBindings(frame, components);
               components.forEach((name, c) -> engine.put(name, c)); 

               var events = new Properties();
               in = frame.getClass().getResourceAsStream(language + ".properties");
               events.load(
                  new InputStreamReader(in, StandardCharsets.UTF_8));
               
               for (Object e : events.keySet())
               {
                  String[] s = ((String) e).split("\\.");
                  addListener(s[0], s[1], (String) events.get(e), engine, components);
               }
               frame.setTitle("ScriptTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
            catch (ReflectiveOperationException | IOException 
                  | ScriptException | IntrospectionException e)
            {
               e.printStackTrace();
            }
         });
   }

   /**
    * Gathers all named components in a container.
    * @param c the component
    * @param namedComponents a map into which to enter the component names and components
    */
   private static void getComponentBindings(Component c, 
         Map<String, Component> namedComponents)
   {
      String name = c.getName();
      if (name != null) { namedComponents.put(name, c); }
      if (c instanceof Container container)
      {
         for (Component child : container.getComponents())
            getComponentBindings(child, namedComponents);
      }
   }

   /**
    * Adds a listener to an object whose listener method executes a script.
    * @param beanName the name of the bean to which the listener should be added
    * @param eventName the name of the listener type, such as "action" or "change"
    * @param scriptCode the script code to be executed
    * @param engine the engine that executes the code
    * @param bindings the bindings for the execution
    * @throws IntrospectionException 
    */
   private static void addListener(String beanName, String eventName, final String scriptCode,
         ScriptEngine engine, Map<String, Component> components) 
         throws ReflectiveOperationException, IntrospectionException
   {
      Object bean = components.get(beanName);
      EventSetDescriptor descriptor = getEventSetDescriptor(bean, eventName);
      if (descriptor == null) return;
      descriptor.getAddListenerMethod().invoke(bean,
         Proxy.newProxyInstance(null, new Class[] { descriptor.getListenerType() },
            (proxy, method, args) ->
               {
                  engine.eval(scriptCode);
                  return null;
               }));
   }

   private static EventSetDescriptor getEventSetDescriptor(Object bean, String eventName)
         throws IntrospectionException
   {
      for (EventSetDescriptor descriptor : Introspector.getBeanInfo(bean.getClass())
            .getEventSetDescriptors())
         if (descriptor.getName().equals(eventName)) return descriptor;
      return null;
   }
}
