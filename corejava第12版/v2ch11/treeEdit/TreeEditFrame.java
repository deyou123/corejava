package treeEdit;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * A frame with a tree and buttons to edit the tree.
 */
public class TreeEditFrame extends JFrame
{
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 200;

   private DefaultTreeModel model;
   private JTree tree;

   public TreeEditFrame()
   {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // construct tree

      TreeNode root = makeSampleTree();
      model = new DefaultTreeModel(root);
      tree = new JTree(model);
      tree.setEditable(true);

      // add scroll pane with tree

      var scrollPane = new JScrollPane(tree);
      add(scrollPane, BorderLayout.CENTER);

      makeButtons();
   }

   public TreeNode makeSampleTree()
   {
      var root = new DefaultMutableTreeNode("World");
      var country = new DefaultMutableTreeNode("USA");
      root.add(country);
      var state = new DefaultMutableTreeNode("California");
      country.add(state);
      var city = new DefaultMutableTreeNode("San Jose");
      state.add(city);
      city = new DefaultMutableTreeNode("San Diego");
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
      return root;
   }

   /**
    * Makes the buttons to add a sibling, add a child, and delete a node.
    */
   public void makeButtons()
   {
      var panel = new JPanel();
      var addSiblingButton = new JButton("Add Sibling");
      addSiblingButton.addActionListener(event ->
         {
            var selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (selectedNode == null) return;

            var parent = (DefaultMutableTreeNode) selectedNode.getParent();

            if (parent == null) return;

            var newNode = new DefaultMutableTreeNode("New");

            int selectedIndex = parent.getIndex(selectedNode);
            model.insertNodeInto(newNode, parent, selectedIndex + 1);

            // now display new node

            TreeNode[] nodes = model.getPathToRoot(newNode);
            var path = new TreePath(nodes);
            tree.scrollPathToVisible(path);
         });
      panel.add(addSiblingButton);

      var addChildButton = new JButton("Add Child");
      addChildButton.addActionListener(event ->
         {
            var selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (selectedNode == null) return;

            var newNode = new DefaultMutableTreeNode("New");
            model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());

            // now display new node

            TreeNode[] nodes = model.getPathToRoot(newNode);
            var path = new TreePath(nodes);
            tree.scrollPathToVisible(path);
         });
      panel.add(addChildButton);

      var deleteButton = new JButton("Delete");
      deleteButton.addActionListener(event ->
         {
            var selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (selectedNode != null && selectedNode.getParent() != null) model
                  .removeNodeFromParent(selectedNode);
         });
      panel.add(deleteButton);
      add(panel, BorderLayout.SOUTH);
   }
}
