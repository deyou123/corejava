package loggingfx;

import java.io.*;
import java.nio.file.*;
import java.util.logging.*;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.stage.FileChooser.*;

/**
 * A modification of the image viewer program that logs various events.
 * @version 1.10 2017-12-14
 * @author Cay Horstmann
 */
public class LoggingImageViewer extends Application
{
   private static final int MIN_SIZE = 400;
   private Logger logger = Logger.getLogger("com.horstmann.corejava");

   public void start(Stage stage) throws IOException
   {
      if (System.getProperty("java.util.logging.config.class") == null
            && System.getProperty("java.util.logging.config.file") == null)
      {
         try
         {
            logger.setLevel(Level.ALL);
            final int LOG_ROTATION_COUNT = 10;
            Handler handler = new FileHandler("%h/LoggingImageViewer.log", 0,
                  LOG_ROTATION_COUNT);
            logger.addHandler(handler);
            handler.setLevel(Level.ALL);
         }
         catch (IOException e)
         {
            logger.log(Level.SEVERE, "Can't create log file handler", e);
         }
      }

      BorderPane pane = new BorderPane();
      MenuBar bar = new MenuBar();
      pane.setTop(bar);
      Menu fileMenu = new Menu("File");
      bar.getMenus().add(fileMenu);
      MenuItem openItem = new MenuItem("Open");
      openItem.setOnAction(event -> load(stage, pane));
      MenuItem exitItem = new MenuItem("Exit");
      exitItem.setOnAction(event -> Platform.exit());
      fileMenu.getItems().addAll(openItem, exitItem);
      stage.setScene(new Scene(pane, MIN_SIZE, MIN_SIZE));
      stage.setTitle("ImageViewer");

      logger.addHandler(new WindowHandler(stage, Level.ALL));
      logger.fine("Showing stage");
      stage.show();
   }

   /**
    * Loads an image.
    * @param stage the stage above which to place the file chooser
    * @param pane the pane into which to place the image view
    */
   public void load(Stage stage, BorderPane pane)
   {
      logger.entering("LoggingImageViewerFrame", "load",
            new Object[] { stage, pane });
      FileChooser fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
            new ExtensionFilter("All Files", "*.*"));
      File file = fileChooser.showOpenDialog(stage);
      logger.fine("Selected file: " + file);
      if (file != null)
      {
         try
         {
            Path path = file.toPath();
            Image image = new Image(Files.newInputStream(path));
            pane.setCenter(new ImageView(image));
         }
         catch (IOException e)
         {
            logger.log(Level.FINE, "File not found", e);
            Alert alert = new Alert(AlertType.ERROR,
               "Cannot open file.");
            alert.showAndWait();
         }
      }
      logger.exiting("LoggingImageViewerFrame", "load");
   }
}

/**
 * A handler for displaying log records in a window.
 */
class WindowHandler extends StreamHandler
{
   public WindowHandler(Stage parent, Level level)
   {
      setLevel(level);
      TextArea output = new TextArea();
      output.setEditable(false);
      Stage stage = new Stage();
      stage.setScene(new Scene(output, 400, 200));
      stage.setTitle("Log messages");
      stage.setX(100);
      stage.setY(100);

      stage.show();
      setOutputStream(new OutputStream()
      {
         public void write(int b)
         {
            // not called
         }

         public void write(byte[] b, int off, int len)
         {
            output.appendText(new String(b, off, len));
         }
      });
   }

   public void publish(LogRecord record)
   {
      super.publish(record);
      flush();
   }
}
