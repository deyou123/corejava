import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @version 1.33 2007-06-12
 * @author Cay Horstmann
 */
public class BorderTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               BorderFrame frame = new BorderFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame with radio buttons to pick a border style.
 */
class BorderFrame extends JFrame
{
   public BorderFrame()
   {
      setTitle("BorderTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      demoPanel = new JPanel();
      buttonPanel = new JPanel();
      group = new ButtonGroup();

      addRadioButton("Lowered bevel", BorderFactory.createLoweredBevelBorder());
      addRadioButton("Raised bevel", BorderFactory.createRaisedBevelBorder());
      addRadioButton("Etched", BorderFactory.createEtchedBorder());
      addRadioButton("Line", BorderFactory.createLineBorder(Color.BLUE));
      addRadioButton("Matte", BorderFactory.createMatteBorder(10, 10, 10, 10, Color.BLUE));
      addRadioButton("Empty", BorderFactory.createEmptyBorder());

      Border etched = BorderFactory.createEtchedBorder();
      Border titled = BorderFactory.createTitledBorder(etched, "Border types");
      buttonPanel.setBorder(titled);

      setLayout(new GridLayout(2, 1));
      add(buttonPanel);
      add(demoPanel);
   }

   public void addRadioButton(String buttonName, final Border b)
   {
      JRadioButton button = new JRadioButton(buttonName);
      button.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               demoPanel.setBorder(b);
            }
         });
      group.add(button);
      buttonPanel.add(button);
   }

   public static final int DEFAULT_WIDTH = 600;
   public static final int DEFAULT_HEIGHT = 200;

   private JPanel demoPanel;
   private JPanel buttonPanel;
   private ButtonGroup group;
}
