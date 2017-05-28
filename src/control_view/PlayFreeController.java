package control_view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import midi.Event;
import midi.SoundRecord;
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
	@FXML
	private Button startRecord;
	@FXML
	private Button stopRecord;
	@FXML
	private TextField recordName;

	private Stage prevStage;

	private MainView mainApp;

	private Drummer drummer;

	private SoundRecord record;

	private long time;

	public PlayFreeController() {
		System.out.println("PlayFreeController initialized.");
		this.drummer = new Drummer();
	}

	public void setPrevStage(Stage stage){
		this.prevStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle rb) {
		this.stopRecord.setDisable(true);
	}

	@FXML
	public void onStartRecord(MouseEvent event) {
		this.record = new SoundRecord(recordName.getText());
		this.recordName.setPromptText("Enter the name of your record here");
		this.recordName.clear();
		this.recordName.setDisable(true);
		this.stopRecord.setDisable(false);
		this.startRecord.setDisable(true);
	}

	@FXML
	public void onStopRecord(MouseEvent event) {
		// TODO : Save record in file
		this.stopRecord.setDisable(true);
		this.startRecord.setDisable(false);
		this.recordName.setDisable(false);
	}


	@FXML
	void onClickCymbHautGauche(MouseEvent event) {
		drummer.noteOn(Drummer.CRASH, 100);
		this.record.add(new Event(this.time, Drummer.CRASH, 100));
		System.out.println("T'as cliqué sur la cymbale en haut à gauche");
	}

	@FXML
	void onClickCymbHautDroite(MouseEvent event) {
		drummer.noteOn(Drummer.RIDE, 100);

		this.record.add(new Event(this.time, Drummer.RIDE, 100));
		System.out.println("T'as cliqué sur la cymbale en haut à droite");
	}

	@FXML
	void onClickCymbBasGauche(MouseEvent event) {
		drummer.noteOn(Drummer.HITHAT, 100);
		this.record.add(new Event(this.time, Drummer.HITHAT, 100));
		System.out.println("T'as cliqué sur la cymbale en bas à gauche");
	}

	@FXML
	void onClickCaisseBasGauche(MouseEvent event) {
		drummer.noteOn(Drummer.SNARE, 100);
		this.record.add(new Event(this.time, Drummer.SNARE, 100));
		System.out.println("T'as cliqué sur la caisse en bas à gauche");
	}

	@FXML
	void onClickCaisseBasDroite(MouseEvent event) {
		drummer.noteOn(Drummer.FLOOR_TOM, 100);
		this.record.add(new Event(this.time, Drummer.FLOOR_TOM, 100));
		System.out.println("T'as cliqué sur la caisse en bas à droite");
	}

	@FXML
	void onClickCaisseHautGauche(MouseEvent event) {
		drummer.noteOn(Drummer.HIGH_TOM, 100);
		this.record.add(new Event(this.time, Drummer.HIGH_TOM, 100));
		System.out.println("T'as cliqué sur la caisse en haut à gauche");
	}

	@FXML
	void onClickCaisseHautDroite(MouseEvent event) {
		drummer.noteOn(Drummer.MIDDLE_TOM, 100);
		this.record.add(new Event(this.time, Drummer.MIDDLE_TOM, 100));
		System.out.println("T'as cliqué sur la caisse en haut à droite");
	}

	@FXML
	void onClickPedale(MouseEvent event) {
		drummer.noteOn(Drummer.KICK, 100);
		this.record.add(new Event(this.time, Drummer.KICK, 100));
		System.out.println("T'as cliqué sur la pédale");
	}

	@FXML
	void onClickMenu(MouseEvent event) {
		this.mainApp.showMenuView(this.prevStage);
	}


	public void setMainApp(MainView mainApp) {
		this.mainApp = mainApp;
	}
}
