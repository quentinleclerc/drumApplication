package control_view;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import views.MainView;

public class MenuController implements Initializable {
	
	@FXML
	Button playMidi;
	@FXML
	Button playFree;
	@FXML
	Button listening;

	private Stage prevStage;
	private MainView mainApp;

	public MenuController() {
		System.out.println("MenuController Controller initialized.");
	}

	public void setPrevStage(Stage stage){
		this.prevStage = stage;
	}

	@Override
	public void initialize(URL location, ResourceBundle rb) {
	}

	@FXML
	public void onPlayFree(){
		this.mainApp.showPlayFreeView(this.prevStage);
	}
	
	@FXML
	public void onPlayMidi() {
		this.mainApp.showPlayMidiView(this.prevStage);
	}

	@FXML
	public void onListening() {
		this.mainApp.showListeningView(this.prevStage);
	}

	public void setMainApp(MainView mainApp) {
		this.mainApp = mainApp;
	}
}