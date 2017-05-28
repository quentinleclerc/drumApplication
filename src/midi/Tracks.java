package midi;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.sound.midi.Track;

public class Tracks {
	public final int NOTE_ON = 0x90;
	public final int NOTE_OFF = 0x80;
	public final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	private final Track tracks[];
	private final int length;

	public Tracks(Track[] tracks) {
		this.tracks=tracks;
		this.length=this.tracks.length;
	}

	public String toString() {
		int i = 0;
		String retour="Nombre de tracks : "+Integer.toString(this.length) +"\n";
		for (Track track : this.tracks) {
			retour+="Track["+ (++i) +"], size[";
			retour+= trackToString(track) + "\n";
		}
		return retour;
	}
	
	public Track getTrack(int index){
		return this.tracks[index];
	}

	public String trackToString(Track track){
		String retour= Integer.toString(track.size()) +"]\n";
		for (int i = 0; i < track.size(); i++) {
			retour+= "Tick["+track.get(i).getTick()+"] :";
			retour +=" Status[" + track.get(i).getMessage().getStatus()+ "]";
			retour+=", data["+ track.get(i).getMessage().getLength()+"bytes], [";
			for (int j = 0; j < track.get(i).getMessage().getLength(); j++) {
				int value = track.get(i).getMessage().getMessage()[j] & 0xFF;
				retour+=value+",";
			}
			retour+="]\n";
//			int signal = track.get(i).getMessage().getMessage()[0] & 0xFF;
//			int note = track.get(i).getMessage().getMessage()[1] & 0xFF;
//			int velocity = track.get(i).getMessage().getMessage()[2] & 0xFF;
//			retour+= "["+ signal +"," + note +"," + velocity +"] \n";
		}
		return retour;
	}

	public String byteToString(byte[] bytes){
		String retour="";
		for (int i = 0; i < bytes.length; i++) {

		}
		return retour;
	}
}
