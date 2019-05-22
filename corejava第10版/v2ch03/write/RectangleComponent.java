package write;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.stream.*;
import org.w3c.dom.*;

/**
 * A component that shows a set of colored rectangles
 */
public class RectangleComponent extends JComponent
{
   private static final Dimension PREFERRED_SIZE = new Dimension(300, 200);   

   private java.util.List<Rectangle2D> rects;
   private java.util.List<Color> colors;
   private Random generator;
   private DocumentBuilder builder;

   public RectangleComponent()
   {
      rects = new ArrayList<>();
      colors = new ArrayList<>();
      generator = new Random();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
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
      String namespace = "http://www.w3.org/2000/svg";
      Document doc = builder.newDocument();
      Element svgElement = doc.createElementNS(namespace, "svg");
      doc.appendChild(svgElement);
      svgElement.setAttribute("width", "" + getWidth());
      svgElement.setAttribute("height", "" + getHeight());
      for (int i = 0; i < rects.size(); i++)
      {
         Color c = colors.get(i);
         Rectangle2D r = rects.get(i);
         Element rectElement = doc.createElementNS(namespace, "rect");
         rectElement.setAttribute("x", "" + r.getX());
         rectElement.setAttribute("y", "" + r.getY());
         rectElement.setAttribute("width", "" + r.getWidth());
         rectElement.setAttribute("height", "" + r.getHeight());
         rectElement.setAttribute("fill", String.format("#%06x",
               c.getRGB() & 0xFFFFFF));
         svgElement.appendChild(rectElement);
      }
      return doc;
   }

   /**
    * Writes an SVG document of the current drawing.
    * @param writer the document destination
    */
   public void writeDocument(XMLStreamWriter writer) throws XMLStreamException
   {
      writer.writeStartDocument();
      writer.writeDTD("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20000802//EN\" " 
            + "\"http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd\">");
      writer.writeStartElement("svg");
      writer.writeDefaultNamespace("http://www.w3.org/2000/svg");
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
         writer.writeAttribute("fill", String.format("#%06x",
               c.getRGB() & 0xFFFFFF));         
      }
      writer.writeEndDocument(); // closes svg element
   }  

   public Dimension getPreferredSize() { return PREFERRED_SIZE; }
}
