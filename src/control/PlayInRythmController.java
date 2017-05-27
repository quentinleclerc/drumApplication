package control;

import java.net.URL;
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
	@FXML
	private Circle caisseHautDroite;
	@FXML
	private Circle cymbaleBasGauche;
	@FXML
	private Circle caisseHautGauche;
	@FXML
	private Circle caisseBasDroite;
	@FXML
	private Circle cymbaleGauche;
	@FXML
	private Circle cymbaleDroite;
	@FXML
	private Ellipse pedale;
	@FXML
	private Pane fondPlayRythm;

	private Stage prevStage;
	private MainView mainApp;

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
	}

	@FXML
	void onClickCymbHautGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en haut à gauche");
		Circle circle = makeCircle(cymbaleGauche.getLayoutX(), cymbaleGauche.getRadius(), cymbaleGauche.getRadius(), 0.6);
		moveCircle(circle);
	}

	@FXML
	void onClickCymbHautDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en haut à droite");
		Circle circle = makeCircle(cymbaleDroite.getLayoutX(), cymbaleDroite.getRadius(), cymbaleDroite.getRadius(), 0.65);
		moveCircle(circle);
	}

	@FXML
	void onClickCymbBasGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la cymbale en bas à gauche");
		Circle circle = makeCircle(cymbaleBasGauche.getLayoutX(), cymbaleBasGauche.getRadius(), cymbaleBasGauche.getRadius(), 0.7);
		moveCircle(circle);
		/*new Thread() {
			public void run(){
				while(!circleInTarget(cymbaleBasGauche, circle)){
					System.out.println("NOPE");
				}
				System.out.println("CIRCLE IN TRAGET");
			}
		}.run();*/
	}

	@FXML
	void onClickCaisseBasGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en bas à gauche : ");
		Circle circle = makeCircle(caisseBasGauche.getLayoutX(), caisseBasGauche.getRadius(), caisseBasGauche.getRadius(), 0.75);
		moveCircle(circle);
	}

	@FXML
	void onClickCaisseBasDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en bas à droite");
		Circle circle = makeCircle(caisseBasDroite.getLayoutX(), caisseBasDroite.getRadius(), caisseBasDroite.getRadius(), 0.8);
		moveCircle(circle);
	}

	@FXML
	void onClickCaisseHautGauche(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en haut à gauche");
		Circle circle = makeCircle(caisseHautGauche.getLayoutX(), caisseHautGauche.getRadius(), caisseHautGauche.getRadius(), 0.85);
		moveCircle(circle);
	}

	@FXML
	void onClickCaisseHautDroite(MouseEvent event) {
		System.out.println("T'as cliqué sur la caisse en haut à droite");
		Circle circle = makeCircle(caisseHautDroite.getLayoutX(), caisseHautDroite.getRadius(), caisseHautDroite.getRadius(), 0.9);
		moveCircle(circle);
	}

	@FXML
	void onClickPedale(MouseEvent event) {
		System.out.println("T'as cliqué sur la pédale");
		//moveCircle(pedale.getLayoutX(), pedale.getRadius(), pedale.getRadius(), 0.1);
	}

	@FXML
	void onClickMenu(MouseEvent event) {
		this.mainApp.showMenuView(this.prevStage);
		// myController.setScreen(MainView.MenuViewID);
	}

	private Circle makeCircle(double x, double y, double radius, double opacity) {
		Circle circle = new Circle(x, y, radius, Color.gray(opacity, 0.5));
		circle.setEffect(new Lighting());
		
		circle.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
				if (circleInTarget(circle, this.caisseBasGauche)) {
					circle.setStroke(Color.ORANGE);
					// circle.setDisable(true);
					System.out.println("Circle in target");
				}
			});
		
		fondPlayRythm.getChildren().add(circle);
		return circle;
	}

	private void moveCircle(Circle circle){
	    Path path = new Path();
	    path.getElements().add(new MoveTo(circle.getCenterX(),circle.getCenterY()));
	    path.getElements().add(new VLineTo(900));
	    PathTransition pathTransition = new PathTransition();
	    pathTransition.setDuration(Duration.millis(4000));
	    pathTransition.setPath(path);
	    pathTransition.setNode(circle);
	    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
	    pathTransition.play();
	}
	
	public boolean circleInTarget(Circle target, Circle circle2){ 
		return target.getBoundsInParent().intersects(circle2.getBoundsInParent()); 
	}

}
