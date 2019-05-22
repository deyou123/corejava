package treeModel;

import java.lang.reflect.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * This tree model describes the tree structure of a Java object. Children are the objects that are
 * stored in instance variables.
 */
public class ObjectTreeModel implements TreeModel
{
   private Variable root;
   private EventListenerList listenerList = new EventListenerList();

   /**
    * Constructs an empty tree.
    */
   public ObjectTreeModel()
   {
      root = null;
   }

   /**
    * Sets the root to a given variable.
    * @param v the variable that is being described by this tree
    */
   public void setRoot(Variable v)
   {
      Variable oldRoot = v;
      root = v;
      fireTreeStructureChanged(oldRoot);
   }

   public Object getRoot()
   {
      return root;
   }

   public int getChildCount(Object parent)
   {
      return ((Variable) parent).getFields().size();
   }

   public Object getChild(Object parent, int index)
   {
      ArrayList<Field> fields = ((Variable) parent).getFields();
      Field f = (Field) fields.get(index);
      Object parentValue = ((Variable) parent).getValue();
      try
      {
         return new Variable(f.getType(), f.getName(), f.get(parentValue));
      }
      catch (IllegalAccessException e)
      {
         return null;
      }
   }

   public int getIndexOfChild(Object parent, Object child)
   {
      int n = getChildCount(parent);
      for (int i = 0; i < n; i++)
         if (getChild(parent, i).equals(child)) return i;
      return -1;
   }

   public boolean isLeaf(Object node)
   {
      return getChildCount(node) == 0;
   }

   public void valueForPathChanged(TreePath path, Object newValue)
   {
   }

   public void addTreeModelListener(TreeModelListener l)
   {
      listenerList.add(TreeModelListener.class, l);
   }

   public void removeTreeModelListener(TreeModelListener l)
   {
      listenerList.remove(TreeModelListener.class, l);
   }

   protected void fireTreeStructureChanged(Object oldRoot)
   {
      TreeModelEvent event = new TreeModelEvent(this, new Object[] { oldRoot });
      for (TreeModelListener l : listenerList.getListeners(TreeModelListener.class))
         l.treeStructureChanged(event);
   }
}
