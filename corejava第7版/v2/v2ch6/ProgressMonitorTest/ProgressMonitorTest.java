/**
   @version 1.03 2004-08-22
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.Timer;

/**
   A program to test a progress monitor dialog.
*/
public class ProgressMonitorTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new ProgressMonitorFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);      
   }
}

/**
   A frame that contains a button to launch a simulated activity
   and a text area for the activity output.
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

      startButton.addActionListener(new 
         ActionListener()
         {  
            public void actionPerformed(ActionEvent event)
            {  
               // start activity
               activity = new SimulatedActivity(1000);
               activityThread = new Thread(activity);
               activityThread.start();

               // launch progress dialog
               progressDialog = new ProgressMonitor(ProgressMonitorFrame.this,
                  "Waiting for Simulated Activity", null, 0, activity.getTarget());
               
               // start timer
               activityMonitor.start();
               
               startButton.setEnabled(false);
            }
         });

      // set up the timer action

      activityMonitor = new Timer(500, new 
         ActionListener()
         {  
            public void actionPerformed(ActionEvent event)
            {  
               int current = activity.getCurrent();

               // show progress
               textArea.append(current + "\n");
               progressDialog.setProgress(current);
               
               // check if task is completed or canceled
               if (current == activity.getTarget() || progressDialog.isCanceled())
               {  
                  activityMonitor.stop();
                  progressDialog.close();
                  activityThread.interrupt();
                  startButton.setEnabled(true);
               }
            }
         });
   }

   private Timer activityMonitor;
   private JButton startButton;
   private ProgressMonitor progressDialog;
   private JTextArea textArea;
   private Thread activityThread;
   private SimulatedActivity activity;

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;
}

/**
   A similated activity runnable.
*/
class SimulatedActivity implements Runnable
{ 
   /**
      Constructs the simulated activity thread object. The
      thread increments a counter from 0 to a given target.
      @param t the target value of the counter.
   */
   public SimulatedActivity(int t)
   {  
      current = 0;
      target = t;
   }

   public int getTarget()
   {  
      return target;
   }

   public int getCurrent()
   {  
      return current;
   }

   public void run()
   {  
      try
      {
         while (current < target)
         {    
            Thread.sleep(100);
            current++;
         }
      }
      catch(InterruptedException e)
      {  
      }
   }

   private volatile int current;
   private int target;
}
