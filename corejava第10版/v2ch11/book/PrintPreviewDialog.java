package book;

import java.awt.*;
import java.awt.print.*;

import javax.swing.*;

/**
 * This class implements a generic print preview dialog.
 */
public class PrintPreviewDialog extends JDialog
{
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 300;

   private PrintPreviewCanvas canvas;

   /**
    * Constructs a print preview dialog.
    * @param p a Printable
    * @param pf the page format
    * @param pages the number of pages in p
    */
   public PrintPreviewDialog(Printable p, PageFormat pf, int pages)
   {
      Book book = new Book();
      book.append(p, pf, pages);
      layoutUI(book);
   }

   /**
    * Constructs a print preview dialog.
    * @param b a Book
    */
   public PrintPreviewDialog(Book b)
   {
      layoutUI(b);
   }

   /**
    * Lays out the UI of the dialog.
    * @param book the book to be previewed
    */
   public void layoutUI(Book book)
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      canvas = new PrintPreviewCanvas(book);
      add(canvas, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();

      JButton nextButton = new JButton("Next");
      buttonPanel.add(nextButton);
      nextButton.addActionListener(event -> canvas.flipPage(1));

      JButton previousButton = new JButton("Previous");
      buttonPanel.add(previousButton);
      previousButton.addActionListener(event -> canvas.flipPage(-1));

      JButton closeButton = new JButton("Close");
      buttonPanel.add(closeButton);
      closeButton.addActionListener(event -> setVisible(false));

      add(buttonPanel, BorderLayout.SOUTH);
   }
}