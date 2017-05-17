package control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import player.CustomSynthesizer;
import player.Drummer;
import views.MainView;

public class PlayFreeController implements Initializable {

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

	private Stage prevStage;

	private MainView mainApp;

	private Drummer drummer;

	public PlayFreeController() {
		System.out.println("PlayFreeController initialized.");

		CustomSynthesizer cs = new CustomSynthesizer();
		URL resource = getClass().getResource("/soundbanks/sdb.sf2");
		String fileName = resource.getFile();
		File file = new File(fileName);

		cs.loadSoundbank(file);

		this.drummer = new Drummer(cs);
	}

	public void setPrevStage(Stage stage){
		this.prevStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle rb) {
	}

	@FXML
	void onClickCymbHautGauche(MouseEvent event) {
		drummer.noteOn(Drummer.CRASH, 100);
		System.out.println("T'as cliqué sur la cymbale en haut à gauche");
	}

	@FXML
	void onClickCymbHautDroite(MouseEvent event) {
		drummer.noteOn(Drummer.RIDE, 100);
		System.out.println("T'as cliqué sur la cymbale en haut à droite");
	}

	@FXML
	void onClickCymbBasGauche(MouseEvent event) {
		drummer.noteOn(Drummer.HITHAT, 100);
		System.out.println("T'as cliqué sur la cymbale en bas à gauche");
	}

	@FXML
	void onClickCaisseBasGauche(MouseEvent event) {
		drummer.noteOn(Drummer.SNARE, 100);
		System.out.println("T'as cliqué sur la caisse en bas à gauche");
	}

	@FXML
	void onClickCaisseBasDroite(MouseEvent event) {
		drummer.noteOn(Drummer.FLOOR_TOM, 100);
		System.out.println("T'as cliqué sur la caisse en bas à droite");
	}

	@FXML
	void onClickCaisseHautGauche(MouseEvent event) {
		drummer.noteOn(Drummer.HIGH_TOM, 100);
		System.out.println("T'as cliqué sur la caisse en haut à gauche");
	}

	@FXML
	void onClickCaisseHautDroite(MouseEvent event) {
		drummer.noteOn(Drummer.MIDDLE_TOM, 100);
		System.out.println("T'as cliqué sur la caisse en haut à droite");
	}

	@FXML
	void onClickPedale(MouseEvent event) {
		drummer.noteOn(Drummer.KICK, 100);
		System.out.println("T'as cliqué sur la pédale");
	}

	@FXML
	void onClickMenu(MouseEvent event) {
		this.mainApp.showMenuView(this.prevStage);
		// myController.setScreen(MainView.MenuViewID);
	}


	public void setMainApp(MainView mainApp) {
		this.mainApp = mainApp;
	}
}
