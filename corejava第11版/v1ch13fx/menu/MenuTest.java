package menu;

import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
   @version 1.3 2017-12-29
   @author Cay Horstmann
*/
public class MenuTest extends Application
{
   private TextArea textArea = new TextArea();

   /**
      Makes this item or, if a menu, its descendant items, carry out the 
      given action if there isn't already an action defined.
      @param item the menu item (which may be a menu)
      @param action the default action
   */
   private void defaultAction(MenuItem item, EventHandler<ActionEvent> action)
   {
      if (item instanceof Menu)
         for (MenuItem child : ((Menu) item).getItems())
            defaultAction(child, action);
      else if (item.getOnAction() == null)
         item.setOnAction(action);
   }
   
   public void start(Stage stage)
   {      
      Menu fileMenu = new Menu("File");
      MenuItem exitItem = new MenuItem("Exit");
      exitItem.setOnAction(event -> Platform.exit());

      // demonstrate accelerators

      MenuItem newItem = new MenuItem("New");
      MenuItem openItem = new MenuItem("Open ...");
      openItem.setAccelerator(
         KeyCombination.keyCombination("Shortcut+O"));
      MenuItem saveItem = new MenuItem("Save");
      saveItem.setAccelerator(
         KeyCombination.keyCombination("Shortcut+S"));
      MenuItem saveAsItem = new MenuItem("Save as ...");

      fileMenu.getItems().addAll(newItem,
         openItem,
         saveItem,
         saveAsItem,
         new SeparatorMenuItem(),
         exitItem);

      // demonstrate checkbox and radio button menus

      CheckMenuItem readOnlyItem = new CheckMenuItem("Read-only");
      readOnlyItem.setOnAction(event ->
         {
            saveItem.setDisable(readOnlyItem.isSelected());
            saveAsItem.setDisable(readOnlyItem.isSelected());
         });
      /*
        Or use binding:
        saveItem.disableProperty().bind(readOnlyItem.selectedProperty());
        saveAsItem.disableProperty().bind(readOnlyItem.selectedProperty());
      */

      ToggleGroup group = new ToggleGroup();
      RadioMenuItem insertItem = new RadioMenuItem("Insert");
      insertItem.setToggleGroup(group);
      insertItem.setSelected(true);
      RadioMenuItem overtypeItem = new RadioMenuItem("Overtype");
      overtypeItem.setToggleGroup(group);
      
      Menu editMenu = new Menu("Edit");

      // demonstrate icons

      MenuItem cutItem = new MenuItem("Cut",
         new ImageView("menu/cut.gif"));
      MenuItem copyItem = new MenuItem("Copy",
         new ImageView("menu/copy.gif"));
      MenuItem pasteItem = new MenuItem("Paste",
         new ImageView("menu/paste.gif"));      

      // demonstrate context menu
      
      ContextMenu contextMenu = new ContextMenu(cutItem, copyItem, pasteItem);
      textArea.setContextMenu(contextMenu);

      editMenu.getItems().addAll(cutItem, copyItem, pasteItem);
         // Bug or restriction--must add to context menu first
         // http://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8194270
      
      // demonstrate nested menus

      Menu optionsMenu = new Menu("Options",
         new ImageView("menu/options.gif"), readOnlyItem,
         insertItem, overtypeItem);
      
      editMenu.getItems().add(optionsMenu);
     
      // demonstrate mnemonics

      MenuItem aboutProgramItem = new MenuItem("_About this program");
      MenuItem aboutCoreJavaItem = new MenuItem("About _Core Java");
      Menu helpMenu = new Menu("_Help", null,
         aboutProgramItem, aboutCoreJavaItem);

      // add menu bar

      MenuBar bar = new MenuBar(fileMenu, editMenu, helpMenu);
      VBox root = new VBox(bar, textArea);
      for (Menu menu : bar.getMenus()) defaultAction(menu, event ->
         {
            MenuItem item = (MenuItem) event.getSource();
            textArea.appendText(item.getText() + " selected\n");
         });
      
      stage.setScene(new Scene(root));
      stage.setTitle("MenuTest");
      stage.show();
   }
}
