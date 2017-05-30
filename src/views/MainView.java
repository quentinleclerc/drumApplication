package views;

import control_view.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import midi.Event;
import midi.Scores;
import midi.SoundRecord;
import network.UDP_Server;
import player.Drummer;

import java.io.IOException;
import java.util.ArrayList;

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
            controller.setScene(scene);
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
            controller.setScoreManager(new Scores());
            UDP_Server server = new UDP_Server(5678);
            controller.setServer(server);

            prevStage.close();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<SoundRecord> createRecords() {
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
        Event e1 = new Event(200, Drummer.KICK, 100);
        Event e2 = new Event(400, Drummer.FLOOR_TOM, 100);
        Event e3 = new Event(600, Drummer.SNARE, 100);
        Event e4 = new Event(800, Drummer.HIGH_TOM, 100);
        Event e5 = new Event(1000, Drummer.KICK, 100);
        Event e6 = new Event(1300, Drummer.KICK, 100);
        Event e7 = new Event(1600, Drummer.KICK, 100);
        Event e8 = new Event(1900, Drummer.KICK, 100);

        EatSleepRaveRepeat.addEvent(e1);
        EatSleepRaveRepeat.addEvent(e2);
        EatSleepRaveRepeat.addEvent(e3);
        EatSleepRaveRepeat.addEvent(e4);
        EatSleepRaveRepeat.addEvent(e5);
        EatSleepRaveRepeat.addEvent(e6);
        EatSleepRaveRepeat.addEvent(e7);
        EatSleepRaveRepeat.addEvent(e8);

        rec.add(EatSleepRaveRepeat);

        SoundRecord test = new SoundRecord("Test");
        Event e10 = new Event(400, 38, 100);
        Event e20 = new Event(800, 38, 100);
        Event e30 = new Event(1000, 41, 100);
        test.addEvent(e10);
        test.addEvent(e20);
        test.addEvent(e30);
        rec.add(test);


        SoundRecord metronome = new SoundRecord("Metronome");
        Event m1 = new Event(1000, 38, 100);
        Event m2 = new Event(2000, 38, 100);
        Event m3 = new Event(3000, 38, 100);
        Event m4 = new Event(4000, 38, 100);
        Event m5 = new Event(5000, 38, 100);
        Event m6 = new Event(6000, 38, 100);
        Event m7 = new Event(7000, 38, 100);
        Event m8 = new Event(8000, 38, 100);
        metronome.add(m1);
        metronome.add(m2);
        metronome.add(m3);
        metronome.add(m4);
        metronome.add(m5);
        metronome.add(m6);
        metronome.add(m7);
        metronome.add(m8);

        rec.add(metronome);


//        System.out.println(metronome.getSub(0, 890));
  //      System.out.println(metronome.getSub(0, 1290));
    //    System.out.println(metronome.getSub(1800, 4500));


        return rec;
    }


    public static void main(String[] args) {
        launch(args);
    }


}
