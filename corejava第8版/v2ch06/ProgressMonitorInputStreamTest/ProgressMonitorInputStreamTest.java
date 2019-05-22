import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * A program to test a progress monitor input stream.
 * @version 1.04 2007-08-01
 * @author Cay Horstmann
 */
public class ProgressMonitorInputStreamTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new TextFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame with a menu to load a text file and a text area to display its contents. The text area is
 * constructed when the file is loaded and set as the content pane of the frame when the loading is
 * complete. That avoids flicker during loading.
 */
class TextFrame extends JFrame
{
   public TextFrame()
   {
      setTitle("ProgressMonitorInputStreamTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      textArea = new JTextArea();
      add(new JScrollPane(textArea));

      chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      JMenu fileMenu = new JMenu("File");
      menuBar.add(fileMenu);
      openItem = new JMenuItem("Open");
      openItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               try
               {
                  openFile();
               }
               catch (IOException exception)
               {
                  exception.printStackTrace();
               }
            }
         });

      fileMenu.add(openItem);
      exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });
      fileMenu.add(exitItem);
   }

   /**
    * Prompts the user to select a file, loads the file into a text area, and sets it as the content
    * pane of the frame.
    */
   public void openFile() throws IOException
   {
      int r = chooser.showOpenDialog(this);
      if (r != JFileChooser.APPROVE_OPTION) return;
      final File f = chooser.getSelectedFile();

      // set up stream and reader filter sequence
      
      FileInputStream fileIn = new FileInputStream(f);
      ProgressMonitorInputStream progressIn = new ProgressMonitorInputStream(this, "Reading "
            + f.getName(), fileIn);
      final Scanner in = new Scanner(progressIn);

      textArea.setText("");

      SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
         {
            protected Void doInBackground() throws Exception
            {
               while (in.hasNextLine())
               {
                  String line = in.nextLine();
                  textArea.append(line);
                  textArea.append("\n");
               }
               in.close();
               return null;
            }
         };
      worker.execute();
   }

   private JMenuItem openItem;
   private JMenuItem exitItem;
   private JTextArea textArea;
   private JFileChooser chooser;

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;
}
