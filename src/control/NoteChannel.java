package control;

import midi.SoundRecord;

public interface NoteChannel {

    void receivedNote(int note, int velocity, long time);

}
