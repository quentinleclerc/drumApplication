package control;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.util.StringConverter;

import midi.SoundRecord;

import midi.Trainer;
import network.UDP_Server;

import player.CustomSynthesizer;
import player.Drummer;

import views.MainView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListeningController implements Initializable, NoteChannel {

    @FXML
    Button backToMenu;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button startTraining;
    @FXML
    private Button stopTraining;
    @FXML
    private Pane pane;
    @FXML
    private Button playSong;
    @FXML
    private ComboBox<SoundRecord> selectionMorceau;

    private Stage prevStage;

    private MainView mainApp;

    private UDP_Server server;

    private Drummer drummer;

    private Boolean training;

    private ObservableList<SoundRecord> records;

    private Trainer trainer;


    public ListeningController() {
        System.out.println("Listening initialized.");
        this.training = false;
    }

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    public void setRecords(ObservableList<SoundRecord> records) {
        this.records = records;
        initializeComboBox();

        System.out.println("records bien initialise");
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        stopButton.setDisable(true);
        stopTraining.setDisable(true);
        initializeDrummer();
    }

    private void initializeComboBox() {
        selectionMorceau.setItems(records);
        selectionMorceau.setCellFactory((comboBox) -> new ListCell<SoundRecord>() {
            @Override
            protected void updateItem(SoundRecord item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null || empty) {
                    setText(null);
                }
                else {
                    setText(item.getNom());
                }
            }
        });

        selectionMorceau.setConverter(new StringConverter<SoundRecord>() {
            @Override
            public String toString(SoundRecord record) {
                if (record == null) {
                    return null;
                }
                else {
                    return record.getNom();
                }
            }

            @Override
            public SoundRecord fromString(String string) {
                return null;
            }
        });

        selectionMorceau.setOnAction((event) -> {
            SoundRecord selectedRecord = selectionMorceau.getSelectionModel().getSelectedItem();
            System.out.println("ComboBox Action (selected: " + selectedRecord.toString() + ")");
        });
    }

    private void initializeDrummer() {
        CustomSynthesizer cs = new CustomSynthesizer();
        URL resource = getClass().getResource("/soundbanks/sdb.sf2");
        String fileName = resource.getFile();
        File file = new File(fileName);
        cs.loadSoundbank(file);
        this.drummer = new Drummer();
        System.out.println("drummer bien initialise");
    }

    @FXML
    void onClickMenu(MouseEvent event) {
        this.mainApp.showMenuView(this.prevStage);
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

    @FXML
    void onClickStartTraining(MouseEvent event) {
        stopTraining.setDisable(false);
        startTraining.setDisable(true);
        this.trainer = new Trainer(selectedRecord(), this.drummer);
        this.trainer.startTraining();
        this.server.setListener(trainer);
    }

    @FXML
    void onClickStopTraining(MouseEvent event) {
        stopTraining.setDisable(true);
        startTraining.setDisable(false);

    }

    private SoundRecord selectedRecord() {
        return selectionMorceau.getSelectionModel().getSelectedItem();
    }

    @FXML
    void onClickPlay(MouseEvent event) {
        Platform.runLater(() -> {
            selectedRecord().play(drummer);
        });
    }

    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    public void setServer(UDP_Server server) {
        this.server = server;
    }

    @Override
    public void receivedNote(int note, int velocity, long time) {
        this.drummer.noteOn(note, velocity);
    }



}
