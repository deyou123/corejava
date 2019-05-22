package script;

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.script.*;
import javax.swing.*;

/**
 * @version 1.01 2012-01-28
 * @author Cay Horstmann
 */
public class ScriptTest
{
   public static void main(final String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               try
               {
                  ScriptEngineManager manager = new ScriptEngineManager();
                  String language;
                  if (args.length == 0) 
                  {
                     System.out.println("Available factories: ");
                     for (ScriptEngineFactory factory : manager.getEngineFactories())
                        System.out.println(factory.getEngineName());
                     
                     language = "js";
                  }
                  else language = args[0];

                  final ScriptEngine engine = manager.getEngineByName(language);               
                  if (engine == null)
                  {
                     System.err.println("No engine for " + language);
                     System.exit(1);
                  }               

                  final String frameClassName = args.length < 2 ? "buttons1.ButtonFrame" : args[1]; 
                  
                  JFrame frame = (JFrame) Class.forName(frameClassName).newInstance();
                  InputStream in = frame.getClass().getResourceAsStream("init." + language);
                  if (in != null) engine.eval(new InputStreamReader(in));
                  getComponentBindings(frame, engine);

                  final Properties events = new Properties();
                  in = frame.getClass().getResourceAsStream(language + ".properties");
                  events.load(in);

                  for (final Object e : events.keySet())
                  {
                     String[] s = ((String) e).split("\\.");
                     addListener(s[0], s[1], (String) events.get(e), engine);
                  }
                  frame.setTitle("ScriptTest");
                  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                  frame.setVisible(true);
               }
               catch (ReflectiveOperationException | IOException 
                  | ScriptException | IntrospectionException ex)
               {
                  ex.printStackTrace();
               }
            }
         });
   }

   /**
    * Gathers all named components in a container.
    * @param c the component
    * @param namedComponents
    */
   private static void getComponentBindings(Component c, ScriptEngine engine)
   {
      String name = c.getName();
      if (name != null) engine.put(name, c);
      if (c instanceof Container)
      {
         for (Component child : ((Container) c).getComponents())
            getComponentBindings(child, engine);
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
      final ScriptEngine engine) throws ReflectiveOperationException, IntrospectionException
   {
      Object bean = engine.get(beanName);
      EventSetDescriptor descriptor = getEventSetDescriptor(bean, eventName);
      if (descriptor == null) return;
      descriptor.getAddListenerMethod().invoke(bean,
         Proxy.newProxyInstance(null, new Class[] { descriptor.getListenerType() },
            new InvocationHandler()
               {
                  public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable
                  {
                     engine.eval(scriptCode);
                     return null;
                  }
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