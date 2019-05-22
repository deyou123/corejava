package print;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.print.attribute.*;
import javax.swing.*;

/**
 * This frame shows a panel with 2D graphics and buttons to print the graphics and to set up the
 * page format.
 */
public class PrintTestFrame extends JFrame
{
   private PrintComponent canvas;
   private PrintRequestAttributeSet attributes;

   public PrintTestFrame()
   {
      canvas = new PrintComponent();
      add(canvas, BorderLayout.CENTER);

      attributes = new HashPrintRequestAttributeSet();

      JPanel buttonPanel = new JPanel();
      JButton printButton = new JButton("Print");
      buttonPanel.add(printButton);
      printButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               try
               {
                  PrinterJob job = PrinterJob.getPrinterJob();
                  job.setPrintable(canvas);
                  if (job.printDialog(attributes)) job.print(attributes);
               }
               catch (PrinterException e)
               {
                  JOptionPane.showMessageDialog(PrintTestFrame.this, e);
               }
            }
         });

      JButton pageSetupButton = new JButton("Page setup");
      buttonPanel.add(pageSetupButton);
      pageSetupButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               PrinterJob job = PrinterJob.getPrinterJob();
               job.pageDialog(attributes);
            }
         });

      add(buttonPanel, BorderLayout.NORTH);
      pack();
   }
}