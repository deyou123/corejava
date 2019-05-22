package dialogs;

import java.io.*;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
   @version 1.0 2017-12-29
   @author Cay Horstmann
*/
public class DialogDemo extends Application
{
   public void start(Stage stage)
   {
      TextArea textArea = new TextArea();
      
      Button information = new Button("Information");
      information.setOnAction(event ->
         {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
               "Everything is fine.");
            alert.showAndWait();
         });

      Button warning = new Button("Warning");
      warning.setOnAction(event ->
         {
            Alert alert = new Alert(Alert.AlertType.WARNING,
               "We have a problem.");
            alert.showAndWait();
         });

      Button error = new Button("Error");
      error.setOnAction(event ->
         {
            Alert alert = new Alert(Alert.AlertType.ERROR,
               "This looks really bad.");            
            alert.showAndWait();
         });

      Button confirmation = new Button("Confirmation");
      confirmation.setOnAction(event ->
         {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
               "Are you sure?");
            if (alert.showAndWait().get() == ButtonType.OK)
               textArea.appendText("Confirmed\n");
            else
               textArea.appendText("Canceled\n");
         });
                  
      Button multipleButtons = new Button("Multiple Buttons");
      multipleButtons.setOnAction(event ->
         {
            Alert alert = new Alert(Alert.AlertType.NONE,
               "Now what?",
               ButtonType.NEXT, ButtonType.PREVIOUS, ButtonType.FINISH);
            alert.setTitle("Multiple Buttons");
            textArea.appendText(alert.showAndWait() + "\n");
         });      
      
      Button expandableContent = new Button("Expandable Content");
      expandableContent.setOnAction(event ->
         {
            Throwable t = new NullPointerException("Ugh--a null");
            Alert alert = new Alert(Alert.AlertType.ERROR,
               t.getMessage());
            alert.setHeaderText("Exception");
            alert.setGraphic(new ImageView("dialogs/bomb.png"));

            TextArea stackTrace = new TextArea();
            StringWriter out = new StringWriter();
            t.printStackTrace(new PrintWriter(out));
            stackTrace.setText(out.toString());
            alert.getDialogPane().setExpandableContent(stackTrace);

            textArea.appendText(alert.showAndWait() + "\n");
         });

      Button textInput = new Button("Text input");
      textInput.setOnAction(event ->
         {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("What is your name?");
            dialog.showAndWait().ifPresentOrElse(
               result -> textArea.appendText("Name: " + result + "\n"),
               () -> textArea.appendText("Canceled\n"));
         });

      Button choiceDialog = new Button("Choice Dialog");
      choiceDialog.setOnAction(event ->
         {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("System",
               Font.getFamilies());
            dialog.setHeaderText("Pick a font.");
            dialog.showAndWait().ifPresentOrElse(
               result -> textArea.appendText("Selected: " + result + "\n"),
               () -> textArea.appendText("Canceled\n"));
         });
      
      Button fileChooser = new Button("File Chooser");
      fileChooser.setOnAction(event ->
         {
            FileChooser dialog = new FileChooser();
            dialog.setInitialDirectory(new File("menu"));
            dialog.setInitialFileName("untitled.gif");
            dialog.getExtensionFilters().addAll(
               new FileChooser.ExtensionFilter("GIF images", "*.gif"),
               new FileChooser.ExtensionFilter("JPEG images", "*.jpg", "*.jpeg"));
            File result = dialog.showSaveDialog(stage);
            if (result == null)
               textArea.appendText("Canceled\n");
            else
               textArea.appendText("Selected: " + result + "\n");
         });
            
      Button directoryChooser = new Button("Directory Chooser");
      directoryChooser.setOnAction(event ->
         {
            DirectoryChooser dialog = new DirectoryChooser();
            File result = dialog.showDialog(stage);
            if (result == null)
               textArea.appendText("Canceled\n");
            else
               textArea.appendText("Selected: " + result + "\n");
         });

      final double rem = new Text("").getLayoutBounds().getHeight();
      VBox buttons = new VBox(0.8 * rem,
         information, warning, error, confirmation,
         multipleButtons, expandableContent, textInput, choiceDialog,
         fileChooser, directoryChooser); 
      buttons.setPadding(new Insets(0.8 * rem));

      HBox root = new HBox(textArea, buttons);      

      stage.setScene(new Scene(root));
      stage.setTitle("DialogDemo");
      stage.show();
   }
}
