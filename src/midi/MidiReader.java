package midi;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiReader {
	public final Sequence midiSequence;
	public final Tracks tracks;
	private final float division;
//	private final int resolution;
	public MidiReader(File fileName){
		Sequence sequence=null;
		try {
			sequence = MidiSystem.getSequence(fileName);
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}
		this.midiSequence=sequence;
		this.tracks=new Tracks(this.midiSequence.getTracks());
		this.division=this.midiSequence.getDivisionType(); //  Sequence.PPQ == Pules per quarter note == 0 ; or frame per second
		
	}
	
	
	public String printTracks(){
		return this.tracks.toString();
	}
	
	public String toString(){
		String retour="";
		retour+= "Division Type" + this.midiSequence.getDivisionType();
		retour+= "Resolution" + this.midiSequence.getResolution();
		retour+=printTracks();
		return retour;
		
	}
}