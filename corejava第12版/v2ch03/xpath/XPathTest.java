package xpath;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import javax.xml.catalog.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * This program evaluates XPath expressions.
 * @version 1.1 2018-04-06
 * @author Cay Horstmann
 */
public class XPathTest
{
   public static void main(String[] args) throws Exception
   {            
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();

      // Avoid a delay in parsing an XHTML file--see the first note in 
      // Section 3.3.1
      builder.setEntityResolver(CatalogManager.catalogResolver(
            CatalogFeatures.defaults(), 
            Path.of("xpath/catalog.xml").toAbsolutePath().toUri()));      
      
      XPathFactory xpfactory = XPathFactory.newInstance();
      XPath path = xpfactory.newXPath();
      try (var in = new Scanner(System.in))
      {
         String filename;
         if (args.length == 0)
         {
            System.out.print("Input file: ");
            filename = in.nextLine();
         }
         else
            filename = args[0];
      
         Document doc = builder.parse(filename);
         boolean done = false;
         while (!done)
         {
            System.out.print("XPath expression (empty line to exit): " );
            String expression = in.nextLine();
            if (expression.strip().isEmpty()) done = true;
            else
            {
               try
               {
                  XPathEvaluationResult<?> result 
                        = path.evaluateExpression(expression, doc);
                  if (result.type() == XPathEvaluationResult.XPathResultType.NODESET)
                  {
                     for (Node n : (XPathNodes) result.value())
                        System.out.println(description(n));
                  }
                  else if (result.type() == XPathEvaluationResult.XPathResultType.NODE)
                     System.out.println((Node) result.value());
                  else
                     System.out.println(result.value());
               } 
               catch (XPathExpressionException e)
               {
                  System.out.println(e.getMessage());
               }               
            }   
         }
      }
   }

   public static String description(Node n)
   {
      if (n instanceof Element) return "Element " + n.getNodeName();
      else if (n instanceof Attr) return "Attribute " + n;
      else return n.toString();
   }
}
