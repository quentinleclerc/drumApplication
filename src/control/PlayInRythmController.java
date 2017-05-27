package control;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.stage.Stage;
import javafx.util.Duration;
import views.MainView;

public class PlayInRythmController implements Initializable{
	@FXML
	private Circle caisseBasGauche;
	private static final int int1 = 38;
	@FXML
	private Circle caisseHautDroite;
	private static final int int2 = 43;
	@FXML
	private Circle cymbaleBasGauche;
	private static final int int3 = 42;
	@FXML
	private Circle caisseHautGauche;
	private static final int int4 = 50;
	@FXML
	private Circle caisseBasDroite;
	private static final int int5 = 41;
	@FXML
	private Circle cymbaleGauche;
	private static final int int6 = 49;
	@FXML
	private Circle cymbaleDroite;
	private static final int int7 = 51;
	@FXML
	private Ellipse pedale;
	private static final int int8 = 35;
	
	@FXML
	private Pane fondPlayRythm;
	
	private Stage prevStage;
	private MainView mainApp;
	
	private Map<Integer, Double> kickDistance = new HashMap<Integer, Double>();

	public PlayInRythmController() {
		System.out.println("PlayInRythmController initialized.");

	}

	public void setPrevStage(Stage stage){
		this.prevStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	public void setMainApp(MainView mainApp) {
		this.mainApp = mainApp;
		kickDistance.put(int1, caisseBasGauche.getLayoutY() - caisseBasGauche.getRadius());
		kickDistance.put(int2, caisseHautDroite.getLayoutY() - caisseHautDroite.getRadius());
		kickDistance.put(int3, cymbaleBasGauche.getLayoutY() - cymbaleBasGauche.getRadius());
		kickDistance.put(int4, caisseHautGauche.getLayoutY() - caisseHautGauche.getRadius());
		kickDistance.put(int5, caisseBasDroite.getLayoutY() - caisseBasDroite.getRadius());
		kickDistance.put(int6, cymbaleGauche.getLayoutY() - cymbaleGauche.getRadius());
		kickDistance.put(int7, cymbaleDroite.getLayoutY() - cymbaleDroite.getRadius());
		kickDistance.put(int8, pedale.getLayoutY() - pedale.getRadiusY());
		printDistance();
	}

	@FXML
	void onClickCymbHautGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en haut à gauche");
		Circle circle = makeCircle(cymbaleGauche, 0.6);
		moveCircle(circle, cymbaleGauche);
	}

	@FXML
	void onClickCymbHautDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en haut à droite");
		Circle circle = makeCircle(cymbaleDroite, 0.65);
		moveCircle(circle, cymbaleDroite);
	}

	@FXML
	void onClickCymbBasGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en bas à gauche");
		Circle circle = makeCircle(cymbaleBasGauche, 0.7);
		moveCircle(circle, cymbaleBasGauche);
	}

	@FXML
	void onClickCaisseBasGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en bas à gauche : ");
		Circle circle = makeCircle(caisseBasGauche, 0.75);
		moveCircle(circle, caisseBasGauche);
	}

	@FXML
	void onClickCaisseBasDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en bas à droite");
		Circle circle = makeCircle(caisseBasDroite, 0.8);
		moveCircle(circle, caisseBasDroite);
	}

	@FXML
	void onClickCaisseHautGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en haut à gauche");
		Circle circle = makeCircle(caisseHautGauche, 0.85);
		moveCircle(circle, caisseHautGauche);
	}

	@FXML
	void onClickCaisseHautDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en haut à droite");
		Circle circle = makeCircle(caisseHautDroite, 0.9);
		moveCircle(circle, caisseHautDroite);
	}

	@FXML
	void onClickPedale(MouseEvent event) {
		System.out.println("T'as cliqué sur la pédale");
		Ellipse ellipse = makeEllipse(pedale);
		moveEllipse(ellipse);
	}

	@FXML
	void onClickMenu(MouseEvent event) {
		this.mainApp.showMenuView(this.prevStage);
		// myController.setScreen(MainView.MenuViewID);
	}

	private Circle makeCircle(Circle target, double opacity) {
		double x = target.getLayoutX();
		double radius = target.getRadius();
		Circle circle = new Circle(x, radius, radius, Color.gray(opacity, 0.5));
		circle.setEffect(new Lighting());
		
		circle.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
				if (circleInTarget(target, circle)) {
					circle.setStroke(Color.RED);
					System.out.println("Circle in target");
				}
			});
		
		fondPlayRythm.getChildren().add(circle);
		return circle;
	}
    
	private Ellipse makeEllipse(Ellipse target) {
		double x = target.getLayoutX();
		double radiusX = target.getRadiusX();
		double radiusY = target.getRadiusY();
		Ellipse ellipse = new Ellipse(x, radiusY, radiusY, radiusX);
		ellipse.setFill(Color.rgb(192, 192, 192, 0.2));
		ellipse.setEffect(new Lighting());
		ellipse.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
				if (ellipseInTarget(target, ellipse)) {
					ellipse.setStroke(Color.RED);
					System.out.println("Ellipse in target");
				}
			});
		
		fondPlayRythm.getChildren().add(ellipse);
		return ellipse;
	}

	private void moveCircle(Circle circle, Circle target){
	    Path path = new Path();
	    path.getElements().add(new MoveTo(circle.getCenterX(),circle.getCenterY()));
	    path.getElements().add(new VLineTo(target.getLayoutY())); //Distance (le centre du rond cible)
	    PathTransition pathTransition = new PathTransition();
	    pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
	    pathTransition.setPath(path);
	    pathTransition.setNode(circle);
	    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	    pathTransition.play();
	}
	
	private void moveEllipse(Ellipse ellipse){
	    Path path = new Path();
	    path.getElements().add(new MoveTo(ellipse.getCenterX(),ellipse.getCenterY()));
	    path.getElements().add(new VLineTo(pedale.getLayoutY())); //Distance (pédale cible)
	    PathTransition pathTransition = new PathTransition();
	    pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
	    pathTransition.setPath(path);
	    pathTransition.setNode(ellipse);
	    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	    pathTransition.play();
	}
	
	
	public boolean circleInTarget(Circle target, Circle circle2){ 
		return target.getBoundsInParent().intersects(circle2.getBoundsInParent()); 
	}

	public boolean ellipseInTarget(Ellipse target, Ellipse ellipse){ 
		return target.getBoundsInParent().intersects(ellipse.getBoundsInParent()); 
	}
	
	public Double getDistance(int key){
		return kickDistance.get(key);
	}
	
	public void printDistance(){
		for(int i : kickDistance.keySet()){
			System.out.println(i + ": Valeur " + kickDistance.get(i));
		}
	}
}
