/**
   @version 1.01 2004-05-08
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.jnlp.*;

/**
   A calculator with a calculation history that can be 
   deployed as a Java Web Start application.
*/
public class WebStartCalculator
{
   public static void main(String[] args)
   {  
      CalculatorFrame frame = new CalculatorFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame with a calculator panel and a menu to load and
   save the calculator history.
*/
class CalculatorFrame extends JFrame
{
   public CalculatorFrame()
   {
      setTitle();
      panel = new CalculatorPanel();
      add(panel);

      JMenu fileMenu = new JMenu("File");

      JMenuItem openItem = fileMenu.add("Open");
      openItem.addActionListener(new        
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               open();
            }
         });

      JMenuItem saveItem = fileMenu.add("Save");
      saveItem.addActionListener(new        
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               save();
            }
         });
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);
      setJMenuBar(menuBar);
      
      pack();
   }
   
   /**
      Gets the title from the persistent store or 
      asks the user for the title if there is no prior entry.
   */
   public void setTitle()
   {
      try 
      { 
         String title = null;

         BasicService basic = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService"); 
         URL codeBase = basic.getCodeBase();

         PersistenceService service 
            = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService"); 
         URL key = new URL(codeBase, "title");

         try
         {
            FileContents contents = service.get(key);
            InputStream in = contents.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            title = reader.readLine();
         }
         catch (FileNotFoundException e)
         {
            title = JOptionPane.showInputDialog("Please supply a frame title:");
            if (title == null) return;
            
            service.create(key, 100);
            FileContents contents = service.get(key);
            OutputStream out = contents.getOutputStream(true);
            PrintStream printOut = new PrintStream(out);
            printOut.print(title);               
         }
         setTitle(title);
      } 
      catch (UnavailableServiceException e) 
      { 
         JOptionPane.showMessageDialog(this, e);
      }
      catch (MalformedURLException e) 
      { 
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e) 
      { 
         JOptionPane.showMessageDialog(this, e);
      }      
   }

   /**
      Opens a history file and updates the display.
   */
   public void open()
   {
      try 
      {
         FileOpenService service 
            = (FileOpenService) ServiceManager.lookup("javax.jnlp.FileOpenService"); 
         FileContents contents = service.openFileDialog(".", new String[] { "txt" });

         JOptionPane.showMessageDialog(this, contents.getName());
         if (contents != null)
         {
            InputStream in = contents.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null)
            {
               panel.append(line);                  
               panel.append("\n");
            }
         }
      } 
      catch (UnavailableServiceException e) 
      { 
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e) 
      { 
         JOptionPane.showMessageDialog(this, e);
      }
   }

   /**
      Saves the calculator history to a file.
   */
   public void save()
   {
      try 
      { 
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         PrintStream printOut = new PrintStream(out);
         printOut.print(panel.getText());
         InputStream data = new ByteArrayInputStream(out.toByteArray());                  
         FileSaveService service 
            = (FileSaveService) ServiceManager.lookup("javax.jnlp.FileSaveService"); 
         service.saveFileDialog(".", new String[] { "txt" }, data, "calc.txt");
      } 
      catch (UnavailableServiceException e) 
      { 
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e) 
      { 
         JOptionPane.showMessageDialog(this, e);
      }
   }

   private CalculatorPanel panel;
}


/**
   A panel with calculator buttons and a result display.
*/
class CalculatorPanel extends JPanel
{  
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
   
   private JTextArea display;
   private JPanel panel;
   private double result;
   private String lastCommand;
   private boolean start;
}


