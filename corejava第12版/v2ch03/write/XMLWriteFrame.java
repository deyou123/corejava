package write;

import java.io.*;
import java.nio.file.*;

import javax.swing.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

/**
 * A frame with a component for showing a modern drawing.
 */
public class XMLWriteFrame extends JFrame
{
   private RectangleComponent comp;
   private JFileChooser chooser;

   public XMLWriteFrame()
   {
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
      newItem.addActionListener(event -> comp.newDrawing());
      
      JMenuItem saveItem = new JMenuItem("Save with DOM/XSLT");
      menu.add(saveItem);
      saveItem.addActionListener(event -> saveDocument()); 

      JMenuItem saveStAXItem = new JMenuItem("Save with StAX");
      menu.add(saveStAXItem);
      saveStAXItem.addActionListener(event -> saveStAX());
      
      JMenuItem exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(event -> System.exit(0));
      pack();
   }

   /**
    * Saves the drawing in SVG format, using DOM/XSLT
    */
   public void saveDocument() 
   {
      try 
      {
         if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
         File file = chooser.getSelectedFile();
         Document doc = comp.buildDocument();
         Transformer t = TransformerFactory.newInstance().newTransformer();
         t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
               "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
         t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD SVG 20000802//EN");
         t.setOutputProperty(OutputKeys.INDENT, "yes");
         t.setOutputProperty(OutputKeys.METHOD, "xml");
         t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(file.toPath())));
      }
      catch (TransformerException | IOException e) 
      { 
         e.printStackTrace();
      }      
   }
   
   /**
    * Saves the drawing in SVG format, using StAX
    */
   public void saveStAX()
   {
      if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
      File file = chooser.getSelectedFile();
      XMLOutputFactory factory = XMLOutputFactory.newInstance();
      try
      {
         XMLStreamWriter writer = factory.createXMLStreamWriter(Files.newOutputStream(file.toPath()));
         try
         {
            comp.writeDocument(writer);
         }
         finally
         {
            writer.close(); // Not autocloseable
         }
      }         
      catch (XMLStreamException | IOException e) 
      { 
         e.printStackTrace();
      }      
   }
}
