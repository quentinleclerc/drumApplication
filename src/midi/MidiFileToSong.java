package midi;

import java.io.File;
import java.net.URL;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;

public class MidiFileToSong {
	public final float rapidite;
	public long delay; 
	public SoundRecord song;
	public final Track drumTrack;
	public MidiFileToSong(String filepath, float rapidite, long delay) {
		super();
		//TODO : faire ave l'url
//		URL resource = getClass().getResource(filepath);
//		String fileName = resource.getFile();
		File file = new File(filepath);
		MidiReader midiReader= new MidiReader(file);
		this.rapidite=rapidite;
		Track interm=null;
		try {
			interm=midiReader.tracks.getTrack(1);
		} catch (Exception e) {
			System.out.println("Le track 1 n'est pas dispo, le file doit être pété");
		}
		this.drumTrack=interm;
		this.delay=delay;
		buildSong();
	}

	public void buildSong(){
		this.song= new SoundRecord();
		for (int i = 0; i < this.drumTrack.size(); i++) {
			MidiEvent me = this.drumTrack.get(i);
			//If note on? 
			if (me.getMessage().getStatus()!=137){
				long timeStamp = (long)(delay + me.getTick()*rapidite);
				int note = me.getMessage().getMessage()[1]& 0xFF;
				int velocity = me.getMessage().getMessage()[2]& 0xFF;
				Event e = new Event(timeStamp,note,velocity);
				song.add(e);
				//				System.out.println(timeStamp +" "+ note + " "+ velocity);
			}
		}
	}

	public double getRapidite() {
		return rapidite;
	}


	public double getDelay() {
		return delay;
	}


	public SoundRecord getSong() {
		return song;
	}

	public void setDelay(long delay) {
		this.delay=delay;
		buildSong();
	}
}
