package control;

import midi.Event;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class NoteListenerPeriodicThread implements Runnable, NoteChannel {

    private ArrayList<Event> played;

    private ArrayList<Event> record;

    private boolean training;

    public NoteListenerPeriodicThread(ArrayList<Event> record) {
        this.played = new ArrayList<>();
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
        this.played.add(new Event(time, note, velocity));
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
