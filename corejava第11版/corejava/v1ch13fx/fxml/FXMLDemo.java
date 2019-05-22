package fxml;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.stage.*;

/**
   @version 1.0 2017-12-29
   @author Cay Horstmann
*/
public class FXMLDemo extends Application implements Initializable
{
   @FXML private TextField username;
   @FXML private PasswordField password; 
   @FXML private Button okButton; 
   @FXML private Button cancelButton; 
   
   public void initialize(URL url, ResourceBundle rb)
   {
      okButton.setOnAction(event ->
         {
            Alert alert = new Alert(AlertType.INFORMATION,
               "Verifying " + username.getText() + ":" + password.getText());
            alert.showAndWait();
         });        
      cancelButton.setOnAction(event ->
         {
            username.setText("");
            password.setText("");
         });
   }

   public void start(Stage stage)
   {
      try
      {
         Parent root = FXMLLoader.load(
            getClass().getResource("dialog.fxml"));
         stage.setScene(new Scene(root));
         stage.setTitle("FXMLDemo");
         stage.show();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }   
}

