/**
   @version 1.02 2004-08-24
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**
   This program displays the effects of various transformations.
*/
public class TransformTest
{  
   public static void main(String[] args)
   {  
      JFrame frame = new TransformTestFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame contains radio buttons to choose a transformations
   and a panel to display the effect of the chosen 
   transformation.
*/
class TransformTestFrame extends JFrame
{  
   public TransformTestFrame()
   {  
      setTitle("TransformTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      canvas = new TransformPanel();
      add(canvas, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      ButtonGroup group = new ButtonGroup();

      JRadioButton rotateButton = new JRadioButton("Rotate", true);
      buttonPanel.add(rotateButton);
      group.add(rotateButton);
      rotateButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               canvas.setRotate();
            }
         });

      JRadioButton translateButton = new JRadioButton("Translate", false);
      buttonPanel.add(translateButton);
      group.add(translateButton);
      translateButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               canvas.setTranslate();
            }
         });

      JRadioButton scaleButton = new JRadioButton("Scale", false);
      buttonPanel.add(scaleButton);
      group.add(scaleButton);
      scaleButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               canvas.setScale();
            }
         });

      JRadioButton shearButton = new JRadioButton("Shear", false);
      buttonPanel.add(shearButton);
      group.add(shearButton);
      shearButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               canvas.setShear();
            }
         });

      add(buttonPanel, BorderLayout.NORTH);
   }

   private TransformPanel canvas;
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 300;
}

/**
   This panel displays a square and its transformed image
   under a transformation.
*/
class TransformPanel extends JPanel
{  
   public TransformPanel()
   {  
      square = new Rectangle2D.Double(-50, -50, 100, 100);
      t = new AffineTransform();
      setRotate();
   }

   public void paintComponent(Graphics g)
   {  
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.translate(getWidth() / 2, getHeight() / 2);
      g2.setPaint(Color.gray);
      g2.draw(square);
      g2.transform(t);
      // we don't use setTransform because we want to compose with the current translation
      g2.setPaint(Color.black);
      g2.draw(square);
   }

   /**
      Set the transformation to a rotation.
   */
   public void setRotate()
   {  
      t.setToRotation(Math.toRadians(30));
      repaint();
   }

   /**
      Set the transformation to a translation.
   */
   public void setTranslate()
   {  
      t.setToTranslation(20, 15);
      repaint();
   }

   /**
      Set the transformation to a scale transformation.
   */
   public void setScale()
   {  
      t.setToScale(2.0, 1.5);
      repaint();
   }

   /**
      Set the transformation to a shear transformation.
   */
   public void setShear()
   {  
      t.setToShear(-0.2, 0);
      repaint();
   }

   private Rectangle2D square;
   private AffineTransform t;
}

