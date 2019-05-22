import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

/**
 * This program shows how to write an XML file. It saves a file describing a modern drawing in SVG
 * format.
 * @version 1.10 2004-09-04
 * @author Cay Horstmann
 */
public class XMLWriteTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               XMLWriteFrame frame = new XMLWriteFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame with a component for showing a modern drawing.
 */
class XMLWriteFrame extends JFrame
{
   public XMLWriteFrame()
   {
      setTitle("XMLWriteTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      chooser = new JFileChooser();

      // add component to frame

      comp = new RectangleComponent();
      add(comp);

      // set up menu bar

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu menu = new JMenu("File");
      menuBar.add(menu);

      JMenuItem newItem = new JMenuItem("New");
      menu.add(newItem);
      newItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               comp.newDrawing();
            }
         });

      JMenuItem saveItem = new JMenuItem("Save with DOM/XSLT");
      menu.add(saveItem);
      saveItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               try
               {
                  saveDocument();
               }
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(XMLWriteFrame.this, e.toString());
               }
            }
         });

      JMenuItem saveStAXItem = new JMenuItem("Save with StAX");
      menu.add(saveStAXItem);
      saveStAXItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               try
               {
                  saveStAX();
               }
               catch (Exception e)
               {
                  JOptionPane.showMessageDialog(XMLWriteFrame.this, e.toString());
               }
            }
         });

      JMenuItem exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });
   }

   /**
    * Saves the drawing in SVG format, using DOM/XSLT
    */
   public void saveDocument() throws TransformerException, IOException
   {
      if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
      File f = chooser.getSelectedFile();
      Document doc = comp.buildDocument();
      Transformer t = TransformerFactory.newInstance().newTransformer();
      t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
            "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
      t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD SVG 20000802//EN");
      t.setOutputProperty(OutputKeys.INDENT, "yes");
      t.setOutputProperty(OutputKeys.METHOD, "xml");
      t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(f)));
   }
   
   /**
    * Saves the drawing in SVG format, using StAX
    */
   public void saveStAX() throws FileNotFoundException, XMLStreamException
   {
      if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
      File f = chooser.getSelectedFile();
      XMLOutputFactory factory = XMLOutputFactory.newInstance();
      XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(f));
      comp.writeDocument(writer);
      writer.close();
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;

   private RectangleComponent comp;
   private JFileChooser chooser;
}

/**
 * A component that shows a set of colored rectangles
 */
class RectangleComponent extends JComponent
{
   public RectangleComponent()
   {
      rects = new ArrayList<Rectangle2D>();
      colors = new ArrayList<Color>();
      generator = new Random();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      try
      {
         builder = factory.newDocumentBuilder();
      }
      catch (ParserConfigurationException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Create a new random drawing.
    */
   public void newDrawing()
   {
      int n = 10 + generator.nextInt(20);
      rects.clear();
      colors.clear();
      for (int i = 1; i <= n; i++)
      {
         int x = generator.nextInt(getWidth());
         int y = generator.nextInt(getHeight());
         int width = generator.nextInt(getWidth() - x);
         int height = generator.nextInt(getHeight() - y);
         rects.add(new Rectangle(x, y, width, height));
         int r = generator.nextInt(256);
         int g = generator.nextInt(256);
         int b = generator.nextInt(256);
         colors.add(new Color(r, g, b));
      }
      repaint();
   }

   public void paintComponent(Graphics g)
   {
      if (rects.size() == 0) newDrawing();
      Graphics2D g2 = (Graphics2D) g;

      // draw all rectangles
      for (int i = 0; i < rects.size(); i++)
      {
         g2.setPaint(colors.get(i));
         g2.fill(rects.get(i));
      }
   }

   /**
    * Creates an SVG document of the current drawing.
    * @return the DOM tree of the SVG document
    */
   public Document buildDocument()
   {
      Document doc = builder.newDocument();
      Element svgElement = doc.createElement("svg");
      doc.appendChild(svgElement);
      svgElement.setAttribute("width", "" + getWidth());
      svgElement.setAttribute("height", "" + getHeight());
      for (int i = 0; i < rects.size(); i++)
      {
         Color c = colors.get(i);
         Rectangle2D r = rects.get(i);
         Element rectElement = doc.createElement("rect");
         rectElement.setAttribute("x", "" + r.getX());
         rectElement.setAttribute("y", "" + r.getY());
         rectElement.setAttribute("width", "" + r.getWidth());
         rectElement.setAttribute("height", "" + r.getHeight());
         rectElement.setAttribute("fill", colorToString(c));
         svgElement.appendChild(rectElement);
      }
      return doc;
   }

   /**
    * Writers an SVG document of the current drawing.
    * @param writer the document destination
    */
   public void writeDocument(XMLStreamWriter writer) throws XMLStreamException
   {
      writer.writeStartDocument();
      writer.writeDTD("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20000802//EN\" " 
            + "\"http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd\">");
      writer.writeStartElement("svg");
      writer.writeAttribute("width", "" + getWidth());
      writer.writeAttribute("height", "" + getHeight());
      for (int i = 0; i < rects.size(); i++)
      {
         Color c = colors.get(i);
         Rectangle2D r = rects.get(i);
         writer.writeEmptyElement("rect");
         writer.writeAttribute("x", "" + r.getX());
         writer.writeAttribute("y", "" + r.getY());
         writer.writeAttribute("width", "" + r.getWidth());
         writer.writeAttribute("height", "" + r.getHeight());
         writer.writeAttribute("fill", colorToString(c));         
      }
      writer.writeEndDocument(); // closes svg element
   }  
   
   /**
    * Converts a color to a hex value.
    * @param c a color
    * @return a string of the form #rrggbb
    */
   private static String colorToString(Color c)
   {
      StringBuffer buffer = new StringBuffer();
      buffer.append(Integer.toHexString(c.getRGB() & 0xFFFFFF));
      while (buffer.length() < 6)
         buffer.insert(0, '0');
      buffer.insert(0, '#');
      return buffer.toString();
   }

   private ArrayList<Rectangle2D> rects;
   private ArrayList<Color> colors;
   private Random generator;
   private DocumentBuilder builder;
}