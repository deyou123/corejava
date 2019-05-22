package chart;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * The panel inside the DoubleArrayEditor. It contains a list of the array values, together with
 * buttons to resize the array and change the currently selected list value.
 * @version 1.31 2012-06-10
 * @author Cay Horstmann
 */
public class DoubleArrayEditorPanel extends JPanel
{
   private PropertyEditorSupport editor;
   private double[] array;
   private JFormattedTextField sizeField = new JFormattedTextField(new Integer(0));
   private JFormattedTextField valueField = new JFormattedTextField(new Double(0.0));
   private JButton sizeButton = new JButton("Resize");
   private JButton valueButton = new JButton("Change");
   private JList<String> elementList = new JList<>();
   private DoubleArrayListModel model = new DoubleArrayListModel();

   public DoubleArrayEditorPanel(PropertyEditorSupport ed)
   {
      editor = ed;
      setArray((double[]) ed.getValue());

      setLayout(new GridBagLayout());

      add(sizeField, new GBC(0, 0, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(valueField, new GBC(0, 1, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(sizeButton, new GBC(1, 0, 1, 1).setWeight(100, 0));
      add(valueButton, new GBC(1, 1, 1, 1).setWeight(100, 0));
      add(new JScrollPane(elementList), new GBC(0, 2, 2, 1).setWeight(100, 100).setFill(GBC.BOTH));

      ActionListener listener = EventHandler.create(ActionListener.class, this, "changeSize"); 
      sizeButton.addActionListener(listener);
      sizeField.addActionListener(listener);

      listener = EventHandler.create(ActionListener.class, this, "changeValue"); 
      valueButton.addActionListener(listener);
      valueField.addActionListener(listener);

      elementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      elementList.addListSelectionListener(new ListSelectionListener()
         {
            public void valueChanged(ListSelectionEvent event)
            {
               int i = elementList.getSelectedIndex();
               if (i < 0) return;
               valueField.setValue(array[i]);
            }
         });

      elementList.setModel(model);
      elementList.setSelectedIndex(0);
   }

   /**
    * This method is called when the user wants to change the size of the array.
    */
   public void changeSize()
   {
      int s = (Integer) sizeField.getValue();
      if (s < 0 || s == array.length) return;
      setArray(Arrays.copyOf(array, s));
      editor.setValue(array);
   }

   /**
    * This method is called when the user wants to change the currently selected array value.
    */
   public void changeValue()
   {
      double v = (Double) valueField.getValue(); 
      int currentIndex = elementList.getSelectedIndex();
      if (0 <= currentIndex && currentIndex < array.length)
      {
         model.setValue(currentIndex, v);
         elementList.setSelectedIndex(currentIndex);
      }
      editor.firePropertyChange();
   }

   /**
    * Sets the indexed array property.
    * @param v the array to edit
    */
   private void setArray(double[] v)
   {
      array = v;
      model.setArray(array);
      sizeField.setValue(array.length);
      if (array.length > 0)
      {
         valueField.setValue(array[0]);
         elementList.setSelectedIndex(0);
      }
      else valueField.setValue(0.0);
   }
}

/**
 * The list model for the element list in the editor.
 */
class DoubleArrayListModel extends AbstractListModel<String>
{
   private double[] array;

   public int getSize()
   {
      return array.length;
   }

   public String getElementAt(int i)
   {
      return "[" + i + "] " + array[i];
   }

   /**
    * Sets a new array to be displayed in the list.
    * @param a the new array
    */
   public void setArray(double[] a)
   {
      int oldLength = array == null ? 0 : array.length;
      if (oldLength > 0) fireIntervalRemoved(this, 0, oldLength);
      array = a;
      int newLength = array == null ? 0 : array.length;
      if (newLength > 0) fireIntervalAdded(this, 0, newLength);
   }

   /**
    * Changes a value in the array to be displayed in the list.
    * @param i the index whose value to change
    * @param value the new value for the given index
    */
   public void setValue(int i, double value)
   {
      array[i] = value;
      fireContentsChanged(this, i, i);      
   }
}
