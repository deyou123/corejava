package collation;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

/**
 * This program demonstrates collating strings under various locales.
 * @version 1.15 2016-05-06
 * @author Cay Horstmann
 */
public class CollationTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> 
         {
            JFrame frame = new CollationFrame();
            frame.setTitle("CollationTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * This frame contains combo boxes to pick a locale, collation strength and decomposition rules, a
 * text field and button to add new strings, and a text area to list the collated strings.
 */
class CollationFrame extends JFrame
{
   private Collator collator = Collator.getInstance(Locale.getDefault());
   private List<String> strings = new ArrayList<>();
   private Collator currentCollator;
   private Locale[] locales;
   private JComboBox<String> localeCombo = new JComboBox<>();
   private JTextField newWord = new JTextField(20);
   private JTextArea sortedWords = new JTextArea(20, 20);
   private JButton addButton = new JButton("Add");
   private EnumCombo<Integer> strengthCombo = new EnumCombo<>(Collator.class, "Primary",
         "Secondary", "Tertiary", "Identical");
   private EnumCombo<Integer> decompositionCombo = new EnumCombo<>(Collator.class, 
         "Canonical Decomposition", "Full Decomposition", "No Decomposition");

   public CollationFrame()
   {
      setLayout(new GridBagLayout());
      add(new JLabel("Locale"), new GBC(0, 0).setAnchor(GBC.EAST));
      add(new JLabel("Strength"), new GBC(0, 1).setAnchor(GBC.EAST));
      add(new JLabel("Decomposition"), new GBC(0, 2).setAnchor(GBC.EAST));
      add(addButton, new GBC(0, 3).setAnchor(GBC.EAST));
      add(localeCombo, new GBC(1, 0).setAnchor(GBC.WEST));
      add(strengthCombo, new GBC(1, 1).setAnchor(GBC.WEST));
      add(decompositionCombo, new GBC(1, 2).setAnchor(GBC.WEST));
      add(newWord, new GBC(1, 3).setFill(GBC.HORIZONTAL));
      add(new JScrollPane(sortedWords), new GBC(0, 4, 2, 1).setFill(GBC.BOTH));
      
      locales = (Locale[]) Collator.getAvailableLocales().clone();
      Arrays.sort(locales, (l1, l2) -> collator.compare(l1.getDisplayName(), l2.getDisplayName()));               
      for (Locale loc : locales)
         localeCombo.addItem(loc.getDisplayName());      
      localeCombo.setSelectedItem(Locale.getDefault().getDisplayName());
      
      strings.add("America");
      strings.add("able");
      strings.add("Zulu");
      strings.add("zebra");
      strings.add("\u00C5ngstr\u00F6m");
      strings.add("A\u030angstro\u0308m");
      strings.add("Angstrom");
      strings.add("Able");
      strings.add("office");
      strings.add("o\uFB03ce");
      strings.add("Java\u2122");
      strings.add("JavaTM");
      updateDisplay();

      addButton.addActionListener(event ->
         {
            strings.add(newWord.getText());
            updateDisplay();
         });

      ActionListener listener = event -> updateDisplay();

      localeCombo.addActionListener(listener);
      strengthCombo.addActionListener(listener);
      decompositionCombo.addActionListener(listener);
      pack();
   }

   /**
    * Updates the display and collates the strings according to the user settings.
    */
   public void updateDisplay()
   {
      Locale currentLocale = locales[localeCombo.getSelectedIndex()];
      localeCombo.setLocale(currentLocale);

      currentCollator = Collator.getInstance(currentLocale);
      currentCollator.setStrength(strengthCombo.getValue());
      currentCollator.setDecomposition(decompositionCombo.getValue());

      strings.sort(currentCollator);

      sortedWords.setText("");
      for (int i = 0; i < strings.size(); i++)
      {
         String s = strings.get(i);
         if (i > 0 && currentCollator.compare(s, strings.get(i - 1)) == 0) sortedWords
               .append("= ");
         sortedWords.append(s + "\n");
      }
      pack();
   }
}
