package book;

import java.awt.*;
import java.awt.print.*;

import javax.print.attribute.*;
import javax.swing.*;

/**
 * This frame has a text field for the banner text and buttons for printing, page setup, and print
 * preview.
 */
public class BookTestFrame extends JFrame
{
   private JTextField text;
   private PageFormat pageFormat;
   private PrintRequestAttributeSet attributes;

   public BookTestFrame()
   {
      text = new JTextField();
      add(text, BorderLayout.NORTH);

      attributes = new HashPrintRequestAttributeSet();

      JPanel buttonPanel = new JPanel();

      JButton printButton = new JButton("Print");
      buttonPanel.add(printButton);
      printButton.addActionListener(event ->
         {
            try
            {
               PrinterJob job = PrinterJob.getPrinterJob();
               job.setPageable(makeBook());
               if (job.printDialog(attributes))
               {
                  job.print(attributes);
               }
            }
            catch (PrinterException e)
            {
               JOptionPane.showMessageDialog(BookTestFrame.this, e);
            }
         });

      JButton pageSetupButton = new JButton("Page setup");
      buttonPanel.add(pageSetupButton);
      pageSetupButton.addActionListener(event ->
         {
            PrinterJob job = PrinterJob.getPrinterJob();
            pageFormat = job.pageDialog(attributes);
         });

      JButton printPreviewButton = new JButton("Print preview");
      buttonPanel.add(printPreviewButton);
      printPreviewButton.addActionListener(event ->
         {
            PrintPreviewDialog dialog = new PrintPreviewDialog(makeBook());
            dialog.setVisible(true);
         });

      add(buttonPanel, BorderLayout.SOUTH);
      pack();
   }

   /**
    * Makes a book that contains a cover page and the pages for the banner.
    */
   public Book makeBook()
   {
      if (pageFormat == null)
      {
         PrinterJob job = PrinterJob.getPrinterJob();
         pageFormat = job.defaultPage();
      }
      Book book = new Book();
      String message = text.getText();
      Banner banner = new Banner(message);
      int pageCount = banner.getPageCount((Graphics2D) getGraphics(), pageFormat);
      book.append(new CoverPage(message + " (" + pageCount + " pages)"), pageFormat);
      book.append(banner, pageFormat, pageCount);
      return book;
   }
}
