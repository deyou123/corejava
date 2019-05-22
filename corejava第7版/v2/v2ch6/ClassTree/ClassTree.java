/**
   @version 1.02 2004-08-21
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
   This program demonstrates cell rendering by showing
   a tree of classes and their superclasses.
*/
public class ClassTree
{  
   public static void main(String[] args)
   {  
      JFrame frame = new ClassTreeFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame displays the class tree, a text field and 
   add button to add more classes into the tree.
*/
class ClassTreeFrame extends JFrame
{  
   public ClassTreeFrame()
   {  
      setTitle("ClassTree");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // the root of the class tree is Object
      root = new DefaultMutableTreeNode(java.lang.Object.class);
      model = new DefaultTreeModel(root);
      tree = new JTree(model);

      // add this class to populate the tree with some data
      addClass(getClass());

      // set up node icons
      ClassNameTreeCellRenderer renderer = new ClassNameTreeCellRenderer();
      renderer.setClosedIcon(new ImageIcon("red-ball.gif"));
      renderer.setOpenIcon(new ImageIcon("yellow-ball.gif"));
      renderer.setLeafIcon(new ImageIcon("blue-ball.gif"));
      tree.setCellRenderer(renderer);

      add(new JScrollPane(tree), BorderLayout.CENTER);

      addTextField();
   }

   /**
      Add the text field and "Add" button to add a new class.
   */
   public void addTextField()
   {
      JPanel panel = new JPanel();

      ActionListener addListener = new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {  
               // add the class whose name is in the text field
               try
               {  
                  String text = textField.getText();
                  addClass(Class.forName(text)); // clear text field to indicate success
                  textField.setText("");
               }
               catch (ClassNotFoundException e)
               {  
                  JOptionPane.showMessageDialog(null, "Class not found");
               }
            }
         };

      // new class names are typed into this text field
      textField = new JTextField(20);
      textField.addActionListener(addListener);
      panel.add(textField);

      JButton addButton = new JButton("Add");
      addButton.addActionListener(addListener);
      panel.add(addButton);

      add(panel, BorderLayout.SOUTH);
   }

   /**
      Finds an object in the tree.
      @param obj the object to find
      @return the node containing the object or null
      if the object is not present in the tree
   */
   public DefaultMutableTreeNode findUserObject(Object obj)
   {  
      // find the node containing a user object
      Enumeration e = root.breadthFirstEnumeration();
      while (e.hasMoreElements())
      {  
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
         if (node.getUserObject().equals(obj))
            return node;
      }
      return null;
   }

   /**
      Adds a new class and any parent classes that aren't
      yet part of the tree
      @param c the class to add
      @return the newly added node.
   */
   public DefaultMutableTreeNode addClass(Class c)
   {  
      // add a new class to the tree

      // skip non-class types
      if (c.isInterface() || c.isPrimitive()) return null;

      // if the class is already in the tree, return its node
      DefaultMutableTreeNode node = findUserObject(c);
      if (node != null) return node;

      // class isn't present--first add class parent recursively

      Class s = c.getSuperclass();

      DefaultMutableTreeNode parent;
      if (s == null)
         parent = root;
      else
         parent = addClass(s);

      // add the class as a child to the parent
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(c);
      model.insertNodeInto(newNode, parent, parent.getChildCount());

      // make node visible
      TreePath path = new TreePath(model.getPathToRoot(newNode));
      tree.makeVisible(path);

      return newNode;
   }

   private DefaultMutableTreeNode root;
   private DefaultTreeModel model;
   private JTree tree;
   private JTextField textField;
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 300;
}

/**
   This class renders a class name either in plain or italic.
   Abstract classes are italic.
*/
class ClassNameTreeCellRenderer extends DefaultTreeCellRenderer
{  
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, 
      boolean expanded, boolean leaf, int row, boolean hasFocus)
   {  
      super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
      // get the user object
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      Class c = (Class) node.getUserObject();

      // the first time, derive italic font from plain font
      if (plainFont == null)
      {  
         plainFont = getFont();
         // the tree cell renderer is sometimes called with a label that has a null font
         if (plainFont != null) italicFont = plainFont.deriveFont(Font.ITALIC);
      }

      // set font to italic if the class is abstract, plain otherwise
      if ((c.getModifiers() & Modifier.ABSTRACT) == 0)
         setFont(plainFont);
      else
         setFont(italicFont);
      return this;
   }

   private Font plainFont = null;
   private Font italicFont = null;
}
