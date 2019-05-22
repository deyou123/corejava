import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * This program demonstrates the Porter-Duff composition rules.
 * @version 1.03 2007-08-16
 * @author Cay Horstmann
 */
public class CompositeTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new CompositeTestFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame contains a combo box to choose a composition rule, a slider to change the source alpha
 * channel, and a component that shows the composition.
 */
class CompositeTestFrame extends JFrame
{
   public CompositeTestFrame()
   {
      setTitle("CompositeTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      canvas = new CompositeComponent();
      add(canvas, BorderLayout.CENTER);

      ruleCombo = new JComboBox(new Object[] { new Rule("CLEAR", "  ", "  "),
            new Rule("SRC", " S", " S"), new Rule("DST", "  ", "DD"),
            new Rule("SRC_OVER", " S", "DS"), new Rule("DST_OVER", " S", "DD"),
            new Rule("SRC_IN", "  ", " S"), new Rule("SRC_OUT", " S", "  "),
            new Rule("DST_IN", "  ", " D"), new Rule("DST_OUT", "  ", "D "),
            new Rule("SRC_ATOP", "  ", "DS"), new Rule("DST_ATOP", " S", " D"),
            new Rule("XOR", " S", "D "), });
      ruleCombo.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               Rule r = (Rule) ruleCombo.getSelectedItem();
               canvas.setRule(r.getValue());
               explanation.setText(r.getExplanation());
            }
         });

      alphaSlider = new JSlider(0, 100, 75);
      alphaSlider.addChangeListener(new ChangeListener()
         {
            public void stateChanged(ChangeEvent event)
            {
               canvas.setAlpha(alphaSlider.getValue());
            }
         });
      JPanel panel = new JPanel();
      panel.add(ruleCombo);
      panel.add(new JLabel("Alpha"));
      panel.add(alphaSlider);
      add(panel, BorderLayout.NORTH);

      explanation = new JTextField();
      add(explanation, BorderLayout.SOUTH);

      canvas.setAlpha(alphaSlider.getValue());
      Rule r = (Rule) ruleCombo.getSelectedItem();
      canvas.setRule(r.getValue());
      explanation.setText(r.getExplanation());
   }

   private CompositeComponent canvas;
   private JComboBox ruleCombo;
   private JSlider alphaSlider;
   private JTextField explanation;
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 400;
}

/**
 * This class describes a Porter-Duff rule.
 */
class Rule
{
   /**
    * Constructs a Porter-Duff rule
    * @param n the rule name
    * @param pd1 the first row of the Porter-Duff square
    * @param pd2 the second row of the Porter-Duff square
    */
   public Rule(String n, String pd1, String pd2)
   {
      name = n;
      porterDuff1 = pd1;
      porterDuff2 = pd2;
   }

   /**
    * Gets an explanation of the behavior of this rule.
    * @return the explanation
    */
   public String getExplanation()
   {
      StringBuilder r = new StringBuilder("Source ");
      if (porterDuff2.equals("  ")) r.append("clears");
      if (porterDuff2.equals(" S")) r.append("overwrites");
      if (porterDuff2.equals("DS")) r.append("blends with");
      if (porterDuff2.equals(" D")) r.append("alpha modifies");
      if (porterDuff2.equals("D ")) r.append("alpha complement modifies");
      if (porterDuff2.equals("DD")) r.append("does not affect");
      r.append(" destination");
      if (porterDuff1.equals(" S")) r.append(" and overwrites empty pixels");
      r.append(".");
      return r.toString();
   }

   public String toString()
   {
      return name;
   }

   /**
    * Gets the value of this rule in the AlphaComposite class
    * @return the AlphaComposite constant value, or -1 if there is no matching constant.
    */
   public int getValue()
   {
      try
      {
         return (Integer) AlphaComposite.class.getField(name).get(null);
      }
      catch (Exception e)
      {
         return -1;
      }
   }

   private String name;
   private String porterDuff1;
   private String porterDuff2;
}

/**
 * This component draws two shapes, composed with a composition rule.
 */
class CompositeComponent extends JComponent
{
   public CompositeComponent()
   {
      shape1 = new Ellipse2D.Double(100, 100, 150, 100);
      shape2 = new Rectangle2D.Double(150, 150, 150, 100);
   }

   public void paintComponent(Graphics g)
   {
      Graphics2D g2 = (Graphics2D) g;

      BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D gImage = image.createGraphics();
      gImage.setPaint(Color.red);
      gImage.fill(shape1);
      AlphaComposite composite = AlphaComposite.getInstance(rule, alpha);
      gImage.setComposite(composite);
      gImage.setPaint(Color.blue);
      gImage.fill(shape2);
      g2.drawImage(image, null, 0, 0);
   }

   /**
    * Sets the composition rule.
    * @param r the rule (as an AlphaComposite constant)
    */
   public void setRule(int r)
   {
      rule = r;
      repaint();
   }

   /**
    * Sets the alpha of the source
    * @param a the alpha value between 0 and 100
    */
   public void setAlpha(int a)
   {
      alpha = (float) a / 100.0F;
      repaint();
   }

   private int rule;
   private Shape shape1;
   private Shape shape2;
   private float alpha;
}
