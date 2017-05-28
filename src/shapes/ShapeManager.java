package shapes;


import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class ShapeManager {


    private Circle makeCircle(Circle target, double opacity) {
        double x = target.getLayoutX();
        double radius = target.getRadius();
        Circle circle = new Circle(x, radius, radius, Color.gray(opacity, 0.5));
        circle.setEffect(new Lighting());

        // fondPlayRythm.getChildren().add(circle);
        return circle;
    }

    private Ellipse makeEllipse(Ellipse target) {
        double x = target.getLayoutX();
        double radiusX = target.getRadiusX();
        double radiusY = target.getRadiusY();
        Ellipse ellipse = new Ellipse(x, radiusY, radiusY, radiusX);
        ellipse.setFill(Color.rgb(192, 192, 192, 0.2));
        ellipse.setEffect(new Lighting());


        // fondPlayRythm.getChildren().add(ellipse);
        return ellipse;
    }

    private void moveCircle(Circle circle, Circle target, Pane parent){
        Path path = new Path();
        path.getElements().add(new MoveTo(circle.getCenterX(),circle.getCenterY()));
        path.getElements().add(new VLineTo(target.getLayoutY())); //Distance (le centre du rond cible)
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
        pathTransition.setPath(path);
        pathTransition.setNode(circle);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parent.getChildren().remove(circle);
            }
        });
    }

    private void moveEllipse(Ellipse ellipse, Ellipse target, Pane parent){
        Path path = new Path();
        path.getElements().add(new MoveTo(ellipse.getCenterX(),ellipse.getCenterY()));
        path.getElements().add(new VLineTo(target.getLayoutY())); //Distance (pédale cible)
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
        pathTransition.setPath(path);
        pathTransition.setNode(ellipse);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               parent.getChildren().remove(ellipse);
            }
        });
    }


    private void moveShape(Shape shape, Shape target, Pane parent){
        Path path = new Path();
        path.getElements().add(new MoveTo(shape.getLayoutX(),shape.getLayoutY()));
        path.getElements().add(new VLineTo(target.getLayoutY())); //Distance (pédale cible)
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
        pathTransition.setPath(path);
        pathTransition.setNode(shape);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.play();
        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parent.getChildren().remove(shape);
            }
        });
    }

}
