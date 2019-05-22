/**
   @version 1.20 1999-09-28
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.lang.reflect.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

/**
   The panel inside the DoubleArrayEditor. It contains
   a list of the array values, together with buttons to
   resize the array and change the currently selected list value.
*/
public class DoubleArrayEditorPanel extends JPanel
{
   public DoubleArrayEditorPanel(PropertyEditorSupport ed)
   {
      editor = ed;
      setArray((double[])ed.getValue());

      setLayout(new GridBagLayout());

      add(sizeField, new GBC(0, 0, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(valueField, new GBC(0, 1, 1, 1).setWeight(100, 0).setFill(GBC.HORIZONTAL));
      add(sizeButton, new GBC(1, 0, 1, 1).setWeight(100, 0));
      add(valueButton, new GBC(1, 1, 1, 1).setWeight(100, 0));
      add(new JScrollPane(elementList), new GBC(0, 2, 2, 1).setWeight(100, 100).setFill(GBC.BOTH));

      sizeButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event) { changeSize(); }
         });

      valueButton.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event) { changeValue(); }
         });


      elementList.setSelectionMode(
         ListSelectionModel.SINGLE_SELECTION);

      elementList.addListSelectionListener(new
         ListSelectionListener()
         {
            public void valueChanged(ListSelectionEvent event)
            {
               int i = elementList.getSelectedIndex();
               if (i < 0) return;
               valueField.setText("" + array[i]);
            }
         });

      elementList.setModel(model);
      elementList.setSelectedIndex(0);
   }

   /**
      This method is called when the user wants to change
      the size of the array.
   */
   public void changeSize()
   {
      fmt.setParseIntegerOnly(true);
      int s = 0;
      try
      {
         s = fmt.parse(sizeField.getText()).intValue();
         if (s < 0) throw new ParseException("Out of bounds", 0);
      }
      catch (ParseException e)
      {
         JOptionPane.showMessageDialog(this, "" + e, "Input Error", JOptionPane.WARNING_MESSAGE);
         sizeField.requestFocus();
         return;
      }
      if (s == array.length) return;
      setArray((double[]) arrayGrow(array, s));
      editor.setValue(array);
      editor.firePropertyChange();
   }

   /**
      This method is called when the user wants to change
      the currently selected array value.
   */
   public void changeValue()
   {
      double v = 0;
      fmt.setParseIntegerOnly(false);
      try
      {
         v = fmt.parse(valueField.getText()).doubleValue();
      }
      catch (ParseException e)
      {
         JOptionPane.showMessageDialog(this, "" + e, "Input Error", JOptionPane.WARNING_MESSAGE);
         valueField.requestFocus();
         return;
      }
      int currentIndex = elementList.getSelectedIndex();
      setArray(currentIndex, v);
      editor.firePropertyChange();
   }

   /**
      Sets the indexed array property.
      @param v the array to edit
   */
   public void setArray(double[] v)
   {
      if (v == null) array = new double[0];
      else array = v;
      model.setArray(array);
      sizeField.setText("" + array.length);
      if (array.length > 0)
      {
         valueField.setText("" + array[0]);
         elementList.setSelectedIndex(0);
      }
      else
         valueField.setText("");
   }

   /**
      Gets the indexed array property.
      @return the array being edited
   */
   public double[] getArray()
   {
      return (double[]) array.clone();
   }

   /**
      Sets the indexed array property.
      @param i the index whose value to set
      @param value the new value for the given index
   */
   public void setArray(int i, double value)
   {
      if (0 <= i && i < array.length)
      {
         model.setValue(i, value);
         elementList.setSelectedIndex(i);
         valueField.setText("" + value);
      }
   }

   /**
      Gets the indexed array property.
      @param i the index whose value to get
      @return the value at the given index
   */
   public double getArray(int i)
   {
      if (0 <= i && i < array.length) return array[i];
      return 0;
   }

   /**
      Resizes an array
      @param a the array to grow
      @param newLength the new length
      @return an array with the given length and the same
      elements as a in the common positions
   */
   private static Object arrayGrow(Object a, int newLength)
   {
      Class cl = a.getClass();
      if (!cl.isArray()) return null;
      Class componentType = a.getClass().getComponentType();
      int length = Array.getLength(a);

      Object newArray = Array.newInstance(componentType, newLength);
      System.arraycopy(a, 0, newArray, 0, Math.min(length, newLength));
      return newArray;
   }

   private PropertyEditorSupport editor;
   private double[] array;
   private NumberFormat fmt = NumberFormat.getNumberInstance();
   private JTextField sizeField = new JTextField(4);
   private JTextField valueField = new JTextField(12);
   private JButton sizeButton = new JButton("Resize");
   private JButton valueButton = new JButton("Change");
   private JList elementList = new JList();
   private DoubleArrayListModel model = new DoubleArrayListModel();
}

/**
   The list model for the element list in the editor.
*/
class DoubleArrayListModel extends AbstractListModel
{
   public int getSize() { return array.length; }
   public Object getElementAt(int i) { return "[" + i + "] " + array[i]; }

   /**
      Sets a new array to be displayed in the list.
      @param a the new array
   */
   public void setArray(double[] a)
   {
      int oldLength = array == null ? 0 : array.length;
      array = a;
      int newLength = array == null ? 0 : array.length;
      if (oldLength > 0) fireIntervalRemoved(this, 0, oldLength);
      if (newLength > 0) fireIntervalAdded(this, 0, newLength);
   }

   /**
      Changes a value in the array to be displayed in the list.
      @param i the index whose value to change
      @param value the new value for the given index
   */
   public void setValue(int i, double value)
   {
      array[i] = value;
      fireContentsChanged(this, i, i);
   }

   private double[] array;
}
