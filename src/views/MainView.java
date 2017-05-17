package views;

import control.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import network.UDP_Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainView extends Application {

    private static String MenuViewFXML = "/fxml/Menu.fxml";
    private static String PlayFreeViewFXML = "/fxml/PlayFreeView.fxml";
    private static String PlayMidiFXML = "/fxml/PlayMidi.fxml";
    private static String ListeningFXML = "/fxml/Listening.fxml";

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        showMenuView(primaryStage);
    }

    public void showMenuView(Stage prevStage) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Menu View");
            Pane myPane;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MainView.MenuViewFXML));

            myPane = loader.load();


            Scene scene = new Scene(myPane);
            stage.setScene(scene);

            MenuController controller = loader.getController();
            controller.setPrevStage(stage);
            controller.setMainApp(this);

            prevStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPlayFreeView(Stage prevStage) {
        try {
            Stage stage = new Stage();
            stage.setTitle("PlayFree View");
            Pane myPane;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MainView.PlayFreeViewFXML));

            myPane = loader.load();

            Scene scene = new Scene(myPane);
            stage.setScene(scene);

            PlayFreeController controller = loader.getController();
            controller.setPrevStage(stage);
            controller.setMainApp(this);

            prevStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPlayMidiView(Stage prevStage) {
        try {
            Stage stage = new Stage();
            stage.setTitle("PlayMidi View");
            Pane myPane;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MainView.PlayMidiFXML));

            myPane = loader.load();

            Scene scene = new Scene(myPane);
            stage.setScene(scene);

            PlayMidiController controller = loader.getController();
            controller.setPrevStage(stage);
            controller.setMainApp(this);

            prevStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showListeningView(Stage prevStage) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Listening View");
            Pane myPane;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MainView.ListeningFXML));

            myPane = loader.load();

            Scene scene = new Scene(myPane);
            stage.setScene(scene);

            ListeningController controller = loader.getController();
            controller.setPrevStage(stage);
            controller.setMainApp(this);
            controller.setServer(new UDP_Server(5678));

            prevStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }


}
