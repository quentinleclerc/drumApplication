package control;

import player.*;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.File;
import java.net.MalformedURLException;

import java.text.DecimalFormat;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import views.MainView;

import javax.sound.midi.*;

public class PlayMidiController implements Initializable {

    private Stage prevStage;
    MediaPlayer mediaPlayer;
    MediaView mediaView;
    File musicFile = null;
    Duration totalDuration;
    File imageFilePausePlay;
    String localUrl;
    Double totalTimeOfMusic ;
    boolean repeat = false;
    Duration duration;
    ToggleButton tb = new ToggleButton();

    @FXML
    Label songName;

    private String filePath;

    private MidiPlayer midiPlayer = null;
    private boolean loop;
    private MainView mainApp;
    private Drummer drummer;

    public PlayMidiController() {
        System.out.println("PlayMidiController initialized.");
    }

    public void setPrevStage(Stage stage){
        this.prevStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {

    }

    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void playpause() throws MalformedURLException {
        /*

        if (playpauseButton.isSelected()) {
            if (musicFile != null) {
                totalTimeOfMusic=mediaPlayer.getTotalDuration().toSeconds();
                System.out.println(totalTimeOfMusic);
                durationLabel.setText(String.valueOf(new DecimalFormat("#.00")
                        .format(mediaPlayer.getTotalDuration().toMinutes())));// duration label


                // for set value for initial
                volumeSlider.setValue(40);
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);

                imageFilePausePlay = new File("images/pause.jpg");
                localUrl = imageFilePausePlay.toURI().toURL().toString();
                Image image2 = new Image(localUrl);
                playPuseImage.setImage(image2);
                mediaPlayer.play();


                volumeSlider.valueProperty().addListener((Observable) -> {

                    mediaPlayer.setVolume(volumeSlider.getValue() / 100);

                });

                // seek slider


                mediaPlayer.currentTimeProperty().addListener((Observable)->{
                    if(seekSlider.isValueChanging()){
                        mediaPlayer.seek(Duration.seconds((seekSlider.getValue()*(totalTimeOfMusic)/100)));
                    }
                    if(seekSlider.isPressed()){
                        mediaPlayer.seek(Duration.seconds((seekSlider.getValue()*(totalTimeOfMusic)/100)));
                    }
                    //updateValues();
                    seekSlider.setValue((mediaPlayer.getCurrentTime().toSeconds()*100)/totalTimeOfMusic);
                    System.out.println("ok"+mediaPlayer.getCurrentTime().toSeconds());
                    playTime.setText(String.valueOf(new DecimalFormat("#0.00").format(mediaPlayer.getCurrentTime().toMinutes()))+"  /");
                });

                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);// for run
                // infinite
                // time

                // mediaPlayer.setOnPlaying(new Runnable() {
                // public void run() {
                // System.out.println("playing");
                // updateValues();
                // }
                // });

                System.out.println(mediaPlayer.getStartTime());

                System.out.println();
            } else {

            }
        } else {
            if (musicFile != null) {
                imageFilePausePlay = new File("images/Button-Play-icon.png");
                localUrl = imageFilePausePlay.toURI().toURL().toString();
                Image image2 = new Image(localUrl);
                playPuseImage.setImage(image2);
                durationLabel.setText(String.valueOf(new DecimalFormat("#.00")
                        .format(mediaPlayer.getTotalDuration().toMinutes())));// duration
                // label
                mediaPlayer.pause();
            }
        }*/

    }

    @FXML
    void onPlay(MouseEvent event) {
        if (midiPlayer != null) {
            this.midiPlayer.setPaused(false);
        }
    }

    @FXML
    void onPause(MouseEvent event) {
        if (midiPlayer != null) {
            this.midiPlayer.setPaused(true);
        }
    }

    @FXML
    void onStop(MouseEvent event) {
        this.midiPlayer.stop();
    }

    @FXML
    void onImportFile(MouseEvent event) {

        try {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".midi","*.mid"));

            musicFile = fc.showOpenDialog(null);
            if (musicFile != null) {
                this.filePath = musicFile.getAbsolutePath();
                this.filePath = this.filePath.replace("\\", "/");
                songName.setText(filePath);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        play();
    }

    @FXML
    void onLoop(MouseEvent event) {
        this.loop = !this.loop;
    }

    @FXML
    void onClickMenu(MouseEvent event) {
        this.mainApp.showMenuView(this.prevStage);
    }

    private void play() {

        this.midiPlayer = new MidiPlayer();

        // load a sequence
        Sequence sequence = midiPlayer.getSequence(filePath);

        // play the sequence
        midiPlayer.play(sequence, loop);
        }

}

