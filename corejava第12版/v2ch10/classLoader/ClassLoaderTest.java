package classLoader;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This program demonstrates a custom class loader that decrypts class files.
 * @version 1.25 2018-05-01
 * @author Cay Horstmann
 */
public class ClassLoaderTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            var frame = new ClassLoaderFrame();
            frame.setTitle("ClassLoaderTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * This frame contains two text fields for the name of the class to load and the decryption
 * key.
 */
class ClassLoaderFrame extends JFrame
{
   private JTextField keyField = new JTextField("3", 4);
   private JTextField nameField = new JTextField("Calculator", 30);
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;

   public ClassLoaderFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      setLayout(new GridBagLayout());
      add(new JLabel("Class"), new GBC(0, 0).setAnchor(GBC.EAST));
      add(nameField, new GBC(1, 0).setWeight(100, 0).setAnchor(GBC.WEST));
      add(new JLabel("Key"), new GBC(0, 1).setAnchor(GBC.EAST));
      add(keyField, new GBC(1, 1).setWeight(100, 0).setAnchor(GBC.WEST));
      var loadButton = new JButton("Load");
      add(loadButton, new GBC(0, 2, 2, 1));
      loadButton.addActionListener(event -> runClass(nameField.getText(), keyField.getText()));
      pack();
   }

   /**
    * Runs the main method of a given class.
    * @param name the class name
    * @param key the decryption key for the class files
    */
   public void runClass(String name, String key)
   {
      try
      {
         var loader = new CryptoClassLoader(Integer.parseInt(key));
         Class<?> c = loader.loadClass(name);
         Method m = c.getMethod("main", String[].class);
         m.invoke(null, (Object) new String[] {});
      }
      catch (Throwable t)
      {
         JOptionPane.showMessageDialog(this, t);
      }
   }
}

/**
 * This class loader loads encrypted class files.
 */
class CryptoClassLoader extends ClassLoader
{
   private int key;

   /**
    * Constructs a crypto class loader.
    * @param k the decryption key
    */
   public CryptoClassLoader(int k)
   {
      key = k;
   }

   protected Class<?> findClass(String name) throws ClassNotFoundException
   {
      try
      {
         byte[] classBytes = null;
         classBytes = loadClassBytes(name);
         Class<?> cl = defineClass(name, classBytes, 0, classBytes.length);
         if (cl == null) throw new ClassNotFoundException(name);
         return cl;
      }
      catch (IOException e)
      {
         throw new ClassNotFoundException(name);
      }
   }

   /**
    * Loads and decrypt the class file bytes.
    * @param name the class name
    * @return an array with the class file bytes
    */
   private byte[] loadClassBytes(String name) throws IOException
   {
      String cname = name.replace('.', '/') + ".caesar";
      byte[] bytes = Files.readAllBytes(Path.of(cname));
      for (int i = 0; i < bytes.length; i++)
         bytes[i] = (byte) (bytes[i] - key);
      return bytes;
   }
}
