/**
   @version 1.11 2004-08-30
   @author Cay Horstmann
*/

package com.horstmann.corejava;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
   A customizer for the chart bean that allows the user to 
   edit all chart properties in a single tabbed dialog.
*/
public class ChartBean2Customizer extends JTabbedPane
   implements Customizer
{  
   public ChartBean2Customizer()
   {  
      data = new JTextArea();
      JPanel dataPane = new JPanel();
      dataPane.setLayout(new BorderLayout());
      dataPane.add(new JScrollPane(data), BorderLayout.CENTER);
      JButton dataButton = new JButton("Set data");
      dataButton.addActionListener(new
         ActionListener()
         {  
            public void actionPerformed(ActionEvent event) { setData(data.getText()); }
         });
      JPanel p = new JPanel();
      p.add(dataButton);
      dataPane.add(p, BorderLayout.SOUTH);

      JPanel colorPane = new JPanel();
      colorPane.setLayout(new BorderLayout());

      normal = new JCheckBox("Normal", true);
      inverse = new JCheckBox("Inverse", false);
      p = new JPanel();
      p.add(normal);
      p.add(inverse);
      ButtonGroup g = new ButtonGroup();
      g.add(normal);
      g.add(inverse);
      normal.addActionListener(new
         ActionListener()
         {  
            public void actionPerformed(ActionEvent event) { setInverse(false); }
         });

      inverse.addActionListener(
         new ActionListener()
         {  
            public void actionPerformed(ActionEvent event) { setInverse(true); }
         });

      colorEditor = PropertyEditorManager.findEditor(Color.class);
      colorEditor.addPropertyChangeListener(
         new PropertyChangeListener()
         {  
            public void propertyChange(PropertyChangeEvent event)
            {  
               setGraphColor((Color) colorEditor.getValue());
            }
         });

      colorPane.add(p, BorderLayout.NORTH);
      colorPane.add(colorEditor.getCustomEditor(), BorderLayout.CENTER);

      JPanel titlePane = new JPanel();
      titlePane.setLayout(new BorderLayout());

      g = new ButtonGroup();
      position = new JCheckBox[3];
      position[0] = new JCheckBox("Left", false);
      position[1] = new JCheckBox("Center", true);
      position[2] = new JCheckBox("Right", false);

      p = new JPanel();
      for (int i = 0; i < position.length; i++)
      {  
         final int value = i;
         p.add(position[i]);
         g.add(position[i]);
         position[i].addActionListener(new
            ActionListener()
            {  
               public void actionPerformed(ActionEvent event) { setTitlePosition(value); }
            });
      }

      titleField = new JTextField();
      titleField.getDocument().addDocumentListener(
         new DocumentListener()
         {  
            public void changedUpdate(DocumentEvent evt) {  setTitle(titleField.getText()); }
            public void insertUpdate(DocumentEvent evt) { setTitle(titleField.getText()); }
            public void removeUpdate(DocumentEvent evt) { setTitle(titleField.getText()); }
         });

      titlePane.add(titleField, BorderLayout.NORTH);
      titlePane.add(p, BorderLayout.SOUTH);
      addTab("Color", colorPane);
      addTab("Title", titlePane);
      addTab("Data", dataPane);
   }

   /**
      Sets the data to be shown in the chart.
      @param s a string containing the numbers to be displayed,
      separated by white space
   */
   public void setData(String s)
   {  
      StringTokenizer tokenizer = new StringTokenizer(s);

      int i = 0;
      double[] values = new double[tokenizer.countTokens()];
      while (tokenizer.hasMoreTokens())
      {  
         String token = tokenizer.nextToken();
         try
         {  
            values[i] = Double.parseDouble(token);
            i++;
         }
         catch (NumberFormatException e)
         {
         }
      }
      setValues(values);
   }

   /**
      Sets the title of the chart.
      @param newValue the new title
   */
   public void setTitle(String newValue)
   {  
      if (bean == null) return;
      String oldValue = bean.getTitle();
      bean.setTitle(newValue);
      firePropertyChange("title", oldValue, newValue);
   }

   /**
      Sets the title position of the chart.
      @param i the new title position (ChartBean2.LEFT,
      ChartBean2.CENTER, or ChartBean2.RIGHT)
   */   
   public void setTitlePosition(int i)
   {  
      if (bean == null) return;
      Integer oldValue = new Integer(bean.getTitlePosition());
      Integer newValue = new Integer(i);
      bean.setTitlePosition(i);
      firePropertyChange("titlePosition", oldValue, newValue);
   }

   /**
      Sets the inverse setting of the chart.
      @param b true if graph and background color are inverted
   */
   public void setInverse(boolean b)
   {  
      if (bean == null) return;
      Boolean oldValue = new Boolean(bean.isInverse());
      Boolean newValue = new Boolean(b);
      bean.setInverse(b);
      firePropertyChange("inverse", oldValue, newValue);
   }

   /**
      Sets the values to be shown in the chart.
      @param newValue the new value array
   */
   public void setValues(double[] newValue)
   {  
      if (bean == null) return;
      double[] oldValue = bean.getValues();
      bean.setValues(newValue);
      firePropertyChange("values", oldValue, newValue);
   }

   /**
      Sets the color of the chart
      @param newValue the new color
   */
   public void setGraphColor(Color newValue)
   {  
      if (bean == null) return;
      Color oldValue = bean.getGraphColor();
      bean.setGraphColor(newValue);
      firePropertyChange("graphColor", oldValue, newValue);
   }

   public void setObject(Object obj)
   {  
      bean = (ChartBean2) obj;

      data.setText("");
      for (double value : bean.getValues())
         data.append(value + "\n");

      normal.setSelected(!bean.isInverse());
      inverse.setSelected(bean.isInverse());

      titleField.setText(bean.getTitle());

      for (int i = 0; i < position.length; i++)
         position[i].setSelected(i == bean.getTitlePosition());

      colorEditor.setValue(bean.getGraphColor());
   }

   public Dimension getPreferredSize() { return new Dimension(XPREFSIZE, YPREFSIZE); }

   private static final int XPREFSIZE = 200;
   private static final int YPREFSIZE = 120;
   private ChartBean2 bean;
   private PropertyEditor colorEditor;

   private JTextArea data;
   private JCheckBox normal;
   private JCheckBox inverse;
   private JCheckBox[] position;
   private JTextField titleField;
}
