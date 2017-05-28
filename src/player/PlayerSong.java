package player;

import java.io.File;
import java.net.URL;

import midi.Event;
import midi.MidiFileToSong;
import midi.SoundRecord;

public class PlayerSong implements Runnable{

	private SoundRecord song ;
	private Drummer drum;

	public PlayerSong(SoundRecord song) {
		this.drum = new Drummer();
		this.song = song;
	}

	public void playSong(){
		new Thread(this).start();
	}

	public void run() {
		int i=0;
		for (i = 0; i < song.size()-1; i++) {
			Event event = this.song.get(i);
			double timestamp = event.getTemps();
			int note = event.getNote();
			int velocity = event.getVelocity();
			drum.noteOn(note, velocity);
			try {
				Thread.sleep((long)(this.song.get(i+1).getTemps() - timestamp));
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//last event
		Event event = this.song.get(i);
		int note = event.getNote();
		int velocity = event.getVelocity();
		drum.noteOn(note, velocity);
	}

	public static void main(String[] args) {

		MidiFileToSong translator = new MidiFileToSong("test.mid",3F,0);
		SoundRecord song = translator.getSong();
		System.out.println(song);
		PlayerSong playerSong = new PlayerSong(song);
		playerSong.playSong();
	}

}
