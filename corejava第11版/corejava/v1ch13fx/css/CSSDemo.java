package css;

import java.io.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class CSSDemo extends Application
{
   public void start(Stage stage)
   {
      try
      {
         Parent root = FXMLLoader.load(getClass().getResource("dialog.fxml"));
         root.lookup("#username").getStyleClass().add("highlight");
         Scene scene = new Scene(root);
         scene.getStylesheets().add("css/scene1.css");
         stage.setScene(scene);
         stage.setTitle("CSSDemo");
         stage.show();
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
         Platform.exit();
      }
   }
}

