import java.io.*;
import java.security.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This program computes the message digest of a file or the contents of a text area.
 * @version 1.13 2007-10-06
 * @author Cay Horstmann
 */
public class MessageDigestTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new MessageDigestFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame contains a menu for computing the message digest of a file or text area, radio buttons
 * to toggle between SHA-1 and MD5, a text area, and a text field to show the messge digest.
 */
class MessageDigestFrame extends JFrame
{
   public MessageDigestFrame()
   {
      setTitle("MessageDigestTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JPanel panel = new JPanel();
      ButtonGroup group = new ButtonGroup();
      addRadioButton(panel, "SHA-1", group);
      addRadioButton(panel, "MD5", group);

      add(panel, BorderLayout.NORTH);
      add(new JScrollPane(message), BorderLayout.CENTER);
      add(digest, BorderLayout.SOUTH);
      digest.setFont(new Font("Monospaced", Font.PLAIN, 12));

      setAlgorithm("SHA-1");

      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu("File");
      JMenuItem fileDigestItem = new JMenuItem("File digest");
      fileDigestItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               loadFile();
            }
         });
      menu.add(fileDigestItem);
      JMenuItem textDigestItem = new JMenuItem("Text area digest");
      textDigestItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               String m = message.getText();
               computeDigest(m.getBytes());
            }
         });
      menu.add(textDigestItem);
      menuBar.add(menu);
      setJMenuBar(menuBar);
   }

   /**
    * Adds a radio button to select an algorithm.
    * @param c the container into which to place the button
    * @param name the algorithm name
    * @param g the button group
    */
   public void addRadioButton(Container c, final String name, ButtonGroup g)
   {
      ActionListener listener = new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               setAlgorithm(name);
            }
         };
      JRadioButton b = new JRadioButton(name, g.getButtonCount() == 0);
      c.add(b);
      g.add(b);
      b.addActionListener(listener);
   }

   /**
    * Sets the algorithm used for computing the digest.
    * @param alg the algorithm name
    */
   public void setAlgorithm(String alg)
   {
      try
      {
         currentAlgorithm = MessageDigest.getInstance(alg);
         digest.setText("");
      }
      catch (NoSuchAlgorithmException e)
      {
         digest.setText("" + e);
      }
   }

   /**
    * Loads a file and computes its message digest.
    */
   public void loadFile()
   {
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      int r = chooser.showOpenDialog(this);
      if (r == JFileChooser.APPROVE_OPTION)
      {
         try
         {
            String name = chooser.getSelectedFile().getAbsolutePath();
            computeDigest(loadBytes(name));
         }
         catch (IOException e)
         {
            JOptionPane.showMessageDialog(null, e);
         }
      }
   }

   /**
    * Loads the bytes in a file.
    * @param name the file name
    * @return an array with the bytes in the file
    */
   public byte[] loadBytes(String name) throws IOException
   {
      FileInputStream in = null;

      in = new FileInputStream(name);
      try
      {
         ByteArrayOutputStream buffer = new ByteArrayOutputStream();
         int ch;
         while ((ch = in.read()) != -1)
            buffer.write(ch);
         return buffer.toByteArray();
      }
      finally
      {
         in.close();
      }
   }

   /**
    * Computes the message digest of an array of bytes and displays it in the text field.
    * @param b the bytes for which the message digest should be computed.
    */
   public void computeDigest(byte[] b)
   {
      currentAlgorithm.reset();
      currentAlgorithm.update(b);
      byte[] hash = currentAlgorithm.digest();
      String d = "";
      for (int i = 0; i < hash.length; i++)
      {
         int v = hash[i] & 0xFF;
         if (v < 16) d += "0";
         d += Integer.toString(v, 16).toUpperCase() + " ";
      }
      digest.setText(d);
   }

   private JTextArea message = new JTextArea();
   private JTextField digest = new JTextField();
   private MessageDigest currentAlgorithm;
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 300;
}
