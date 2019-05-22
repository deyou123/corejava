package binding;

import javafx.application.*;
import javafx.beans.binding.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

/**
   @version 1.0 2017-12-29
   @author Cay Horstmann
*/
public class BindingDemo2 extends Application
{
   public void start(Stage stage)
   {      
      Circle circle = new Circle(100, 100, 100);
      circle.setFill(Color.RED);
      Pane pane = new Pane(circle);
      Scene scene = new Scene(pane);
      circle.centerXProperty().bind(
         Bindings.divide(scene.widthProperty(), 2));
      circle.centerYProperty().bind(
         Bindings.divide(scene.heightProperty(), 2));
      stage.setScene(scene);
      stage.show();
   }
}
