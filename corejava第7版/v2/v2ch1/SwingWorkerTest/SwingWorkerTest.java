import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;

/**
   This program demonstrates a worker thread that runs 
   a potentially time-consuming task.
*/
public class SwingWorkerTest
{
   public static void main(String[] args) throws Exception
   {
      JFrame frame = new SwingWorkerFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame has a text area to show the contents of a text file,
   a menu to open a file and cancel the opening process, and
   a status line to show the file loading progress.
*/
class SwingWorkerFrame extends JFrame
{
   public SwingWorkerFrame()
   {
      chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      textArea = new JTextArea();
      add(new JScrollPane(textArea));
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      statusLine = new JLabel();
      add(statusLine, BorderLayout.SOUTH);

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu menu = new JMenu("File");
      menuBar.add(menu);

      openItem = new JMenuItem("Open");
      menu.add(openItem);
      openItem.addActionListener(new 
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               // show file chooser dialog
               int result = chooser.showOpenDialog(null);

               // if file selected, set it as icon of the label
               if (result == JFileChooser.APPROVE_OPTION)
               {
                  readFile(chooser.getSelectedFile());
               }
            }
         });

      cancelItem = new JMenuItem("Cancel");
      menu.add(cancelItem);
      cancelItem.setEnabled(false);
      cancelItem.addActionListener(new 
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               if (workerThread != null) workerThread.interrupt();
            }
         });
   } 

   /**
      Reads a file asynchronously, updating the UI during the reading process.
      @param file the file to read
   */
   public void readFile(final File file)
   {
      Runnable task = new
         SwingWorkerTask()
         {
            public void init()
            {
               lineNumber = 0;
               openItem.setEnabled(false);                              
               cancelItem.setEnabled(true);
            }
            
            public void update()
            {
               statusLine.setText("" + lineNumber);
            }
            
            public void finish()
            {
               workerThread = null;
               openItem.setEnabled(true);
               cancelItem.setEnabled(false);     
               statusLine.setText("Done");
            }
            
            public void work()
            {
               try
               {
                  Scanner in = new Scanner(new FileInputStream(file));
                  textArea.setText("");
                  while (!Thread.currentThread().isInterrupted() && in.hasNextLine()) 
                  { 
                     lineNumber++;
                     line = in.nextLine();  
                     textArea.append(line);
                     textArea.append("\n");
                     doUpdate();
                  }
               }
               catch (IOException e)
               {
                  JOptionPane.showMessageDialog(null, "" + e);
               }
            }
            
            private String line;
            private int lineNumber;
         };

      workerThread = new Thread(task);
      workerThread.start();
   }
         
   private JFileChooser chooser;
   private JTextArea textArea;
   private JLabel statusLine;
   private JMenuItem openItem;
   private JMenuItem cancelItem;
   private Thread workerThread;

   public static final int DEFAULT_WIDTH = 450;
   public static final int DEFAULT_HEIGHT = 350;  
}

/**
   Extend this class to define an asynchronous task
   that updates a Swing UI.
*/
abstract class SwingWorkerTask implements Runnable
{
   /**
      Place your task in this method. Be sure to call doUpdate(), not update(), to show the 
      update after each unit of work.
   */
   public abstract void work() throws InterruptedException;

   /**
      Override this method for UI operations before work commences.
   */
   public void init() {}
   /**
      Override this method for UI operations after each unit or work.
   */
   public void update() {}
   /**
      Override this method for UI operations after work is completed.
   */
   public void finish() {}

   private void doInit()
   {
      EventQueue.invokeLater(new
         Runnable()
         {
            public void run() { init(); }
         });    
   }

   /**
      Call this method from work() to show the update after each unit of work.
   */
   protected final void doUpdate()
   {
      if (done) return;
      EventQueue.invokeLater(new
         Runnable()
         {
            public void run() { update(); }
         });      
   }

   private void doFinish()
   {
      EventQueue.invokeLater(new
         Runnable()
         {
            public void run() { finish(); }
         });
   }

   public final void run()
   {      
      doInit();
      try
      {
         done = false;
         work();
      }
      catch (InterruptedException ex)
      {
      }
      finally
      {
         done = true;
         doFinish();
      }
   }

   private boolean done;
}
