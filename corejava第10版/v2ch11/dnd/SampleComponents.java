package dnd;

import java.awt.*;

import javax.swing.*;
import javax.swing.tree.*;

public class SampleComponents
{
   public static JTree tree()
   {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("World");
      DefaultMutableTreeNode country = new DefaultMutableTreeNode("USA");
      root.add(country);
      DefaultMutableTreeNode state = new DefaultMutableTreeNode("California");
      country.add(state);
      DefaultMutableTreeNode city = new DefaultMutableTreeNode("San Jose");
      state.add(city);
      city = new DefaultMutableTreeNode("Cupertino");
      state.add(city);
      state = new DefaultMutableTreeNode("Michigan");
      country.add(state);
      city = new DefaultMutableTreeNode("Ann Arbor");
      state.add(city);
      country = new DefaultMutableTreeNode("Germany");
      root.add(country);
      state = new DefaultMutableTreeNode("Schleswig-Holstein");
      country.add(state);
      city = new DefaultMutableTreeNode("Kiel");
      state.add(city);
      return new JTree(root);
   }

   public static JList<String> list()
   {
      String[] words = { "quick", "brown", "hungry", "wild", "silent", "huge", "private",
            "abstract", "static", "final" };

      DefaultListModel<String> model = new DefaultListModel<>();
      for (String word : words)
         model.addElement(word);
      return new JList<>(model);
   }

   public static JTable table()
   {
      Object[][] cells = { { "Mercury", 2440.0, 0, false, Color.YELLOW },
            { "Venus", 6052.0, 0, false, Color.YELLOW },
            { "Earth", 6378.0, 1, false, Color.BLUE }, { "Mars", 3397.0, 2, false, Color.RED },
            { "Jupiter", 71492.0, 16, true, Color.ORANGE },
            { "Saturn", 60268.0, 18, true, Color.ORANGE },
            { "Uranus", 25559.0, 17, true, Color.BLUE },
            { "Neptune", 24766.0, 8, true, Color.BLUE },
            { "Pluto", 1137.0, 1, false, Color.BLACK } };

      String[] columnNames = { "Planet", "Radius", "Moons", "Gaseous", "Color" };
      return new JTable(cells, columnNames);
   }
}
