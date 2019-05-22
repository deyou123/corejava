package choices;

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
   @version 1.4 2017-12-29
   @author Cay Horstmann
*/
public class ChoiceTest extends Application
{
   private static final double rem = new Text("").getLayoutBounds().getHeight();

   private static HBox hbox(Node... children)
   {
      HBox box = new HBox(0.8 * rem, children);
      box.setPadding(new Insets(0.8 * rem));
      return box;
   }
   
   public void start(Stage stage)
   {
      Label sampleText = new Label("The quick brown fox jumps over the lazy dog.");
      sampleText.setPrefWidth(40 * rem);
      sampleText.setPrefHeight(5 * rem);
      sampleText.setFont(Font.font(14));
      
      CheckBox bold = new CheckBox("Bold");
      CheckBox italic = new CheckBox("Italic");

      RadioButton small = new RadioButton("Small");
      RadioButton medium = new RadioButton("Medium");
      RadioButton large = new RadioButton("Large");
      RadioButton extraLarge = new RadioButton("Extra Large");

      small.setUserData(8);
      medium.setUserData(14);
      large.setUserData(18);
      extraLarge.setUserData(36);

      ToggleGroup group = new ToggleGroup();
      small.setToggleGroup(group);
      medium.setToggleGroup(group);
      large.setToggleGroup(group);
      extraLarge.setToggleGroup(group);
      medium.setSelected(true);

      ComboBox<String> families = new ComboBox<>();
      families.getItems().addAll(Font.getFamilies());
      families.setValue("System");

      EventHandler<ActionEvent> listener = event ->
         {
            int size = (int) group.getSelectedToggle().getUserData();
            Font font = Font.font(
               families.getValue(),
               bold.isSelected() ? FontWeight.BOLD : FontWeight.NORMAL,
               italic.isSelected() ? FontPosture.ITALIC : FontPosture.REGULAR,
               size);
            sampleText.setFont(font);
         };
      small.setOnAction(listener);
      medium.setOnAction(listener);
      large.setOnAction(listener);
      extraLarge.setOnAction(listener);
      bold.setOnAction(listener);
      italic.setOnAction(listener);
      families.setOnAction(listener);
      
      VBox root = new VBox(0.8 * rem,
         hbox(sampleText),
         hbox(bold, italic),
         hbox(small, medium, large, extraLarge),
         hbox(families));

      stage.setScene(new Scene(root));
      stage.setTitle("ChoiceTest");
      stage.show();
   }
}
