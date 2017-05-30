package midi;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import player.PlayerSong;

public class Merger {
	public Merger() {
		// TODO Auto-generated constructor stub
	}
	
	public SoundRecord merge(SoundRecord s1,SoundRecord s2){
		SoundRecord song= new SoundRecord(null);
		song.addAll(s1);
		song.addAll(s2);
		Collections.sort(song);
		return song;
	}
	
//	public static void main(String[] args) {
//		Serializer sr = new Serializer();
//		SoundRecord song1 = sr.deSerialize("kick");
//		SoundRecord song2  = sr.deSerialize("hh");
//		System.out.println(song1);
//		System.out.println(song2);
//		Merger merger = new Merger();
//		SoundRecord song = merger.merge(song1, song2);
//		System.out.println(song);
//		PlayerSong player = new PlayerSong(song);
//		player.playSong();
//	}
}
