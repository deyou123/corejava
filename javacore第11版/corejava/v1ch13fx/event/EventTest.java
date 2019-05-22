package event;

import java.util.*;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
   @version 1.0 2017-12-23
   @author Cay Horstmann
*/
public class EventTest extends Application
{
   public void start(Stage stage)
   {
      Button button = new Button("Random font");
      Text message = new Text("Times New Roman");
      message.setFont(Font.font("Times New Roman", 100));
      List<String> families = Font.getFamilies();
      Random generator = new Random();
      button.setOnAction(event ->
         {
            String newFamily = families.get(
               generator.nextInt(families.size()));
            message.setText(newFamily);
            message.setFont(Font.font(
               newFamily, message.getFont().getSize()));
         });

      Slider slider = new Slider();
      slider.setValue(100);
      slider.valueProperty().addListener(property ->
         {
            double newSize = slider.getValue();
            message.setFont(Font.font(
               message.getFont().getFamily(), newSize));
         });

      VBox root = new VBox(button, slider, message);
         Scene scene = new Scene(root);

      stage.setTitle("EventTest");
      stage.setScene(scene);
      stage.setOnCloseRequest(event ->
         {
            if (slider.getValue() == 100)
            {
               event.consume(); // Stops window from closing
               Alert alert = new Alert(AlertType.INFORMATION,
                  "Move the slider before quitting.");
               alert.showAndWait();
            }
         });            
      stage.show();
   }
}
