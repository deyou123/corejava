package uitask;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.concurrent.*;

import javafx.application.*;
import javafx.concurrent.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class TaskDemo extends Application
{
   private TextArea content = new TextArea("");
   private Label status = new Label();   
   private ExecutorService executor = Executors.newCachedThreadPool();
   private Task<Integer> task;
   private Button open = new Button("Open");
   private Button cancel = new Button("Cancel");
    
   public void start(Stage stage)
   {
      open.setOnAction(event -> read(stage));
      cancel.setOnAction(event -> 
         { 
            if (task != null) task.cancel();
         });
      cancel.setDisable(true);
      stage.setOnCloseRequest(event -> 
      { 
         if (task != null) task.cancel(); 
         executor.shutdown(); 
         Platform.exit(); 
      });

      HBox box = new HBox(10, open, cancel);
      VBox pane = new VBox(10, content, box, status);
      pane.setPadding(new Insets(10));
      stage.setScene(new Scene(pane));
      stage.setTitle("TaskDemo");
      stage.show();
   }

   private void read(Stage stage)
   {
      if (task != null) return;
      FileChooser chooser = new FileChooser();
      chooser.setInitialDirectory(new File(".."));
      File file = chooser.showOpenDialog(stage);
      if (file == null) return;
      content.setText("");
      task = new Task<>() 
         {
            public Integer call()
            {
               int lines = 0;
               try (Scanner in = new Scanner(file, StandardCharsets.UTF_8)) 
               {
                  while (!isCancelled() && in.hasNextLine())
                  { 
                     Thread.sleep(10); // Simulate work
                     String line = in.nextLine();
                     Platform.runLater(() -> 
                        content.appendText(line + "\n"));
                     lines++;
                     updateMessage(lines + " lines read");
                  }
               }
               catch (InterruptedException e)
               {
                  // Task was canceled in sleep
               }
               catch (IOException e)
               {
                  throw new UncheckedIOException(null, e);
               }
               return lines;
            }
         };
      executor.execute(task);
      task.setOnScheduled(event ->
         {
            cancel.setDisable(false);
            open.setDisable(true);            
         });
      task.setOnRunning(event -> 
         {
            status.setText("Running");
            status.textProperty().bind(task.messageProperty());
         });
      task.setOnFailed(event -> 
         { 
            cancel.setDisable(true);
            status.textProperty().unbind();
            status.setText("Failed due to " + task.getException());
            task = null;
            open.setDisable(false);            
         });
      task.setOnCancelled(event -> 
         { 
            cancel.setDisable(true);
            status.textProperty().unbind();
            status.setText("Canceled");  
            task = null;
            open.setDisable(false);            
         });
      task.setOnSucceeded(event -> 
         { 
            cancel.setDisable(true);
            status.textProperty().unbind();
            status.setText("Done reading " + task.getValue() + " lines");  
            task = null;
            open.setDisable(false);            
         });
   }
}
