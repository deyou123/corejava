import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;

/**
 * This program demonstrates the use of a progress bar to monitor the progress of a thread.
 * @version 1.04 2007-08-01
 * @author Cay Horstmann
 */
public class ProgressBarTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new ProgressBarFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame that contains a button to launch a simulated activity, a progress bar, and a text area
 * for the activity output.
 */
class ProgressBarFrame extends JFrame
{
   public ProgressBarFrame()
   {
      setTitle("ProgressBarTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // this text area holds the activity output
      textArea = new JTextArea();

      // set up panel with button and progress bar

      final int MAX = 1000;
      JPanel panel = new JPanel();
      startButton = new JButton("Start");
      progressBar = new JProgressBar(0, MAX);
      progressBar.setStringPainted(true);
      panel.add(startButton);
      panel.add(progressBar);

      checkBox = new JCheckBox("indeterminate");
      checkBox.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               progressBar.setIndeterminate(checkBox.isSelected());
               progressBar.setStringPainted(!progressBar.isIndeterminate());
            }
         });
      panel.add(checkBox);
      add(new JScrollPane(textArea), BorderLayout.CENTER);
      add(panel, BorderLayout.SOUTH);

      // set up the button action

      startButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               startButton.setEnabled(false);
               activity = new SimulatedActivity(MAX);
               activity.execute();
            }
         });
   }

   private JButton startButton;
   private JProgressBar progressBar;
   private JCheckBox checkBox;
   private JTextArea textArea;
   private SimulatedActivity activity;

   public static final int DEFAULT_WIDTH = 400;
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
               publish(current);
            }
         }
         catch (InterruptedException e)
         {
         }
         return null;
      }

      protected void process(List<Integer> chunks)
      {
         for (Integer chunk : chunks)
         {
            textArea.append(chunk + "\n");
            progressBar.setValue(chunk);
         }
      }
      
      protected void done()
      {
         startButton.setEnabled(true);
      }
      
      private int current;
      private int target;
   }   
}