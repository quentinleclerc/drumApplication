package midi;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;

import player.Drummer;
import player.PlayerSong;

public class CerclesRepresentation extends ArrayList<Event>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SoundRecord cerclesSong;
	private final TempsCercles mapTemps; 

	public CerclesRepresentation(SoundRecord song, Map<Integer, Double> kickDistance) {
		super();
		this.mapTemps = new TempsCercles(kickDistance, 0.5F);
		this.cerclesSong=song;
		buildCercleListe();
		Collections.sort(this);
	}

	private void buildCercleListe() {
		for (Iterator Event = cerclesSong.iterator(); Event.hasNext();) {
			Event event = (Event) Event.next();
			int note = event.getNote();
			long timeStamp = event.getTemps();
			Long temps = this.mapTemps.get(note);
			if (temps!=null){
				timeStamp= timeStamp - 1000;
				int velocity = event.getVelocity();
				this.add(new Event(timeStamp, note, velocity));
			}
			else {
//				System.out.println("Note non reconnue : " + note);
			}
		}
	}


	public SoundRecord getCerclesSong() {
		return cerclesSong;
	}


//	public static void main(String[] args) {
//		HashMap<Integer,Double> mapDistance = new HashMap<Integer,Double>();
//		mapDistance.put(Drummer.KICK, 130.0);
//		mapDistance.put(Drummer.CRASH, 80.0);
//		mapDistance.put(Drummer.FLOOR_TOM, 100.0);
//		mapDistance.put(Drummer.SNARE, 100.0);
//		mapDistance.put(Drummer.HIGH_TOM, 100.0);
//		mapDistance.put(Drummer.MIDDLE_TOM, 300.0);
//		mapDistance.put(Drummer.RIDE, 100.0);
//		mapDistance.put(Drummer.SNARE, 100.0);
//
//		MidiFileToSong translator = new MidiFileToSong("midi.mid",1.2F,0);
//		SoundRecord song = translator.getSong();
//		PlayerSong p = new PlayerSong(song);
//		p.playSong();
//		System.out.println(song);
//
//		CerclesRepresentation cercles = new CerclesRepresentation(song, mapDistance);
//		System.out.println(cercles);
//	}

}
