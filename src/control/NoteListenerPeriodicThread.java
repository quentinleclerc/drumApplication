package control;

import midi.Event;
import midi.Scores;
import midi.SoundRecord;

import static java.lang.Thread.sleep;

public class NoteListenerPeriodicThread implements Runnable, NoteChannel {

    private SoundRecord played;
    private SoundRecord record;
    private boolean training;
    private Scores scoreManager;

    public NoteListenerPeriodicThread(SoundRecord record, Scores scoreManager) {
        this.played = new SoundRecord("Played_by_user");
        this.record = record;
        this.scoreManager = scoreManager;
    }

    public void setRecord(SoundRecord record) {
        this.record = record;
    }

    public void setTraining(boolean training) {
        this.training = training;
    }

    @Override
    public void receivedNote(int note, int velocity, long time) {
        this.played.addEvent(new Event(time, note, velocity));
    }

    @Override
    public void run() {
        while(training) {
            this.played = new SoundRecord("Played by user");
            try {
                sleep(5000);
                scoreManager.compare_table(record, played);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
