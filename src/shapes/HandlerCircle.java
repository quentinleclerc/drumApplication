package shapes;

import javafx.animation.PathTransition;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;

public class HandlerCircle {

	
	public HandlerCircle(){
	}
	
	private boolean circleInTarget(Shape target, Shape circle){ 
		return target.getBoundsInParent().intersects(circle.getBoundsInParent());
	}
	
	public Circle makeCircle(Circle target) {
		double x = target.getLayoutX() + target.getParent().getLayoutX();
		// System.out.println("Make Circle X = " + x);
		double radius = target.getRadius();
		double opacity = (Math.random()*0.5 + 0.5);
		Circle circle = new Circle(x, radius, radius, Color.gray(opacity, 0.5));
		circle.setEffect(new Lighting());
		return circle;
	}

	public Ellipse makeEllipse(Ellipse target) {
		double x = target.getLayoutX() + target.getParent().getLayoutX();
		double radiusX = target.getRadiusX();
		double radiusY = target.getRadiusY();
		Ellipse ellipse = new Ellipse(x, radiusY, radiusY, radiusX);
		ellipse.setFill(Color.rgb(192, 192, 192, 0.2));
		ellipse.setEffect(new Lighting());
		return ellipse;
	}

	public PathTransition moveCircle(Circle circle, Circle target){
		circle.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
			if (circleInTarget(target, circle)) {
				circle.setStroke(Color.GREEN);
			}
		});
		
		Path path = new Path();
		path.getElements().add(new MoveTo(circle.getCenterX(),circle.getCenterY()));
		path.getElements().add(new VLineTo(target.getLayoutY()+target.getParent().getLayoutY())); //Distance (le centre du rond cible)
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
		pathTransition.setPath(path);
		pathTransition.setNode(circle);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.play();
		return pathTransition;
	}

	public PathTransition moveEllipse(Ellipse ellipse, Ellipse pedale){
		ellipse.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
			if (circleInTarget(pedale, ellipse)) {
				ellipse.setStroke(Color.GREEN);
			}
		});
		
		Path path = new Path();
		path.getElements().add(new MoveTo(ellipse.getCenterX(),ellipse.getCenterY()));
		path.getElements().add(new VLineTo(pedale.getLayoutY()+pedale.getParent().getLayoutY())); //Distance (pédale cible)
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
		pathTransition.setPath(path);
		pathTransition.setNode(ellipse);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.play();
		return pathTransition;

	}
}
