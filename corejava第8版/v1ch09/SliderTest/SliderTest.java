import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * @version 1.13 2007-06-12
 * @author Cay Horstmann
 */
public class SliderTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               SliderTestFrame frame = new SliderTestFrame();
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * A frame with many sliders and a text field to show slider values.
 */
class SliderTestFrame extends JFrame
{
   public SliderTestFrame()
   {
      setTitle("SliderTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      sliderPanel = new JPanel();
      sliderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

      // common listener for all sliders
      listener = new ChangeListener()
         {
            public void stateChanged(ChangeEvent event)
            {
               // update text field when the slider value changes
               JSlider source = (JSlider) event.getSource();
               textField.setText("" + source.getValue());
            }
         };

      // add a plain slider

      JSlider slider = new JSlider();
      addSlider(slider, "Plain");

      // add a slider with major and minor ticks

      slider = new JSlider();
      slider.setPaintTicks(true);
      slider.setMajorTickSpacing(20);
      slider.setMinorTickSpacing(5);
      addSlider(slider, "Ticks");

      // add a slider that snaps to ticks

      slider = new JSlider();
      slider.setPaintTicks(true);
      slider.setSnapToTicks(true);
      slider.setMajorTickSpacing(20);
      slider.setMinorTickSpacing(5);
      addSlider(slider, "Snap to ticks");

      // add a slider with no track

      slider = new JSlider();
      slider.setPaintTicks(true);
      slider.setMajorTickSpacing(20);
      slider.setMinorTickSpacing(5);
      slider.setPaintTrack(false);
      addSlider(slider, "No track");

      // add an inverted slider

      slider = new JSlider();
      slider.setPaintTicks(true);
      slider.setMajorTickSpacing(20);
      slider.setMinorTickSpacing(5);
      slider.setInverted(true);
      addSlider(slider, "Inverted");

      // add a slider with numeric labels

      slider = new JSlider();
      slider.setPaintTicks(true);
      slider.setPaintLabels(true);
      slider.setMajorTickSpacing(20);
      slider.setMinorTickSpacing(5);
      addSlider(slider, "Labels");

      // add a slider with alphabetic labels

      slider = new JSlider();
      slider.setPaintLabels(true);
      slider.setPaintTicks(true);
      slider.setMajorTickSpacing(20);
      slider.setMinorTickSpacing(5);

      Dictionary<Integer, Component> labelTable = new Hashtable<Integer, Component>();
      labelTable.put(0, new JLabel("A"));
      labelTable.put(20, new JLabel("B"));
      labelTable.put(40, new JLabel("C"));
      labelTable.put(60, new JLabel("D"));
      labelTable.put(80, new JLabel("E"));
      labelTable.put(100, new JLabel("F"));

      slider.setLabelTable(labelTable);
      addSlider(slider, "Custom labels");

      // add a slider with icon labels

      slider = new JSlider();
      slider.setPaintTicks(true);
      slider.setPaintLabels(true);
      slider.setSnapToTicks(true);
      slider.setMajorTickSpacing(20);
      slider.setMinorTickSpacing(20);

      labelTable = new Hashtable<Integer, Component>();

      // add card images

      labelTable.put(0, new JLabel(new ImageIcon("nine.gif")));
      labelTable.put(20, new JLabel(new ImageIcon("ten.gif")));
      labelTable.put(40, new JLabel(new ImageIcon("jack.gif")));
      labelTable.put(60, new JLabel(new ImageIcon("queen.gif")));
      labelTable.put(80, new JLabel(new ImageIcon("king.gif")));
      labelTable.put(100, new JLabel(new ImageIcon("ace.gif")));

      slider.setLabelTable(labelTable);
      addSlider(slider, "Icon labels");

      // add the text field that displays the slider value

      textField = new JTextField();
      add(sliderPanel, BorderLayout.CENTER);
      add(textField, BorderLayout.SOUTH);
   }

   /**
    * Adds a slider to the slider panel and hooks up the listener
    * @param s the slider
    * @param description the slider description
    */
   public void addSlider(JSlider s, String description)
   {
      s.addChangeListener(listener);
      JPanel panel = new JPanel();
      panel.add(s);
      panel.add(new JLabel(description));
      sliderPanel.add(panel);
   }

   public static final int DEFAULT_WIDTH = 350;
   public static final int DEFAULT_HEIGHT = 450;

   private JPanel sliderPanel;
   private JTextField textField;
   private ChangeListener listener;
}
