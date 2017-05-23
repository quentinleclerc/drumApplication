package views;

import control.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import midi.Event;
import midi.SoundRecord;
import midi.Trainer;
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

            ArrayList<SoundRecord> recordsArray =createRecords();
            ObservableList<SoundRecord> records = FXCollections.observableArrayList(recordsArray);

            ListeningController controller = loader.getController();
            controller.setRecords(records);
            controller.setPrevStage(stage);
            controller.setMainApp(this);
            UDP_Server server = new UDP_Server(5678);
            server.setListener(controller);
            controller.setServer(server);

            prevStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<SoundRecord> createRecords() {
        ArrayList<SoundRecord> rec = new ArrayList<SoundRecord>();

        SoundRecord macarena = new SoundRecord("Macarena");
        Event event0 = new Event(500,50, 100);
        Event event2 = new Event(1000, 38, 100);
        Event event4 = new Event(1500, 50, 100);
        Event event6 = new Event(2000, 38, 100);
        macarena.addEvent(event0);
        macarena.addEvent(event2);
        macarena.addEvent(event4);
        macarena.addEvent(event6);
        rec.add(macarena);

        SoundRecord EatSleepRaveRepeat = new SoundRecord("EatSleepRaveRepeat");
        Event e1 = new Event(500, 50, 100);
        Event e2 = new Event(1000, 43, 100);
        Event e3 = new Event(1500, 38, 100);
        Event e4 = new Event(2000, 41, 100);
        Event e5 = new Event(2200, 41, 100);
        EatSleepRaveRepeat.addEvent(e1);
        EatSleepRaveRepeat.addEvent(e2);
        EatSleepRaveRepeat.addEvent(e3);
        EatSleepRaveRepeat.addEvent(e4);
        EatSleepRaveRepeat.addEvent(e5);
        rec.add(EatSleepRaveRepeat);

        SoundRecord test = new SoundRecord("Facile");
        Event e10 = new Event(400, 38, 100);
        Event e20 = new Event(800, 38, 100);
        Event e30 = new Event(1000, 41, 100);
        test.addEvent(e10);
        test.addEvent(e20);
        test.addEvent(e30);
        rec.add(test);

        return rec;
    }


    public static void main(String[] args) {
        launch(args);
    }


}
