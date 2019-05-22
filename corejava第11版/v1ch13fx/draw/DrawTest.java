package draw;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;

/**
   @version 1.4 2017-12-23
   @author Cay Horstmann
*/
public class DrawTest extends Application
{
   private static final int PREFERRED_WIDTH = 400;
   private static final int PREFERRED_HEIGHT = 400;

   public void start(Stage stage)
   {
      double leftX = 100;
      double topY = 100;
      double width = 200;
      double height = 150;

      Rectangle rect = new Rectangle(leftX, topY, width, height);
      rect.setFill(Color.TRANSPARENT);
      rect.setStroke(Color.BLACK);
      // an ellipse touching the rectangle
      double centerX = leftX + width / 2;
      double centerY = topY + height / 2;
      Ellipse ellipse = new Ellipse(centerX, centerY, width / 2, height / 2);
      ellipse.setFill(Color.PEACHPUFF);
      // a diagonal line
      Line diagonal = new Line(leftX, topY,
         leftX + width, topY + height);
      // a circle with the same center as the ellipse
      double radius = 150;   
      Circle circle = new Circle(centerX, centerY, radius);
      circle.setFill(Color.TRANSPARENT);
      circle.setStroke(Color.RED);
      Pane root = new Pane(rect, ellipse, diagonal, circle);
      root.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
      stage.setScene(new Scene(root));
      stage.setTitle("DrawTest");
      stage.show();
   }
}
