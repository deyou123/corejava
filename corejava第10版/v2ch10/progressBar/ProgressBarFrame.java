package progressBar;

import java.awt.*;
import java.util.List;

import javax.swing.*;

/**
 * A frame that contains a button to launch a simulated activity, a progress bar, and a text area
 * for the activity output.
 */
public class ProgressBarFrame extends JFrame
{
   public static final int TEXT_ROWS = 10;
   public static final int TEXT_COLUMNS = 40;

   private JButton startButton;
   private JProgressBar progressBar;
   private JCheckBox checkBox;
   private JTextArea textArea;
   private SimulatedActivity activity;

   public ProgressBarFrame()
   {
      // this text area holds the activity output
      textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);

      // set up panel with button and progress bar

      final int MAX = 1000;
      JPanel panel = new JPanel();
      startButton = new JButton("Start");
      progressBar = new JProgressBar(0, MAX);
      progressBar.setStringPainted(true);
      panel.add(startButton);
      panel.add(progressBar);

      checkBox = new JCheckBox("indeterminate");
      checkBox.addActionListener(event ->
         {
            progressBar.setIndeterminate(checkBox.isSelected());
            progressBar.setStringPainted(!progressBar.isIndeterminate());
         });
      panel.add(checkBox);
      add(new JScrollPane(textArea), BorderLayout.CENTER);
      add(panel, BorderLayout.SOUTH);

      // set up the button action

      startButton.addActionListener(event ->
         {
            startButton.setEnabled(false);
            activity = new SimulatedActivity(MAX);
            activity.execute();
         });
      pack();
   }
   
   class SimulatedActivity extends SwingWorker<Void, Integer>
   {
      private int current;
      private int target;

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
   }   
}