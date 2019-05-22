package treeModel;

import java.awt.*;
import javax.swing.*;

/**
 * This frame holds the object tree.
 */
public class ObjectInspectorFrame extends JFrame
{
   private JTree tree;
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 300;

   public ObjectInspectorFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // we inspect this frame object

      var v = new Variable(getClass(), "this", this);
      var model = new ObjectTreeModel();
      model.setRoot(v);

      // construct and show tree

      tree = new JTree(model);
      add(new JScrollPane(tree), BorderLayout.CENTER);
   }
}
