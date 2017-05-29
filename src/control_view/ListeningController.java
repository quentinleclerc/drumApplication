package control_view;

import control.FilesController;
import control.NoteListenerPeriodicThread;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import javafx.util.StringConverter;

import midi.Event;
import midi.MidiFileToSong;
import midi.Scores;
import midi.SoundRecord;

import network.UDP_Server;

import player.Drummer;
import player.PlayerSong;
import shapes.ThreadCircle;
import views.MainView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ListeningController implements Initializable {

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
    private Pane fondPlayFree;
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

    private Thread threadCircle;

    private Map<Integer, Double> kickDistance;

    private Map<Integer, Circle> liaisonToms;

    private PlayerSong player;

    public ListeningController() {
        System.out.println("Listening initialized.");
        this.training = false;
        this.looping = false;
        this.kickDistance = new HashMap<>();
        this.liaisonToms = new HashMap<>();
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
        initializeMaps();
        // initializeDrummer();
    }

    private void initializeMaps() {
        liaisonToms.put(Drummer.SNARE, this.caisseBasGauche);
        liaisonToms.put(Drummer.MIDDLE_TOM, this.caisseHautDroite);
        liaisonToms.put(Drummer.HITHAT, this.cymbaleBasGauche);
        liaisonToms.put(Drummer.HIGH_TOM, this.caisseHautGauche);
        liaisonToms.put(Drummer.FLOOR_TOM, this.caisseBasDroite);
        liaisonToms.put(Drummer.CRASH, this.cymbaleGauche);
        liaisonToms.put(Drummer.RIDE, this.cymbaleDroite);

        kickDistance.put(Drummer.SNARE, caisseBasGauche.getLayoutY() - caisseBasGauche.getRadius());
        kickDistance.put(Drummer.MIDDLE_TOM, caisseHautDroite.getLayoutY() - caisseHautDroite.getRadius());
        kickDistance.put(Drummer.HITHAT, cymbaleBasGauche.getLayoutY() - cymbaleBasGauche.getRadius());
        kickDistance.put(Drummer.HIGH_TOM, caisseHautGauche.getLayoutY() - caisseHautGauche.getRadius());
        kickDistance.put(Drummer.FLOOR_TOM, caisseBasDroite.getLayoutY() - caisseBasDroite.getRadius());
        kickDistance.put(Drummer.CRASH, cymbaleGauche.getLayoutY() - cymbaleGauche.getRadius());
        kickDistance.put(Drummer.RIDE, cymbaleDroite.getLayoutY() - cymbaleDroite.getRadius());
        kickDistance.put(Drummer.KICK, pedale.getLayoutY() - pedale.getRadiusY());
    }

    private void initializeCheckBox() {
        loopCheckBox.setSelected(false);

        loopCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (loopCheckBox.isSelected()) {
                this.looping = true;
            } else {
                this.looping = false;
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

        this.threadCircle = new Thread (new ThreadCircle(selectedRecord(),this, this.kickDistance, this.liaisonToms, this.pedale));
        this.threadCircle.start();

        this.onClickPlay(null);


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

        this.threadCircle.interrupt();
        System.out.println(this.player);
        this.player.stopSong();
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
            MidiFileToSong translator = new MidiFileToSong(fileImported, 8F, 10000 );
            record = translator.getSong();
        }
        else {
            record = null;
        }
        System.out.println("Coucou" + record);
        return record;
    }

    @FXML
    void onClickPlay(MouseEvent event) {
        SoundRecord selected = selectedRecord();
        this.player = new PlayerSong(selected);
        this.player.playSong(looping);
    }

    public void addShape(Shape e){
        if(e == null){
            System.out.println("Shape is null");
        }
        else {
            this.fondPlayFree.getChildren().add(e);
        }
    }

    public void removeShape(Shape e) {
        this.fondPlayFree.getChildren().remove(e);
    }

}
