package saver;

import java.util.Date;

import control.NoteChannel;
import midi.Event;
import midi.SoundRecord;

public class SongWriter implements NoteChannel{
	private SoundRecord song;
	private Date begin;
	public SongWriter(){
		this.song=new SoundRecord("taboun");
		this.begin = new Date();
	}
	
	public void sendNote(long time, int note, int velocity) {
		}
	
	public SoundRecord getSong(){
		return this.song;
	}
	public void reset(){
		begin = new Date();
	}
	public void saveSong(){
		Serializer sr = new Serializer();
		sr.Serialize(this.song, "hh");
		System.out.println(this.song);
	}

	@Override
	public void receivedNote(int note, int velocity, long time) {
		Date now = new Date();
		long diff =  now.getTime() - begin.getTime();
		this.song.add(new Event(diff, note, velocity));
	
	}
	
}
