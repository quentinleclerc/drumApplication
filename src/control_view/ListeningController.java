package control_view;

import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
import control.FilesController;
import control.NoteListenerPeriodicThread;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import javafx.util.StringConverter;

import midi.*;

import network.UDP_Server;

import player.Drummer;
import player.PlayerSong;
import shapes.ThreadCircle;
import views.MainView;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
    private GridPane fondPlayFreeOld;
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
    @FXML
    private Label scoreLabel1;
    @FXML
    private Button getStats;
    @FXML
    private Label statsLabel;
    @FXML
    private Group groupBatt;
    @FXML
    private Button printStats;

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

    private Thread threadCircleThread;

    private Map<Integer, Double> kickDistance;

    private Map<Integer, Circle> liaisonToms;

    private PlayerSong player;

    private SoundRecord record;

    private ThreadCircle threadCircle;


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

        /*
        double y = this.fondPlayFree.getBoundsInParent().getMaxY()/2;
        double x = this.fondPlayFree.getBoundsInParent().getMaxX()/2;
        System.out.println(x);
        System.out.println(y);
        this.groupBatt.setLayoutX(x);
        this.groupBatt.setLayoutY(200);
        */

        initializeSelectButtons();
        initializeCheckBox();
        initializeMaps();
        // initializeDrummer();
    }

    private void initializeMaps() {
        liaisonToms.put(Drummer.HITHAT, this.cymbaleBasGauche);
        liaisonToms.put(Drummer.CRASH, this.cymbaleGauche);
        liaisonToms.put(Drummer.SNARE, this.caisseBasGauche);
        liaisonToms.put(Drummer.HIGH_TOM, this.caisseHautGauche);
        liaisonToms.put(Drummer.MIDDLE_TOM, this.caisseHautDroite);
        liaisonToms.put(Drummer.FLOOR_TOM, this.caisseBasDroite);
        liaisonToms.put(Drummer.RIDE, this.cymbaleDroite);

        /*
        for (Circle c : liaisonToms.values()) {
            System.out.println("X = " + c.getLayoutX() + " Y = " + c.getLayoutY());

        }

        Circle circle = new Circle(400d, 400d, this.cymbaleBasGauche.getRadius(), Color.BLUE);
        this.fondPlayFree.getChildren().add(circle);
        */

        kickDistance.put(Drummer.SNARE, caisseBasGauche.getLayoutY());
        kickDistance.put(Drummer.MIDDLE_TOM, caisseHautDroite.getLayoutY() );
        kickDistance.put(Drummer.HITHAT, cymbaleBasGauche.getLayoutY());
        kickDistance.put(Drummer.HIGH_TOM, caisseHautGauche.getLayoutY());
        kickDistance.put(Drummer.FLOOR_TOM, caisseBasDroite.getLayoutY());
        kickDistance.put(Drummer.CRASH, cymbaleGauche.getLayoutY());
        kickDistance.put(Drummer.RIDE, cymbaleDroite.getLayoutY());
        kickDistance.put(Drummer.KICK, pedale.getLayoutY());
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
            this.player = new PlayerSong(getSelectedItem());
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
        //this.scoreLabel.setVisible(false);

        record = selectedRecord();

        this.player = new PlayerSong(record);
        ArrayList<Long> sleepTimes = this.scoreManager.initializeSong(record, looping);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.noteListener = new NoteListenerPeriodicThread(record, this.scoreManager, sleepTimes, looping, this);


        this.server.setListener(noteListener);
        this.noteListenerThread = new Thread(noteListener);
        this.noteListenerThread.start();


        threadCircle = new ThreadCircle(record, this, this.kickDistance, this.liaisonToms, this.pedale);
        this.threadCircleThread = new Thread (threadCircle);

        this.onClickPlay(null);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.threadCircleThread.start();


        this.noteListener.startTimer();
        // START LUIS
        // LOOPING boolean

    }

    @FXML
    void onClickStopTraining(MouseEvent event) {
        stopTraining.setDisable(true);
        startTraining.setDisable(false);
        this.training = false;

        String score = this.scoreManager.end_record();
        // this.updateScore(score);
        // System.out.println(this.scoreManager.getStats());

        this.threadCircleThread.interrupt();
        System.out.println(this.player);
        this.player.stopSong();
        // this.scoreLabel.setText(score);
        // this.scoreLabel.setVisible(true);
        System.out.println(score);
        this.noteListenerThread.interrupt();
        this.threadCircle.deleteTemporaryShapes();
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
            MidiFileToSong translator = new MidiFileToSong(fileImported, 4F, 2000 );
            record = translator.getSong();
        }
        else {
            record = null;
        }
//        System.out.println("Coucou" + record);
        return record;
    }

    @FXML
    void onClickPlay(MouseEvent event) {
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

    @FXML
    void onKeyPressed(KeyEvent event) {
        int velocity = 100;
        switch (event.getCode()) {
            case SPACE :
                noteListener.receivedNote(Drummer.KICK, velocity, 0);
                break;
            case G :
                noteListener.receivedNote(Drummer.HIGH_TOM, velocity, 0);
                break;
            case H :
                noteListener.receivedNote(Drummer.MIDDLE_TOM, velocity, 0);
                break;
            case N :
                noteListener.receivedNote(Drummer.FLOOR_TOM, velocity, 0);
                break;
            case J :
                noteListener.receivedNote(Drummer.RIDE,velocity, 0);
                break;
            case F :
                noteListener.receivedNote(Drummer.CRASH,velocity, 0);
                break;
            case V :
                noteListener.receivedNote(Drummer.SNARE, velocity, 0);
                break;
            case C :
                noteListener.receivedNote(Drummer.HITHAT, velocity, 0);
                break;
            /*
            case R :
                noteListener.saveSong();
                SoundRecord song = noteListener.getSong();
                Merger merge =new Merger();
                song = merge.merge(song, song);
                noteListener.reset();
                PlayerSong playerSong = new PlayerSong(song);
                new Thread(playerSong).start();
                break;
            */
            default:
                // System.out.println(event.getCode());
                break;
        }
    }

    @FXML
    void onGetStats(MouseEvent event) {
        String stats = this.scoreManager.getStats();
        System.out.println("On get stats : " + stats);
        this.statsLabel.setText(stats);
    }

    public void removeShape(Shape e) {
        this.fondPlayFree.getChildren().remove(e);
    }

    public void updateScore(Double score) {
        Platform.runLater(() -> {
            this.scoreLabel1.setText(Double.toString(score));
            this.scoreLabel.setText(Double.toString(score));
        });
    }
}
