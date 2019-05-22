package mouse;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

/**
   @version 1.40 2017-12-27
   @author Cay Horstmann
*/
public class MouseTest extends Application
{
   private static final int PREFERRED_WIDTH = 300;
   private static final int PREFERRED_HEIGHT = 200;
   private static final int RADIUS = 5;
   private Scene scene;
   private Pane root;
   private Circle selected;

   private Circle makeDot(double x, double y)
   {
      Circle dot = new Circle(x, y, RADIUS);
      dot.setOnMouseEntered(event ->
         scene.setCursor(Cursor.CROSSHAIR));
      dot.setOnMouseExited(event ->
         scene.setCursor(Cursor.DEFAULT));
      dot.setOnMouseDragged(event ->
         {
            dot.setCenterX(event.getX());
            dot.setCenterY(event.getY());
         });
      dot.setOnMousePressed(event ->
         {
            if (event.getClickCount() > 1)
            {
               root.getChildren().remove(selected);
               select(null);
            }
            else
            {
               select(dot);
            }
            event.consume();
         });
      
      dot.setOnKeyPressed(event ->
         {
            KeyCode code = event.getCode();
            int distance = event.isShiftDown() ? 10 : 1;            
            if (code == KeyCode.DELETE)
               root.getChildren().remove(dot);
            else if (code == KeyCode.UP)
               dot.setCenterY(dot.getCenterY() - distance);
            else if (code == KeyCode.DOWN)
               dot.setCenterY(dot.getCenterY() + distance);
            else if (code == KeyCode.LEFT)
               dot.setCenterX(dot.getCenterX() - distance);
            else if (code == KeyCode.RIGHT)
               dot.setCenterX(dot.getCenterX() + distance);
         });
      
      return dot;
   }

   private void select(Circle dot)
   {
      if (selected == dot) return;
      if (selected != null) selected.setFill(Color.BLACK);
      selected = dot;
      if (selected != null)
      {
         selected.requestFocus();
         selected.setFill(Color.RED);
      }
   }
   
   public void start(Stage stage)
   {
      root = new Pane();
      root.setOnMousePressed(event ->
         {
            double x = event.getX();
            double y = event.getY();
            Circle dot = makeDot(x, y);
            root.getChildren().add(dot);
            select(dot);
         });
      scene = new Scene(root);
      root.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
      stage.setScene(scene);
      stage.setTitle("MouseTest");
      stage.show();
   }
}
