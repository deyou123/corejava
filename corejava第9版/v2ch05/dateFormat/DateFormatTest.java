package dateFormat;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

/**
 * This program demonstrates formatting dates under various locales.
 * @version 1.13 2007-07-25
 * @author Cay Horstmann
 */
public class DateFormatTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(new Runnable()
         {
            public void run()
            {
               JFrame frame = new DateFormatFrame();
               frame.setTitle("DateFormatTest");
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               frame.setVisible(true);
            }
         });
   }
}

/**
 * This frame contains combo boxes to pick a locale, date and time formats, text fields to display
 * formatted date and time, buttons to parse the text field contents, and a "lenient" check box.
 */
class DateFormatFrame extends JFrame
{
   private Locale[] locales;
   private Date currentDate;
   private Date currentTime;
   private DateFormat currentDateFormat;
   private DateFormat currentTimeFormat;
   private JComboBox<String> localeCombo = new JComboBox<>();
   private JButton dateParseButton = new JButton("Parse date");
   private JButton timeParseButton = new JButton("Parse time");
   private JTextField dateText = new JTextField(30);
   private JTextField timeText = new JTextField(30);
   private JCheckBox lenientCheckbox = new JCheckBox("Parse lenient", true);
   private EnumCombo dateStyleCombo = new EnumCombo(DateFormat.class, "Default",
         "Full", "Long", "Medium", "Short");
   private EnumCombo timeStyleCombo = new EnumCombo(DateFormat.class, "Default",
         "Full", "Long", "Medium", "Short");

   public DateFormatFrame()
   {
      setLayout(new GridBagLayout());
      add(new JLabel("Locale"), new GBC(0, 0).setAnchor(GBC.EAST));
      add(new JLabel("Date style"), new GBC(0, 1).setAnchor(GBC.EAST));
      add(new JLabel("Time style"), new GBC(2, 1).setAnchor(GBC.EAST));
      add(new JLabel("Date"), new GBC(0, 2).setAnchor(GBC.EAST));
      add(new JLabel("Time"), new GBC(0, 3).setAnchor(GBC.EAST));
      add(localeCombo, new GBC(1, 0, 2, 1).setAnchor(GBC.WEST));
      add(dateStyleCombo, new GBC(1, 1).setAnchor(GBC.WEST));
      add(timeStyleCombo, new GBC(3, 1).setAnchor(GBC.WEST));
      add(dateParseButton, new GBC(3, 2).setAnchor(GBC.WEST));
      add(timeParseButton, new GBC(3, 3).setAnchor(GBC.WEST));
      add(lenientCheckbox, new GBC(0, 4, 2, 1).setAnchor(GBC.WEST));
      add(dateText, new GBC(1, 2, 2, 1).setFill(GBC.HORIZONTAL));
      add(timeText, new GBC(1, 3, 2, 1).setFill(GBC.HORIZONTAL));

      locales = (Locale[]) DateFormat.getAvailableLocales().clone();
      Arrays.sort(locales, new Comparator<Locale>()
         {
            public int compare(Locale l1, Locale l2)
            {
               return l1.getDisplayName().compareTo(l2.getDisplayName());
            }
         });
      for (Locale loc : locales)
         localeCombo.addItem(loc.getDisplayName());
      localeCombo.setSelectedItem(Locale.getDefault().getDisplayName());
      currentDate = new Date();
      currentTime = new Date();
      updateDisplay();

      ActionListener listener = new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               updateDisplay();
            }
         };

      localeCombo.addActionListener(listener);
      dateStyleCombo.addActionListener(listener);
      timeStyleCombo.addActionListener(listener);

      dateParseButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               String d = dateText.getText().trim();
               try
               {
                  currentDateFormat.setLenient(lenientCheckbox.isSelected());
                  Date date = currentDateFormat.parse(d);
                  currentDate = date;
                  updateDisplay();
               }
               catch (ParseException e)
               {
                  dateText.setText("Parse error: " + d);
               }
               catch (IllegalArgumentException e)
               {
                  dateText.setText("Argument error: " + d);
               }
            }
         });

      timeParseButton.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               String t = timeText.getText().trim();
               try
               {
                  currentDateFormat.setLenient(lenientCheckbox.isSelected());
                  Date date = currentTimeFormat.parse(t);
                  currentTime = date;
                  updateDisplay();
               }
               catch (ParseException e)
               {
                  timeText.setText("Parse error: " + t);
               }
               catch (IllegalArgumentException e)
               {
                  timeText.setText("Argument error: " + t);
               }
            }
         });
      pack();
   }

   /**
    * Updates the display and formats the date according to the user settings.
    */
   public void updateDisplay()
   {
      Locale currentLocale = locales[localeCombo.getSelectedIndex()];
      int dateStyle = dateStyleCombo.getValue();
      currentDateFormat = DateFormat.getDateInstance(dateStyle, currentLocale);
      String d = currentDateFormat.format(currentDate);
      dateText.setText(d);
      int timeStyle = timeStyleCombo.getValue();
      currentTimeFormat = DateFormat.getTimeInstance(timeStyle, currentLocale);
      String t = currentTimeFormat.format(currentTime);
      timeText.setText(t);
   }
}
