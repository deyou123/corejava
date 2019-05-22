package dateFormat;

import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import javax.swing.*;

/**
 * This program demonstrates formatting dates under various locales.
 * @version 1.01 2018-05-01
 * @author Cay Horstmann
 */
public class DateTimeFormatterTest
{
   public static void main(String[] args)
   {
      EventQueue.invokeLater(() ->
         {
            var frame = new DateTimeFormatterFrame();
            frame.setTitle("DateFormatTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
         });
   }
}

/**
 * This frame contains combo boxes to pick a locale, date and time formats, text fields to 
 * display formatted date and time, buttons to parse the text field contents, and a "lenient" 
 * check box.
 */
class DateTimeFormatterFrame extends JFrame
{
   private Locale[] locales;
   private LocalDate currentDate;
   private LocalTime currentTime;
   private ZonedDateTime currentDateTime;
   private DateTimeFormatter currentDateFormat;
   private DateTimeFormatter currentTimeFormat;
   private DateTimeFormatter currentDateTimeFormat;
   private JComboBox<String> localeCombo = new JComboBox<>();
   private JButton dateParseButton = new JButton("Parse");
   private JButton timeParseButton = new JButton("Parse");
   private JButton dateTimeParseButton = new JButton("Parse");
   private JTextField dateText = new JTextField(30);
   private JTextField timeText = new JTextField(30);
   private JTextField dateTimeText = new JTextField(30);
   private EnumCombo<FormatStyle> dateStyleCombo = new EnumCombo<>(FormatStyle.class, 
      "Short", "Medium", "Long", "Full");
   private EnumCombo<FormatStyle> timeStyleCombo = new EnumCombo<>(FormatStyle.class, 
      "Short", "Medium");
   private EnumCombo<FormatStyle> dateTimeStyleCombo = new EnumCombo<>(FormatStyle.class, 
      "Short", "Medium", "Long", "Full");

   public DateTimeFormatterFrame()
   {
      setLayout(new GridBagLayout());
      add(new JLabel("Locale"), new GBC(0, 0).setAnchor(GBC.EAST));
      add(localeCombo, new GBC(1, 0, 2, 1).setAnchor(GBC.WEST));
      
      add(new JLabel("Date"), new GBC(0, 1).setAnchor(GBC.EAST));
      add(dateStyleCombo, new GBC(1, 1).setAnchor(GBC.WEST));
      add(dateText, new GBC(2, 1, 2, 1).setFill(GBC.HORIZONTAL));
      add(dateParseButton, new GBC(4, 1).setAnchor(GBC.WEST));

      add(new JLabel("Time"), new GBC(0, 2).setAnchor(GBC.EAST));
      add(timeStyleCombo, new GBC(1, 2).setAnchor(GBC.WEST));
      add(timeText, new GBC(2, 2, 2, 1).setFill(GBC.HORIZONTAL));
      add(timeParseButton, new GBC(4, 2).setAnchor(GBC.WEST));

      add(new JLabel("Date and time"), new GBC(0, 3).setAnchor(GBC.EAST));
      add(dateTimeStyleCombo, new GBC(1, 3).setAnchor(GBC.WEST));
      add(dateTimeText, new GBC(2, 3, 2, 1).setFill(GBC.HORIZONTAL));
      add(dateTimeParseButton, new GBC(4, 3).setAnchor(GBC.WEST));

      locales = (Locale[]) Locale.getAvailableLocales().clone();
      Arrays.sort(locales, Comparator.comparing(Locale::getDisplayName));
      for (Locale loc : locales)
         localeCombo.addItem(loc.getDisplayName());
      localeCombo.setSelectedItem(Locale.getDefault().getDisplayName());
      currentDate = LocalDate.now();
      currentTime = LocalTime.now();
      currentDateTime = ZonedDateTime.now();
      updateDisplay();

      ActionListener listener = event -> updateDisplay();
      localeCombo.addActionListener(listener);
      dateStyleCombo.addActionListener(listener);
      timeStyleCombo.addActionListener(listener);
      dateTimeStyleCombo.addActionListener(listener);

      addAction(dateParseButton, () -> {
            currentDate = LocalDate.parse(dateText.getText().trim(), currentDateFormat);
         });
      addAction(timeParseButton, () -> { 
            currentTime = LocalTime.parse(timeText.getText().trim(), currentTimeFormat);
         });
      addAction(dateTimeParseButton, () -> {
            currentDateTime = ZonedDateTime.parse(
               dateTimeText.getText().trim(), currentDateTimeFormat);
         });

      pack();
   }

   /**
    * Adds the given action to the button and updates the display upon completion.
    * @param button the button to which to add the action
    * @param action the action to carry out when the button is clicked
    */
   public void addAction(JButton button, Runnable action) 
   {      
      button.addActionListener(event -> 
         {
            try
            {
               action.run();
               updateDisplay();
            }
            catch (Exception e)
            {
               JOptionPane.showMessageDialog(null, e.getMessage());
            }
         });      
   }      
   
   /**
    * Updates the display and formats the date according to the user settings.
    */
   public void updateDisplay()
   {
      Locale currentLocale = locales[localeCombo.getSelectedIndex()];
      FormatStyle dateStyle = dateStyleCombo.getValue();
      currentDateFormat = DateTimeFormatter.ofLocalizedDate(
         dateStyle).withLocale(currentLocale);
      dateText.setText(currentDateFormat.format(currentDate));
      FormatStyle timeStyle = timeStyleCombo.getValue();
      currentTimeFormat = DateTimeFormatter.ofLocalizedTime(
         timeStyle).withLocale(currentLocale);
      timeText.setText(currentTimeFormat.format(currentTime));
      FormatStyle dateTimeStyle = dateTimeStyleCombo.getValue();
      currentDateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(
         dateTimeStyle).withLocale(currentLocale);
      dateTimeText.setText(currentDateTimeFormat.format(currentDateTime));
   }
}
