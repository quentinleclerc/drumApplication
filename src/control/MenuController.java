package control;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.applet.Main;
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
		// myController.setScreen(MainView.PlayFreeID);
	}
	
	@FXML
	public void onPlayMidi() {
		this.mainApp.showPlayMidiView(this.prevStage);
		// myController.setScreen(MainView.PlayMidiID);
	}

	@FXML
	public void onListening() {
		this.mainApp.showListeningView(this.prevStage);
		// myController.setScreen(MainView.ListeningID);
	}

	public void setMainApp(MainView mainApp) {
		this.mainApp = mainApp;
	}
}
