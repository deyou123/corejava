import java.io.*;
import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This program demonstrates a custom class loader that decrypts class files.
 * @version 1.22 2007-10-05
 * @author Cay Horstmann
 */
public class ClassLoaderTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {

               JFrame frame = new ClassLoaderFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame contains two text fields for the name of the class to load and the decryption key.
 */
class ClassLoaderFrame extends JFrame
{
   public ClassLoaderFrame()
   {
      setTitle("ClassLoaderTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      setLayout(new GridBagLayout());
      add(new JLabel("Class"), new GBC(0, 0).setAnchor(GBC.EAST));
      add(nameField, new GBC(1, 0).setWeight(100, 0).setAnchor(GBC.WEST));
      add(new JLabel("Key"), new GBC(0, 1).setAnchor(GBC.EAST));
      add(keyField, new GBC(1, 1).setWeight(100, 0).setAnchor(GBC.WEST));
      JButton loadButton = new JButton("Load");
      add(loadButton, new GBC(0, 2, 2, 1));
      loadButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               runClass(nameField.getText(), keyField.getText());
            }
         });
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
         ClassLoader loader = new CryptoClassLoader(Integer.parseInt(key));
         Class<?> c = loader.loadClass(name);
         Method m = c.getMethod("main", String[].class);
         m.invoke(null, (Object) new String[] {});
      }
      catch (Throwable e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   private JTextField keyField = new JTextField("3", 4);
   private JTextField nameField = new JTextField("Calculator", 30);
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 200;
}

/**
 * This class loader loads encrypted class files.
 */
class CryptoClassLoader extends ClassLoader
{
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
      byte[] classBytes = null;
      try
      {
         classBytes = loadClassBytes(name);
      }
      catch (IOException e)
      {
         throw new ClassNotFoundException(name);
      }

      Class<?> cl = defineClass(name, classBytes, 0, classBytes.length);
      if (cl == null) throw new ClassNotFoundException(name);
      return cl;
   }

   /**
    * Loads and decrypt the class file bytes.
    * @param name the class name
    * @return an array with the class file bytes
    */
   private byte[] loadClassBytes(String name) throws IOException
   {
      String cname = name.replace('.', '/') + ".caesar";
      FileInputStream in = null;
      in = new FileInputStream(cname);
      try
      {
         ByteArrayOutputStream buffer = new ByteArrayOutputStream();
         int ch;
         while ((ch = in.read()) != -1)
         {
            byte b = (byte) (ch - key);
            buffer.write(b);
         }
         in.close();
         return buffer.toByteArray();
      }
      finally
      {
         in.close();
      }
   }

   private int key;
}
