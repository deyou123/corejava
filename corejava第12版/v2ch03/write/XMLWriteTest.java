package write;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

/**
 * This program shows how to write an XML file. It produces modern art in SVG 
 * format.
 * @version 1.13 2021-09-03
 * @author Cay Horstmann
 */
public class XMLWriteTest
{
   public static void main(String[] args) throws Exception
   {
      Document doc = newDrawing(600, 400);
      writeDocument(doc, "drawing1.svg");
      writeNewDrawing(600, 400, "drawing2.svg");
   }

   private static Random generator = new Random(); 
   
   /**
    * Creates a new random drawing.
    * @param drawingWidth the width of the drawing in pixels
    * @param drawingHeight the width of the drawing in pixels
    * @return the DOM tree of the SVG document
    */
   public static Document newDrawing(int drawingWidth, int drawingHeight) 
         throws ParserConfigurationException
   {          
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();;
      String namespace = "http://www.w3.org/2000/svg";
      Document doc = builder.newDocument();
      Element svgElement = doc.createElementNS(namespace, "svg");
      doc.appendChild(svgElement);
      svgElement.setAttribute("width", "" + drawingWidth);
      svgElement.setAttribute("height", "" + drawingHeight);
      int n = 10 + generator.nextInt(20);
      for (int i = 1; i <= n; i++)
      {
         int x = generator.nextInt(drawingWidth);
         int y = generator.nextInt(drawingHeight);
         int width = generator.nextInt(drawingWidth - x);
         int height = generator.nextInt(drawingHeight - y);
         int r = generator.nextInt(256);
         int g = generator.nextInt(256);
         int b = generator.nextInt(256);
         
         Element rectElement = doc.createElementNS(namespace, "rect");
         rectElement.setAttribute("x", "" + x);
         rectElement.setAttribute("y", "" + y);
         rectElement.setAttribute("width", "" + width);
         rectElement.setAttribute("height", "" + height);
         rectElement.setAttribute("fill", 
            "#%02x%02x%02x".formatted(r, g, b));
         svgElement.appendChild(rectElement);
      }
      return doc;
   }
   
   /**
    * Saves a document using DOM/XSLT.
    * @param doc the document to be written
    * @param filename the name of the destination file
    */
   public static void writeDocument(Document doc, String filename) 
         throws TransformerException, IOException 
   {
      Transformer t = TransformerFactory.newInstance().newTransformer();
      t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
            "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
      t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, 
            "-//W3C//DTD SVG 20000802//EN");
      t.setOutputProperty(OutputKeys.INDENT, "yes");
      t.setOutputProperty(OutputKeys.METHOD, "xml");
      t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      t.transform(new DOMSource(doc), new StreamResult(
            Files.newOutputStream(Path.of(filename))));
   }
   
   /**
    * Uses StAX to write an SVG document with a random drawing.
    * @param drawingWidth the width of the drawing in pixels
    * @param drawingHeight the width of the drawing in pixels
    * @param filename the name of the destination file
    */
   public static void writeNewDrawing(int drawingWidth, int drawingHeight, 
         String filename) throws XMLStreamException, IOException
   {      
      XMLOutputFactory factory = XMLOutputFactory.newInstance();
      XMLStreamWriter writer = factory.createXMLStreamWriter(
            Files.newOutputStream(Path.of(filename)));      
      writer.writeStartDocument();
      writer.writeDTD("""
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 20000802//EN" 
  "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd">
""");
      writer.writeStartElement("svg");
      writer.writeDefaultNamespace("http://www.w3.org/2000/svg");
      writer.writeAttribute("width", "" + drawingWidth);
      writer.writeAttribute("height", "" + drawingHeight);
      int n = 10 + generator.nextInt(20);      
      for (int i = 1; i <= n; i++)
      {
         int x = generator.nextInt(drawingWidth);
         int y = generator.nextInt(drawingHeight);
         int width = generator.nextInt(drawingWidth - x);
         int height = generator.nextInt(drawingHeight - y);
         int r = generator.nextInt(256);
         int g = generator.nextInt(256);
         int b = generator.nextInt(256);
         writer.writeEmptyElement("rect");
         writer.writeAttribute("x", "" + x);
         writer.writeAttribute("y", "" + y);
         writer.writeAttribute("width", "" + width);
         writer.writeAttribute("height", "" + height);
         writer.writeAttribute("fill", "#%02x%02x%02x".formatted(r, g, b));         
      }
      writer.writeEndDocument(); // closes svg element
   }  
}
