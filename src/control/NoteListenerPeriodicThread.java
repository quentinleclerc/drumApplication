package control;

import midi.Event;
import midi.SoundRecord;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class NoteListenerPeriodicThread implements Runnable, NoteChannel {

    private SoundRecord played;

    private SoundRecord record;

    private boolean training;

    public NoteListenerPeriodicThread(SoundRecord record) {
        this.played = new SoundRecord("Played_by_user");
        this.record = record;
    }

    public void setRecord() {
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
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
