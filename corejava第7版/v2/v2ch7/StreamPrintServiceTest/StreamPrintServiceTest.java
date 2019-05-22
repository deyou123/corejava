/**
   @version 1.01 2004-08-24
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.io.*;
import java.util.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.swing.*;


/**
   This program demonstrates the use of a stream print service.
   It prints a 2D graphic to a PostScript file.
*/
public class StreamPrintServiceTest
{
   public static void main(String[] args)
   {  
      JFrame frame = new StreamPrintServiceFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame shows a panel with 2D graphics and buttons
   to print the graphics to a PostScript file and to set up 
   the page format.
*/
class StreamPrintServiceFrame extends JFrame
{  
   public StreamPrintServiceFrame()
   {  
      setTitle("StreamPrintServiceTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      canvas = new PrintPanel();
      add(canvas, BorderLayout.CENTER);

      attributes = new HashPrintRequestAttributeSet();

      JPanel buttonPanel = new JPanel();
      JButton printButton = new JButton("Print");
      buttonPanel.add(printButton);
      printButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               String fileName = getFile();
               if (fileName != null)
                  printPostScript(fileName);
            }
         });

      JButton pageSetupButton = new JButton("Page setup");
      buttonPanel.add(pageSetupButton);
      pageSetupButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               PrinterJob job = PrinterJob.getPrinterJob();
               job.pageDialog(attributes);
            }
         });

      add(buttonPanel, BorderLayout.NORTH);
   }

   /**
      Allows the user to select a PostScript file.
      @return the file name, or null if the user didn't
      select a file.
   */
   public String getFile()
   {
      // set up file chooser
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));
      
      // accept all files ending with .ps
      chooser.setFileFilter(new
         javax.swing.filechooser.FileFilter()
         {
            public boolean accept(File f)
            {
               return f.getName().toLowerCase().endsWith(".ps") || f.isDirectory();
            }

            public String getDescription() { return "PostScript Files"; }
         });

      // show file chooser dialog
      int r = chooser.showSaveDialog(this);
      
      // if file accepted, return path
      if(r == JFileChooser.APPROVE_OPTION)
         return chooser.getSelectedFile().getPath();
      else
         return null;
   }

   /**
      Prints the 2D graphic to a PostScript file.
      @param fileName the name of the PostScript file
   */
   public void printPostScript(String fileName)
   {
      try
      {
         DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
         String mimeType = "application/postscript";
         StreamPrintServiceFactory[] factories = 
            StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor, mimeType);

         FileOutputStream out = new FileOutputStream(fileName);
         if (factories.length == 0) return;
         StreamPrintService service = factories[0].getPrintService(out);

         Doc doc = new SimpleDoc(canvas, flavor, null);
         DocPrintJob job = service.createPrintJob();
         job.print(doc, attributes);
      }
      catch (FileNotFoundException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (PrintException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }

   }

   private PrintPanel canvas;
   private PrintRequestAttributeSet attributes;

   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 300;
}

/**
   This panel generates a 2D graphics image for screen display
   and printing.
*/
class PrintPanel extends JPanel implements Printable
{  
   public void paintComponent(Graphics g)
   {  
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      drawPage(g2);
   }

   public int print(Graphics g, PageFormat pf, int page)
      throws PrinterException
   {  
      if (page >= 1) return Printable.NO_SUCH_PAGE;
      Graphics2D g2 = (Graphics2D) g;
      g2.translate(pf.getImageableX(), pf.getImageableY());
      g2.draw(new Rectangle2D.Double(0, 0, pf.getImageableWidth(), pf.getImageableHeight()));

      drawPage(g2);
      return Printable.PAGE_EXISTS;
   }

   /**
      This method draws the page both on the screen and the
      printer graphics context.
      @param g2 the graphics context
   */
   public void drawPage(Graphics2D g2)
   {  
      FontRenderContext context = g2.getFontRenderContext();
      Font f = new Font("Serif", Font.PLAIN, 72);
      GeneralPath clipShape = new GeneralPath();

      TextLayout layout = new TextLayout("Hello", f, context);
      AffineTransform transform = AffineTransform.getTranslateInstance(0, 72);
      Shape outline = layout.getOutline(transform);
      clipShape.append(outline, false);

      layout = new TextLayout("World", f, context);
      transform = AffineTransform.getTranslateInstance(0, 144);
      outline = layout.getOutline(transform);
      clipShape.append(outline, false);

      g2.draw(clipShape);
      g2.clip(clipShape);

      final int NLINES =50;
      Point2D p = new Point2D.Double(0, 0);
      for (int i = 0; i < NLINES; i++)
      {  
         double x = (2 * getWidth() * i) / NLINES;
         double y = (2 * getHeight() * (NLINES - 1 - i))
            / NLINES;
         Point2D q = new Point2D.Double(x, y);
         g2.draw(new Line2D.Double(p, q));
      }
   }
}
