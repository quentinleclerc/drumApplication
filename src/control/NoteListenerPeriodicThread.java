package control;

import midi.Event;
import midi.Scores;
import midi.SoundRecord;
import player.Drummer;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Thread.sleep;

public class NoteListenerPeriodicThread implements Runnable, NoteChannel {

    private final ArrayList<Long> sleepTimes;
    private SoundRecord played;
    private SoundRecord record;
    private Scores scoreManager;
    private Drummer drummer;

    private boolean started;
    private boolean looping;

    private long timing;
    private Date dateDeb;
    private boolean training;

    public NoteListenerPeriodicThread(SoundRecord record, Scores scoreManager, ArrayList<Long> sleepTimes, boolean looping) {
        this.played = new SoundRecord("Played_by_user");
        this.record = record;
        this.scoreManager = scoreManager;
        this.drummer = new Drummer();
        this.sleepTimes = sleepTimes;
        this.looping = looping;
        this.training = true;
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
                    scoreManager.compare_table(record, played);
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
                        scoreManager.compare_table(record.getSub(sleepTimes.get(i), record.get(record.size()).getTemps()), played);
                    }
                    else if (i == 0){
                        sleep(sleepTimes.get(i));
                        scoreManager.compare_table(record.getSub(0, sleepTimes.get(i)), played);

                    }
                    else{
                        sleep(sleepTimes.get(i)-sleepTimes.get(i-1));
                        scoreManager.compare_table(record.getSub(sleepTimes.get(i), sleepTimes.get(i+1)), played);

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

/*

Thread th = new Thread(new Runnable() {
-            @Override
-            public void run() {
-                while (!success){
-                    Date actualDate = new Date();
-                    timing = (int) (actualDate.getTime() - dateDeb.getTime());
-                    //timer.setText(Long.toString(timing/1000)+":"+Long.toString(timing%1000));
-                    try {
-                        Thread.sleep(100);
-                    } catch (InterruptedException e) {
-                        e.printStackTrace();
-                    }
-                }
-            }
-        });


 */


}
