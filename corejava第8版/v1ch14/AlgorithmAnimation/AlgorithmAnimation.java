import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;

/**
 * This program animates a sort algorithm.
 * @version 1.01 2007-05-18
 * @author Cay Horstmann
 */
public class AlgorithmAnimation
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new AnimationFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame shows the array as it is sorted, together with buttons to single-step the animation or
 * to run it without interruption.
 */
class AnimationFrame extends JFrame
{
   public AnimationFrame()
   {
      ArrayComponent comp = new ArrayComponent();
      add(comp, BorderLayout.CENTER);

      final Sorter sorter = new Sorter(comp);

      JButton runButton = new JButton("Run");
      runButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               sorter.setRun();
            }
         });

      JButton stepButton = new JButton("Step");
      stepButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               sorter.setStep();
            }
         });

      JPanel buttons = new JPanel();
      buttons.add(runButton);
      buttons.add(stepButton);
      add(buttons, BorderLayout.NORTH);
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      Thread t = new Thread(sorter);
      t.start();
   }

   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 300;
}

/**
 * This runnable executes a sort algorithm. When two elements are compared, the algorithm pauses and
 * updates a component.
 */
class Sorter implements Runnable
{
   /**
    * Constructs a Sorter.
    * @param values the array to be sorted
    * @param comp the component on which to display the sorting progress
    */
   public Sorter(ArrayComponent comp)
   {
      values = new Double[VALUES_LENGTH];
      for (int i = 0; i < values.length; i++)
         values[i] = new Double(Math.random());
      this.component = comp;
      this.gate = new Semaphore(1);
      this.run = false;
   }

   /**
    * Sets the sorter to "run" mode. Called on the event dispatch thread.
    */
   public void setRun()
   {
      run = true;
      gate.release();
   }

   /**
    * Sets the sorter to "step" mode. Called on the event dispatch thread.
    */
   public void setStep()
   {
      run = false;
      gate.release();
   }

   public void run()
   {
      Comparator<Double> comp = new Comparator<Double>()
         {
            public int compare(Double i1, Double i2)
            {
               component.setValues(values, i1, i2);
               try
               {
                  if (run) Thread.sleep(DELAY);
                  else gate.acquire();
               }
               catch (InterruptedException exception)
               {
                  Thread.currentThread().interrupt();
               }
               return i1.compareTo(i2);
            }
         };
      Arrays.sort(values, comp);
      component.setValues(values, null, null);
   }

   private Double[] values;
   private ArrayComponent component;
   private Semaphore gate;
   private static final int DELAY = 100;
   private volatile boolean run;
   private static final int VALUES_LENGTH = 30;
}

/**
 * This component draws an array and marks two elements in the array.
 */
class ArrayComponent extends JComponent
{
   /**
    * Sets the values to be painted. Called on the sorter thread.
    * @param values the array of values to display
    * @param marked1 the first marked element
    * @param marked2 the second marked element
    */
   public synchronized void setValues(Double[] values, Double marked1, Double marked2)
   {
      this.values = values.clone();
      this.marked1 = marked1;
      this.marked2 = marked2;
      repaint();
   }

   public synchronized void paintComponent(Graphics g) // Called on the event dispatch thread
   {
      if (values == null) return;
      Graphics2D g2 = (Graphics2D) g;
      int width = getWidth() / values.length;
      for (int i = 0; i < values.length; i++)
      {
         double height = values[i] * getHeight();
         Rectangle2D bar = new Rectangle2D.Double(width * i, 0, width, height);
         if (values[i] == marked1 || values[i] == marked2) g2.fill(bar);
         else g2.draw(bar);
      }
   }

   private Double marked1;
   private Double marked2;
   private Double[] values;
}
