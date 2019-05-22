package webstart;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/**
   A panel with calculator buttons and a result display.
*/
public class CalculatorPanel extends JPanel
{  
   private JTextArea display;
   private JPanel panel;
   private double result;
   private String lastCommand;
   private boolean start;

   /**
      Lays out the panel.
   */
   public CalculatorPanel()
   {  
      setLayout(new BorderLayout());

      result = 0;
      lastCommand = "=";
      start = true;
      
      // add the display

      display = new JTextArea(10, 20);

      add(new JScrollPane(display), BorderLayout.NORTH);
      
      ActionListener insert = new InsertAction();
      ActionListener command = new CommandAction();

      // add the buttons in a 4 x 4 grid

      panel = new JPanel();
      panel.setLayout(new GridLayout(4, 4));

      addButton("7", insert);
      addButton("8", insert);
      addButton("9", insert);
      addButton("/", command);

      addButton("4", insert);
      addButton("5", insert);
      addButton("6", insert);
      addButton("*", command);

      addButton("1", insert);
      addButton("2", insert);
      addButton("3", insert);
      addButton("-", command);

      addButton("0", insert);
      addButton(".", insert);
      addButton("=", command);
      addButton("+", command);

      add(panel, BorderLayout.CENTER);
   }

   /**
      Gets the history text.
      @return the calculator history
   */
   public String getText()
   {
      return display.getText();
   }
   
   /**
      Appends a string to the history text.
      @param s the string to append
   */
   public void append(String s)
   {
      display.append(s);
   }

   /**
      Adds a button to the center panel.
      @param label the button label
      @param listener the button listener
   */
   private void addButton(String label, ActionListener listener)
   {  
      JButton button = new JButton(label);
      button.addActionListener(listener);
      panel.add(button);
   }

   /**
      This action inserts the button action string to the
      end of the display text.
   */
   private class InsertAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {
         String input = event.getActionCommand();
         start = false;
         display.append(input);
      }
   }

   /**
      This action executes the command that the button
      action string denotes.
   */
   private class CommandAction implements ActionListener
   {
      public void actionPerformed(ActionEvent event)
      {  
         String command = event.getActionCommand();

         if (start)
         {  
            if (command.equals("-")) 
            { 
               display.append(command); 
               start = false; 
            }
            else 
               lastCommand = command;
         }
         else
         {  
            try
            {
               int lines = display.getLineCount();
               int lineStart = display.getLineStartOffset(lines - 1);
               int lineEnd = display.getLineEndOffset(lines - 1);
               String value = display.getText(lineStart, lineEnd - lineStart);
               display.append(" ");
               display.append(command); 
               calculate(Double.parseDouble(value));
               if (command.equals("="))
                  display.append("\n" + result);
               lastCommand = command;
               display.append("\n");
               start = true;
            }
            catch (BadLocationException e)
            {
               e.printStackTrace();
            }
         }
      }
   }

   /**
      Carries out the pending calculation. 
      @param x the value to be accumulated with the prior result.
   */
   public void calculate(double x)
   {
      if (lastCommand.equals("+")) result += x;
      else if (lastCommand.equals("-")) result -= x;
      else if (lastCommand.equals("*")) result *= x;
      else if (lastCommand.equals("/")) result /= x;
      else if (lastCommand.equals("=")) result = x;
   }  
}
