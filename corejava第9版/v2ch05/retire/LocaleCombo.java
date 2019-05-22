package retire;

import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * This combo box lets a user pick a locale. The locales are displayed in the locale of the combo
 * box, and sorted according to the collator of the display locale.
 * @version 1.00 2004-09-15
 * @author Cay Horstmann
 */
public class LocaleCombo extends JComboBox<Locale>
{
   private int selected;
   private Locale[] locales;
   private ListCellRenderer<Locale> renderer;

   /**
    * Constructs a locale combo that displays an immutable collection of locales.
    * @param locales the locales to display in this combo box
    */
   public LocaleCombo(Locale[] locales)
   {
      this.locales = (Locale[]) locales.clone();
      sort();
      setSelectedItem(getLocale());
   }

   public void setLocale(Locale newValue)
   {
      super.setLocale(newValue);
      sort();
   }

   private void sort()
   {
      final Locale loc = getLocale();
      final Collator collator = Collator.getInstance(loc);
      final Comparator<Locale> comp = new Comparator<Locale>()
         {
            public int compare(Locale a, Locale b)
            {
               return collator.compare(a.getDisplayName(loc), b.getDisplayName(loc));
            }
         };
      Arrays.sort(locales, comp);
      setModel(new ComboBoxModel<Locale>()
         {
            public Locale getElementAt(int i)
            {
               return locales[i];
            }

            public int getSize()
            {
               return locales.length;
            }

            public void addListDataListener(ListDataListener l)
            {
            }

            public void removeListDataListener(ListDataListener l)
            {
            }

            public Locale getSelectedItem()
            {
               return selected >= 0 ? locales[selected] : null;
            }

            public void setSelectedItem(Object anItem)
            {
               if (anItem == null) selected = -1;
               else selected = Arrays.binarySearch(locales, (Locale) anItem, comp);
            }

         });
      setSelectedItem(selected);
   }

   public ListCellRenderer<Locale> getRenderer()
   {
      if (renderer == null)
      {
         @SuppressWarnings("unchecked") final ListCellRenderer<Object> originalRenderer 
            = (ListCellRenderer<Object>) super.getRenderer();
         if (originalRenderer == null) return null;
         renderer = new ListCellRenderer<Locale>()
            {
               public Component getListCellRendererComponent(JList<? extends Locale> list, Locale value, int index,
                     boolean isSelected, boolean cellHasFocus)
               {
                  String renderedValue =  value.getDisplayName(getLocale());
                  return originalRenderer.getListCellRendererComponent(list, renderedValue, index,
                        isSelected, cellHasFocus);
               }
            };
      }
      return renderer;
   }

   public void setRenderer(ListCellRenderer<? super Locale> newValue)
   {
      renderer = null;
      super.setRenderer(newValue);
   }
}
