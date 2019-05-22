import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * A program to test a progress monitor dialog.
 * @version 1.04 2007-08-01
 * @author Cay Horstmann
 */
public class ProgressMonitorTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ProgressMonitorFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame that contains a button to launch a simulated activity and a text area for the activity
 * output.
 */
class ProgressMonitorFrame extends JFrame
{
   public ProgressMonitorFrame()
   {
      setTitle("ProgressMonitorTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // this text area holds the activity output
      textArea = new JTextArea();

      // set up a button panel
      JPanel panel = new JPanel();
      startButton = new JButton("Start");
      panel.add(startButton);

      add(new JScrollPane(textArea), BorderLayout.CENTER);
      add(panel, BorderLayout.SOUTH);

      // set up the button action

      startButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               startButton.setEnabled(false);
               final int MAX = 1000;

               // start activity
               activity = new SimulatedActivity(MAX);
               activity.execute();
               
               // launch progress dialog
               progressDialog = new ProgressMonitor(ProgressMonitorFrame.this,
                     "Waiting for Simulated Activity", null, 0, MAX);
               cancelMonitor.start();              
            }
         });

      // set up the timer action

      cancelMonitor = new Timer(500, new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {               
               if (progressDialog.isCanceled())
               {                  
                  activity.cancel(true);
                  startButton.setEnabled(true);                  
               }
               else if (activity.isDone())
               {
                  progressDialog.close();
                  startButton.setEnabled(true);                  
               }
               else
               {
                  progressDialog.setProgress(activity.getProgress());                  
               }
            }
         });
   }

   private Timer cancelMonitor;
   private JButton startButton;
   private ProgressMonitor progressDialog;
   private JTextArea textArea;
   private SimulatedActivity activity;

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;

   class SimulatedActivity extends SwingWorker<Void, Integer>
   {
      /**
       * Constructs the simulated activity that increments a counter from 0 to a
       * given target.
       * @param t the target value of the counter.
       */
      public SimulatedActivity(int t)
      {
         current = 0;
         target = t;
      }

      protected Void doInBackground() throws Exception
      {
         try
         {
            while (current < target)
            {
               Thread.sleep(100);
               current++;
               textArea.append(current + "\n");               
               setProgress(current);
            }
         }
         catch (InterruptedException e)
         {
         }
         return null;
      }
      
      private int current;
      private int target;
   }      
}
