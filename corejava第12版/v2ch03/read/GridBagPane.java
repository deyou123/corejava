package read;

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * This panel uses an XML file to describe its components and their grid bag layout positions.
 */
public class GridBagPane extends JPanel
{
   private GridBagConstraints constraints;

   /**
    * Constructs a grid bag pane.
    * @param filename the name of the XML file that describes the pane's components and their
    * positions
    */
   public GridBagPane(File file)
   {
      setLayout(new GridBagLayout());
      constraints = new GridBagConstraints();

      try
      {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setValidating(true);

         if (file.toString().contains("-schema"))
         {
            factory.setNamespaceAware(true);
            final String JAXP_SCHEMA_LANGUAGE = 
               "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
            final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
         }

         factory.setIgnoringElementContentWhitespace(true);

         DocumentBuilder builder = factory.newDocumentBuilder();
         Document doc = builder.parse(file);
         parseGridbag(doc.getDocumentElement());         
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Gets a component with a given name
    * @param name a component name
    * @return the component with the given name, or null if no component in this grid bag pane has
    * the given name
    */
   public Component get(String name)
   {
      Component[] components = getComponents();
      for (int i = 0; i < components.length; i++)
      {
         if (components[i].getName().equals(name)) return components[i];
      }
      return null;
   }

   /**
    * Parses a gridbag element.
    * @param elem a gridbag element
    */
   private void parseGridbag(Element elem)
   {
      NodeList rows = elem.getChildNodes();
      for (int i = 0; i < rows.getLength(); i++)
      {
         Element row = (Element) rows.item(i);
         NodeList cells = row.getChildNodes();
         for (int j = 0; j < cells.getLength(); j++)
         {
            Element cell = (Element) cells.item(j);
            parseCell(cell, i, j);
         }
      }
   }

   /**
    * Parses a cell element.
    * @param elem a cell element
    * @param r the row of the cell
    * @param c the column of the cell
    */
   private void parseCell(Element elem, int r, int c)
   {
      // get attributes

      String value = elem.getAttribute("gridx");
      if (value.length() == 0) // use default
      {
         if (c == 0) constraints.gridx = 0;
         else constraints.gridx += constraints.gridwidth;
      }
      else constraints.gridx = Integer.parseInt(value);

      value = elem.getAttribute("gridy");
      if (value.length() == 0) // use default
      constraints.gridy = r;
      else constraints.gridy = Integer.parseInt(value);

      constraints.gridwidth = Integer.parseInt(elem.getAttribute("gridwidth"));
      constraints.gridheight = Integer.parseInt(elem.getAttribute("gridheight"));
      constraints.weightx = Integer.parseInt(elem.getAttribute("weightx"));
      constraints.weighty = Integer.parseInt(elem.getAttribute("weighty"));
      constraints.ipadx = Integer.parseInt(elem.getAttribute("ipadx"));
      constraints.ipady = Integer.parseInt(elem.getAttribute("ipady"));

      // use reflection to get integer values of static fields
      Class<GridBagConstraints> cl = GridBagConstraints.class;

      try
      {
         String name = elem.getAttribute("fill");
         Field f = cl.getField(name);
         constraints.fill = f.getInt(cl);

         name = elem.getAttribute("anchor");
         f = cl.getField(name);
         constraints.anchor = f.getInt(cl);
      }
      catch (Exception e) // the reflection methods can throw various exceptions
      {
         e.printStackTrace();
      }

      Component comp = (Component) parseBean((Element) elem.getFirstChild());
      add(comp, constraints);
   }

   /**
    * Parses a bean element.
    * @param elem a bean element
    */
   private Object parseBean(Element elem)
   {
      try
      {
         NodeList children = elem.getChildNodes();
         Element classElement = (Element) children.item(0);
         String className = ((Text) classElement.getFirstChild()).getData();

         Class<?> cl = Class.forName(className);

         Object obj = cl.getDeclaredConstructor().newInstance();

         if (obj instanceof Component c) c.setName(elem.getAttribute("id"));

         for (int i = 1; i < children.getLength(); i++)
         {
            Node propertyElement = children.item(i);
            Element nameElement = (Element) propertyElement.getFirstChild();
            String propertyName = ((Text) nameElement.getFirstChild()).getData();

            Element valueElement = (Element) propertyElement.getLastChild();
            Object value = parseValue(valueElement);
            BeanInfo beanInfo = Introspector.getBeanInfo(cl);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            boolean done = false;
            for (int j = 0; !done && j < descriptors.length; j++)
            {
               if (descriptors[j].getName().equals(propertyName))
               {
                  descriptors[j].getWriteMethod().invoke(obj, value);
                  done = true;
               }
            }
         }
         return obj;
      }
      catch (Exception e) // the reflection methods can throw various exceptions
      {
         e.printStackTrace();
         return null;
      }
   }

   /**
    * Parses a value element.
    * @param elem a value element
    */
   private Object parseValue(Element elem)
   {
      Element child = (Element) elem.getFirstChild();
      if (child.getTagName().equals("bean")) return parseBean(child);
      String text = ((Text) child.getFirstChild()).getData();
      if (child.getTagName().equals("int")) return Integer.valueOf(text);
      else if (child.getTagName().equals("boolean")) return Boolean.valueOf(text);
      else if (child.getTagName().equals("string")) return text;
      else return null;
   }
}
