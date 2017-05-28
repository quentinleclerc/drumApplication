package control;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.util.StringConverter;

import midi.Event;
import midi.MidiFileToSong;
import midi.Scores;
import midi.SoundRecord;

import network.UDP_Server;

import player.CustomSynthesizer;
import player.Drummer;

import player.MidiPlayer;
import player.PlayerSong;
import views.MainView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ListeningController implements Initializable {

    @FXML
    private Button backToMenu;
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
    @FXML
    private RadioButton selectRecord;
    @FXML
    private RadioButton selectComputer;
    @FXML
    private ImageView folderIcon;
    @FXML
    private CheckBox loopCheckBox;
    @FXML
    private Label scoreLabel;

    private Stage prevStage;
    private MainView mainApp;

    private UDP_Server server;

    private Boolean training;

    private ObservableList<SoundRecord> records;

    private NoteListenerPeriodicThread noteListener;

    private Thread noteListenerThread;

    private String fileImported;

    private boolean looping;

    private Scores scoreManager;


    public ListeningController() {
        System.out.println("Listening initialized.");
        this.training = false;
        this.looping = false;
    }

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    public void setRecords(ObservableList<SoundRecord> records) {
        this.records = records;
        initializeComboBox();

        System.out.println("records bien initialisés");
    }

    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    public void setServer(UDP_Server server) {
        this.server = server;
    }

    public void setScoreManager(Scores scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        stopButton.setDisable(true);
        stopTraining.setDisable(true);

        folderIcon.setVisible(false);
        selectionMorceau.setDisable(true);

        initializeSelectButtons();
        initializeCheckBox();
        // initializeDrummer();
    }

    private void initializeCheckBox() {
        loopCheckBox.setSelected(false);

        loopCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (loopCheckBox.isSelected()) {
                this.looping = true;
                System.out.println(looping);
            } else {
                this.looping = false;
                System.out.println(looping);
            }
        });
    }

    private void initializeSelectButtons() {
        ToggleGroup group = new ToggleGroup();

        selectComputer.setToggleGroup(group);
        selectRecord.setToggleGroup(group);
        selectComputer.setSelected(false);
        selectRecord.setSelected(false);

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            Toggle selected = group.getSelectedToggle();
            if (selected.equals(selectComputer)) {
                this.selectionMorceau.setDisable(true);
                this.folderIcon.setVisible(true);
                this.selectionMorceau.getSelectionModel().clearSelection();
                this.selectionMorceau.setValue(null);
            }
            else if (selected.equals(selectRecord)) {
                this.selectionMorceau.setDisable(false);
                this.folderIcon.setCache(true);
                this.folderIcon.setVisible(false);
                this.fileImported = "";
            }
            else {
                selectionMorceau.setDisable(true);
            }
        });
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
            // SoundRecord selectedRecord = getSelectedItem();
            // System.out.println("ComboBox Action (selected: " + selectedRecord.toString() + ")");
        });
    }

    /*
    private void initializeDrummer() {
        CustomSynthesizer cs = new CustomSynthesizer();
        URL resource = getClass().getResource("/soundbanks/sdb.sf2");
        String fileName = resource.getFile();
        File file = new File(fileName);
        cs.loadSoundbank(file);
        this.drummer = new Drummer();
        System.out.println("drummer bien initialise");
    }
    */

    @FXML
    void onClickMenu(MouseEvent event) {
        this.mainApp.showMenuView(this.prevStage);
    }

    @FXML
    void onClickStart(MouseEvent event) {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        this.server.setListener(noteListener);
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

        this.training = true;
        this.scoreLabel.setVisible(false);

        this.noteListener = new NoteListenerPeriodicThread(selectedRecord(), this.scoreManager);
        this.server.setListener(noteListener);

        this.noteListenerThread = new Thread(noteListener);
        this.noteListenerThread.start();

        this.scoreManager.initializeSong(selectedRecord());
        // START LUIS
        // LOOPING boolean

    }

    @FXML
    void onClickStopTraining(MouseEvent event) {
        stopTraining.setDisable(true);
        startTraining.setDisable(false);

        this.training = false;


        String score = this.scoreManager.end_record();
        this.scoreLabel.setText(score);
        this.scoreLabel.setVisible(true);
        System.out.println(score);
        this.noteListenerThread.interrupt();
    }

    @FXML
    void onFolderClicked(MouseEvent event) {
        FilesController fileController = new FilesController();
        this.fileImported = fileController.importFile();

    }

    private SoundRecord getSelectedItem() {
        return selectionMorceau.getSelectionModel().getSelectedItem();
    }

    private SoundRecord selectedRecord() {
        SoundRecord record;
        if (getSelectedItem() != null) {
            record = getSelectedItem();
        }
        else if (!fileImported.equals("")) {
            ArrayList<Event> events = null;
            MidiFileToSong translator = new MidiFileToSong(fileImported, 1.5F, 0 );
            record = translator.getSong();
        }
        else {
            record = null;
        }
        return record;
    }

    @FXML
    void onClickPlay(MouseEvent event) {
        Platform.runLater(() -> {
            SoundRecord selected = selectedRecord();
            PlayerSong player = new PlayerSong(selected);
            player.playSong();
        });
    }


}
