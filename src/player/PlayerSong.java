package player;

import midi.Event;
import midi.SoundRecord;

public class PlayerSong implements Runnable{

	private SoundRecord song ;
	private Drummer drum;
	private Thread threadPlaying;
	private boolean looping;

	public PlayerSong(SoundRecord song) {
		this.drum = new Drummer();
		this.song = song;
	}

	public void playSong(boolean looping) {
		this.looping = looping;
		this.threadPlaying = new Thread(this);

		this.threadPlaying.start();
	}

	public void stopSong(){
		this.threadPlaying.interrupt();
	}

	public void run() {
		this.play();

		while(looping) {
			this.play();
		}
	}

	public void play() {
		int i;

		try {
			Thread.sleep(this.song.get(0).getTemps());
		} catch (Exception e) {
			// TODO: handle exception
		}

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
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}

		//last event
		Event event = this.song.get(i);
		int note = event.getNote();
		int velocity = event.getVelocity();
		drum.noteOn(note, velocity);
	}
}
