/**
   @version 1.01 2004-08-25
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
   This program demonstrates the transfer of object references within the same virtual machine.
*/
public class LocalTransferTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new LocalTransferFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame contains a panel to edit a cubic curve, a
   panel that can display an arbitrary shape, and copy and 
   paste buttons.
*/
class LocalTransferFrame extends JFrame
{  
   public LocalTransferFrame()
   {  
      setTitle("LocalTransferTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      curvePanel = new CubicCurvePanel();
      curvePanel.setPreferredSize(new Dimension(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT));
      shapePanel = new ShapePanel();

      add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, curvePanel, shapePanel), 
         BorderLayout.CENTER);
      JPanel panel = new JPanel();

      JButton copyButton = new JButton("Copy");
      panel.add(copyButton);
      copyButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               copy();
            }
         });

      JButton pasteButton = new JButton("Paste");
      panel.add(pasteButton);
      pasteButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               paste();
            }
         });

      add(panel, BorderLayout.SOUTH);
   }

   /**
      Copies the current cubic curve to the local clipboard.
   */
   private void copy()
   {  
      LocalSelection selection = new LocalSelection(curvePanel.getShape());
      clipboard.setContents(selection, null);
   }

   /**
      Pastes the shape from the local clipboard into the
      shape panel.
   */
   private void paste()
   {  
      try
      {  
         DataFlavor flavor 
            = new DataFlavor("application/x-java-jvm-local-objectref;class=java.awt.Shape");
         if (clipboard.isDataFlavorAvailable(flavor))
            shapePanel.setShape((Shape) clipboard.getData(flavor));
      }
      catch (ClassNotFoundException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }
      catch (UnsupportedFlavorException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }
      catch (IOException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }
   }

   private CubicCurvePanel curvePanel;
   private ShapePanel shapePanel;
   private Clipboard clipboard = new Clipboard("local");

   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 300;
}


/**
   This panel draws a shape and allows the user to 
   move the points that define it.
*/
class CubicCurvePanel extends JPanel
{  
   public CubicCurvePanel()
   {  
      addMouseListener(new
         MouseAdapter()
         {
            public void mousePressed(MouseEvent event)
            {  
               for (int i = 0; i < p.length; i++)
               {  
                  double x = p[i].getX() - SIZE / 2;
                  double y = p[i].getY() - SIZE / 2;
                  Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
                  if (r.contains(event.getPoint()))
                  {  
                     current = i;
                     return;
                  }
               }
            }

            public void mouseReleased(MouseEvent event)
            {  
               current = -1;
            }
         });

      addMouseMotionListener(new 
         MouseMotionAdapter()
         {
            public void mouseDragged(MouseEvent event)
            {  
               if (current == -1) return;
               p[current] = event.getPoint();
               repaint();
            }
         });

      current = -1;
   }

   public void paintComponent(Graphics g)
   {  
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      for (int i = 0; i < p.length; i++)
      {  
         double x = p[i].getX() - SIZE / 2;
         double y = p[i].getY() - SIZE / 2;
         g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
      }

      g2.draw(getShape());
   }

   /**
      Gets the current cubic curve.
      @return the curve shape
   */
   public Shape getShape()
   {  
      return new CubicCurve2D.Double(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY(), 
         p[2].getX(), p[2].getY(), p[3].getX(), p[3].getY());
   }

   private Point2D[] p =   
   {
      new Point2D.Double(10, 10), 
      new Point2D.Double(10, 100), 
      new Point2D.Double(100, 10), 
      new Point2D.Double(100, 200)
   };
   private static int SIZE = 10;
   private int current;
}

/**
   This panel displays an arbitrary shape.
*/
class ShapePanel extends JPanel
{
   /**
      Set the shape to be displayed in this panel.
      @param aShape any shape
   */
   public void setShape(Shape aShape)
   {
      shape = aShape;
      repaint();
   }

   public void paintComponent(Graphics g)
   {  
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      if (shape != null) g2.draw(shape);
   }

   private Shape shape;
}

/**
   This class is a wrapper for the data transfer of 
   object references that are transferred within the same
   virtual machine.
*/
class LocalSelection implements Transferable 
{
   /**
      Constructs the selection.
      @param o any object
   */   
   LocalSelection(Object o) 
   {
      obj = o;
   }

   public DataFlavor[] getTransferDataFlavors() 
   {
      DataFlavor[] flavors = new DataFlavor[1];
      Class type = obj.getClass();
      String mimeType = "application/x-java-jvm-local-objectref;class=" + type.getName();
      try 
      {
         flavors[0] = new DataFlavor(mimeType);
         return flavors;
      } 
      catch (ClassNotFoundException e) 
      {
         return new DataFlavor[0];
      }
   }

   public boolean isDataFlavorSupported(DataFlavor flavor) 
   {
      return "application".equals(flavor.getPrimaryType()) 
         && "x-java-jvm-local-objectref".equals(flavor.getSubType()) 
         && flavor.getRepresentationClass().isAssignableFrom(obj.getClass());   
   }

   public Object getTransferData(DataFlavor flavor) 
      throws UnsupportedFlavorException
   {
      if (! isDataFlavorSupported(flavor)) 
         throw new UnsupportedFlavorException(flavor);

      return obj;
   }
   
   private Object obj;
}
