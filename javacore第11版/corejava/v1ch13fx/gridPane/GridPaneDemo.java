package gridPane;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class GridPaneDemo extends Application
{
   public void start(Stage stage)
   {
      final double rem = new Text("").getLayoutBounds().getHeight();

      GridPane pane = new GridPane();
      // Uncomment for debugging
      // pane.setGridLinesVisible(true);

      pane.setHgap(0.8 * rem);
      pane.setVgap(0.8 * rem);
      pane.setPadding(new Insets(0.8 * rem));
      Label usernameLabel = new Label("User name:");
      Label passwordLabel = new Label("Password:");
      TextField username = new TextField();
      PasswordField password = new PasswordField();

      Button okButton = new Button("Ok");
      Button cancelButton = new Button("Cancel");

      HBox buttons = new HBox(0.8 * rem); 
      buttons.getChildren().addAll(okButton, cancelButton);
      buttons.setAlignment(Pos.CENTER);
      // Uncomment for debugging
      // buttons.setStyle("-fx-border-color: red;");
      
      pane.add(usernameLabel, 0, 0);
      pane.add(username, 1, 0);
      pane.add(passwordLabel, 0, 1);
      pane.add(password, 1, 1);
      pane.add(buttons, 0, 2, 2, 1);
      
      GridPane.setHalignment(usernameLabel, HPos.RIGHT);
      GridPane.setHalignment(passwordLabel, HPos.RIGHT);
      stage.setScene(new Scene(pane));
      stage.show();
   }
}
