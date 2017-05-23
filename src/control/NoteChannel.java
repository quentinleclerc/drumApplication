package control;

public interface NoteChannel {

    void receivedNote(int note, int velocity, long time);
}
