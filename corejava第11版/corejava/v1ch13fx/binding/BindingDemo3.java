package binding;

import static javafx.beans.binding.Bindings.*;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

/**
   @version 1.0 2017-12-29
   @author Cay Horstmann
*/
public class BindingDemo3 extends Application
{
   public void start(Stage stage)
   {      
      Button smaller = new Button("Smaller");
      Button larger = new Button("Larger");
      Rectangle gauge = new Rectangle(0, 5, 50, 15);
      Rectangle outline = new Rectangle(0, 5, 100, 15);
      outline.setFill(null);
      outline.setStroke(Color.BLACK);
      Pane pane = new Pane(gauge, outline);
      
      smaller.setOnAction(
         event -> gauge.setWidth(gauge.getWidth() - 10));
      larger.setOnAction(
         event -> gauge.setWidth(gauge.getWidth() + 10));

      // Using Bindings operator

      smaller.disableProperty().bind(
         lessThanOrEqual(gauge.widthProperty(), 0));      
      
      // Creating a binding from a lambda
      
      larger.disableProperty().bind(
         createBooleanBinding(
            () -> gauge.getWidth() >= 100, // This lambda is computed ...
            gauge.widthProperty())); // ... when this property changes

      Scene scene = new Scene(new HBox(10, smaller, pane, larger));
      stage.setScene(scene);
      stage.show();
   }
}
