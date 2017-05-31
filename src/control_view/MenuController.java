
package control_view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import views.MainView;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

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
	public void onListening() {
		this.mainApp.showListeningView(this.prevStage);
	}

	public void setMainApp(MainView mainApp) {
		this.mainApp = mainApp;
	}
}
