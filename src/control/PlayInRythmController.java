package control;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import midi.Event;
import player.ThreadCircle;
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
	private static Pane fondPlayRythm;

	private Stage prevStage;

	private MainView mainApp;

	private Map<Integer, Double> kickDistance = new HashMap<>();
	private Map<Integer, Circle> liaisonToms = new HashMap<>();

	public PlayInRythmController() {
		System.out.println("PlayInRythmController initialized.");

	}

	public void setPrevStage(Stage stage){
		this.prevStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		liaisonToms.put(38, this.caisseBasGauche);
		liaisonToms.put(43, this.caisseHautDroite);
		liaisonToms.put(42, this.cymbaleBasGauche);
		liaisonToms.put(50, this.caisseHautGauche);
		liaisonToms.put(41, this.caisseBasDroite);
		liaisonToms.put(49, this.cymbaleGauche);
		liaisonToms.put(51, this.cymbaleDroite);
		
		kickDistance.put(int1, caisseBasGauche.getLayoutY() - caisseBasGauche.getRadius());
		kickDistance.put(int2, caisseHautDroite.getLayoutY() - caisseHautDroite.getRadius());
		kickDistance.put(int3, cymbaleBasGauche.getLayoutY() - cymbaleBasGauche.getRadius());
		kickDistance.put(int4, caisseHautGauche.getLayoutY() - caisseHautGauche.getRadius());
		kickDistance.put(int5, caisseBasDroite.getLayoutY() - caisseBasDroite.getRadius());
		kickDistance.put(int6, cymbaleGauche.getLayoutY() - cymbaleGauche.getRadius());
		kickDistance.put(int7, cymbaleDroite.getLayoutY() - cymbaleDroite.getRadius());
		kickDistance.put(int8, pedale.getLayoutY() - pedale.getRadiusY());
		//printDistance();

	}

	public void setMainApp(MainView mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	void onClickCymbHautGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en haut à gauche");
	}

	@FXML
	void onClickCymbHautDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en haut à droite");
	}

	@FXML
	void onClickCymbBasGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en bas à gauche");
	}

	@FXML
	void onClickCaisseBasGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en bas à gauche : ");
	}

	@FXML
	void onClickCaisseBasDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en bas à droite");
	}

	@FXML
	void onClickCaisseHautGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en haut à gauche");
	}

	@FXML
	void onClickCaisseHautDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en haut à droite");
	}

	@FXML
	void onClickPedale(MouseEvent event) {
		System.out.println("T'as cliqué sur la pédale");
	}

	@FXML
	void onClickMenu(MouseEvent event) {
		this.mainApp.showMenuView(this.prevStage);
	}
	
	@FXML
	void onClickPlay(MouseEvent event) {
		Thread tC = new Thread (new ThreadCircle(this.kickDistance, this.liaisonToms, this.pedale));
		tC.start();
	}

	/*public Circle makeCircle(Circle target, double opacity) {
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

	public Ellipse makeEllipse(Ellipse target) {
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

	public PathTransition moveCircle(Circle circle, Circle target){
		Path path = new Path();
		path.getElements().add(new MoveTo(circle.getCenterX(),circle.getCenterY()));
		path.getElements().add(new VLineTo(target.getLayoutY())); //Distance (le centre du rond cible)
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
		pathTransition.setPath(path);
		pathTransition.setNode(circle);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.play();
		return pathTransition;
	}

	public PathTransition moveEllipse(Ellipse ellipse){
		Path path = new Path();
		path.getElements().add(new MoveTo(ellipse.getCenterX(),ellipse.getCenterY()));
		path.getElements().add(new VLineTo(pedale.getLayoutY())); //Distance (pédale cible)
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(1000)); //Temps (en ms)
		pathTransition.setPath(path);
		pathTransition.setNode(ellipse);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.play();
		return pathTransition;

	}

	private void removeCircle(PathTransition pathTransition, Shape sh){
		pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fondPlayRythm.getChildren().remove(sh);
			}
		});

	}

	public boolean circleInTarget(Circle target, Circle circle2){ 
		return target.getBoundsInParent().intersects(circle2.getBoundsInParent());
	}

	public boolean ellipseInTarget(Ellipse target, Ellipse ellipse){ 
		return target.getBoundsInParent().intersects(ellipse.getBoundsInParent()); 
	}*/

	public Double getDistance(int key){
		return kickDistance.get(key);
	}

	public void printDistance(){
		for(int i : kickDistance.keySet()){
			System.out.println(i + ": Valeur " + kickDistance.get(i));
		}
	}
	
	public Ellipse getPedale(){
		return pedale;
	}
	
	public static void addShape(Shape e){
		if(e==null){
			System.out.println("NUUUUUUUUUUUUUUULLLLLLLLLLL");
		}
		else{
			fondPlayRythm.getChildren().add(e);
		}
	}
	
	public static void removeShape(Shape e){
		fondPlayRythm.getChildren().remove(e);
	}


}
