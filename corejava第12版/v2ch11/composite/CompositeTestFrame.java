package composite;

import java.awt.*;
import javax.swing.*;

/**
 * This frame contains a combo box to choose a composition rule, a slider to change the
 * source alpha channel, and a component that shows the composition.
 */
class CompositeTestFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 400;

   private CompositeComponent canvas;
   private JComboBox<Rule> ruleCombo;
   private JSlider alphaSlider;
   private JTextField explanation;

   public CompositeTestFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      canvas = new CompositeComponent();
      add(canvas, BorderLayout.CENTER);

      ruleCombo = new JComboBox<>(new Rule[] { new Rule("CLEAR", "  ", "  "),
         new Rule("SRC", " S", " S"), new Rule("DST", "  ", "DD"),
         new Rule("SRC_OVER", " S", "DS"), new Rule("DST_OVER", " S", "DD"),
         new Rule("SRC_IN", "  ", " S"), new Rule("SRC_OUT", " S", "  "),
         new Rule("DST_IN", "  ", " D"), new Rule("DST_OUT", "  ", "D "),
         new Rule("SRC_ATOP", "  ", "DS"), new Rule("DST_ATOP", " S", " D"),
         new Rule("XOR", " S", "D "), });
      ruleCombo.addActionListener(event ->
         {
            var r = (Rule) ruleCombo.getSelectedItem();
            canvas.setRule(r.getValue());
            explanation.setText(r.getExplanation());
         });

      alphaSlider = new JSlider(0, 100, 75);
      alphaSlider.addChangeListener(event -> canvas.setAlpha(alphaSlider.getValue()));
      var panel = new JPanel();
      panel.add(ruleCombo);
      panel.add(new JLabel("Alpha"));
      panel.add(alphaSlider);
      add(panel, BorderLayout.NORTH);

      explanation = new JTextField();
      add(explanation, BorderLayout.SOUTH);

      canvas.setAlpha(alphaSlider.getValue());
      Rule r = ruleCombo.getItemAt(ruleCombo.getSelectedIndex());
      canvas.setRule(r.getValue());
      explanation.setText(r.getExplanation());      
   }
}
