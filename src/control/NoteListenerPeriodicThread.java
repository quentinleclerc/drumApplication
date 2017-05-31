package control;

import control_view.ListeningController;
import javafx.application.Platform;
import midi.Event;
import midi.Scores;
import midi.SoundRecord;
import player.Drummer;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;

public class NoteListenerPeriodicThread implements Runnable, NoteChannel {


    private final ListeningController controller;

    private final ArrayList<Long> sleepTimes;
    private Double score;
    private SoundRecord played;
    private SoundRecord record;
    private Scores scoreManager;
    private Drummer drummer;

    private boolean started;
    private boolean looping;

    private long timing;
    private Date dateDeb;
    private boolean training;

    public NoteListenerPeriodicThread(SoundRecord record, Scores scoreManager, ArrayList<Long> sleepTimes, boolean looping, ListeningController controller) {
        this.played = new SoundRecord("Played_by_user");
        this.record = record;
        this.scoreManager = scoreManager;
        this.drummer = new Drummer();
        this.sleepTimes = sleepTimes;
        this.looping = looping;
        this.training = true;
        this.controller = controller;
        this.score = score;
    }

    public void setRecord(SoundRecord record) {
        this.record = record;
    }

    @Override
    public void receivedNote(int note, int velocity, long timeArg) {
        this.played.addEvent(new Event(timing, note, velocity));
        this.drummer.noteOn(note, velocity);
    }

    @Override
    public void run() {
        int i = 0;

        if (looping) {
            while(looping) {
                dateDeb = new Date();
                this.played = new SoundRecord("Played by user");
                try {
                    sleep(record.get(record.size() - 1).getTemps());
                    score = scoreManager.compare_table(record, played);
                    System.out.println("Score1 = " + score);
                    controller.updateScore(score);
                }
                catch (InterruptedException e) {
                    looping = false;
                }
            }
        }
        else {
            while(training) {
                dateDeb = new Date();
                this.played = new SoundRecord("Played by user");
                try {

                    if (i == (sleepTimes.size())) {
                        training = false;
                        sleep(record.get(record.size()-1).getTemps()-sleepTimes.get(i-1));
                        // System.out.println("sleeptimes : " + sleepTimes.get(i-1));
                        // System.out.println("dernier temps de record : " + record.get(record.size()-1).getTemps());
                        //System.out.println("played : " + played);
                        //System.out.println("record " + record.getSub(sleepTimes.get(i-1), record.get(record.size()-1).getTemps()));
                        score = scoreManager.compare_table(record.getSub(sleepTimes.get(i-1), record.get(record.size()-1).getTemps()), played);
                        //System.out.println("Score2 = " + score);
                        controller.updateScore(score);

                    }
                    else if (i == 0){
                        sleep(sleepTimes.get(i));
                        //System.out.println("played : " + played);
                        //System.out.println("record : " + record.getSub(0, sleepTimes.get(i)));
                        score = scoreManager.compare_table(record.getSub(0, sleepTimes.get(i)), played);
                        //System.out.println("Score3 = " + score);
                        controller.updateScore(score);

                    }
                    else{
                        sleep(sleepTimes.get(i)-sleepTimes.get(i-1));
                        score = scoreManager.compare_table(record.getSub(sleepTimes.get(i), sleepTimes.get(i+1)), played);
                        System.out.println("Score4 = " + score);
                        controller.updateScore(score);
                    }
                    i++;
                }
                catch (InterruptedException e) {
                    training = false;
                    looping = false;
                }
            }
        }


    }

    public void startTimer() {
        Thread threadTimer = new Thread(() -> {
            dateDeb = new Date();
            while(true) {
                try {
                    Date actualDate = new Date();
                    timing = (int) (actualDate.getTime() - dateDeb.getTime());
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadTimer.start();
    }



}
