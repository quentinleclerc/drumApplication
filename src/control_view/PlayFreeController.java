package control_view;

import control.NoteChannel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import midi.Event;
import midi.Merger;
import midi.SoundRecord;
import player.Drummer;
import player.PlayerSong;
import saver.SongWriter;
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
	@FXML
	private Pane fondPlayFree;

	private Stage prevStage;

	private MainView mainApp;

	private Drummer drummer;

	private SoundRecord record;

	private long time;

	private SongWriter songWriter;

	private Scene scene;

	private final static int VELOCITY = 100;


	public PlayFreeController() {
		System.out.println("PlayFreeController initialized.");
		this.drummer = new Drummer();
		this.songWriter = new SongWriter();
	}

	public void setPrevStage(Stage stage){
		this.prevStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle rb) {
		this.stopRecord.setDisable(true);
		// initializeKeyListener();
	}


	private void initializeKeyListener() {
		// this.scene.setOnKeyPressed((KeyEvent event) -> {
			//       });
	}

	@FXML
	void onKeyPressed(KeyEvent event) {
		switch (event.getCode()) {
			case SPACE :
				songWriter.receivedNote(Drummer.KICK,VELOCITY, 0);
				drummer.noteOn(Drummer.KICK, VELOCITY);
				break;
			case G :
				songWriter.receivedNote(Drummer.HIGH_TOM,VELOCITY, 0);
				drummer.noteOn(Drummer.HIGH_TOM, VELOCITY);
				break;
			case H :
				drummer.noteOn(Drummer.MIDDLE_TOM, VELOCITY);
				songWriter.receivedNote(Drummer.MIDDLE_TOM,VELOCITY, 0);
				break;
			case N :
				songWriter.receivedNote(Drummer.FLOOR_TOM,VELOCITY, 0);
				drummer.noteOn(Drummer.FLOOR_TOM, VELOCITY);
				break;
			case J :
				songWriter.receivedNote(Drummer.RIDE,VELOCITY, 0);
				drummer.noteOn(Drummer.RIDE, VELOCITY);
				break;
			case F :
				songWriter.receivedNote(Drummer.CRASH,VELOCITY, 0);
				drummer.noteOn(Drummer.CRASH, VELOCITY);
				break;
			case V :
				songWriter.receivedNote(Drummer.SNARE,VELOCITY, 0);
				drummer.noteOn(Drummer.SNARE, VELOCITY);
				break;
			case C :
				songWriter.receivedNote(Drummer.HITHAT,VELOCITY, 0);
				drummer.noteOn(Drummer.HITHAT, VELOCITY);
				break;
			case R :
				songWriter.saveSong();
				SoundRecord song = songWriter.getSong();
				Merger merge =new Merger();
				song = merge.merge(song, song);
				songWriter.reset();
				PlayerSong playerSong = new PlayerSong(song);
				new Thread(playerSong).start();
				break;
			default:
				System.out.println(event.getCode());
				break;
		}
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

	public void setScene(Scene scene) {
		this.scene = scene;
	}
}
