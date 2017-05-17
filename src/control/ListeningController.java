package control;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import network.UDP_Server;

import views.MainView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListeningController implements Initializable {

    @FXML
    Button backToMenu;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;

    private Stage prevStage;

    private MainView mainApp;

    private UDP_Server server;

    public ListeningController() {
        System.out.println("Listening initialized.");
    }

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        stopButton.setDisable(true);
    }

    @FXML
    void onClickMenu(MouseEvent event) {
        this.mainApp.showMenuView(this.prevStage);
        // myController.setScreen(MainView.MenuViewID);
    }

    @FXML
    void onClickStart(MouseEvent event) {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        Thread threadServeur = new Thread(server);
        threadServeur.start();
    }

    @FXML
    void onClickStop(MouseEvent event) {
        stopButton.setDisable(true);
        startButton.setDisable(false);

        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    public void setServer(UDP_Server server) {
        this.server = server;
    }
}
