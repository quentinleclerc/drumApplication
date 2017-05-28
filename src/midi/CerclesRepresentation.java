package midi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;

import player.Drummer;

public class CerclesRepresentation extends ArrayList<Event>{
	private final SoundRecord cerclesSong;
	private final TempsCercles mapTemps; 

	public CerclesRepresentation(SoundRecord song, HashMap<Integer,Integer> mapDistance) {
		super();
		this.mapTemps=new TempsCercles(mapDistance, 1);
		this.cerclesSong=song;
		buildCercleListe();
	}

	private void buildCercleListe() {
		for (Iterator Event = cerclesSong.iterator(); Event.hasNext();) {
			Event event = (Event) Event.next();
			int note = event.getNote();
			long timeStamp = event.getTemps();
			Long temps = this.mapTemps.get(note);
			if (temps!=null){
				timeStamp= timeStamp - temps;
				int velocity = event.getVelocity();
				this.add(new Event(timeStamp, note, velocity));
			}
			else {
				System.out.println("Note non reconnue : " + note);
			}
		}
	}
	
	public SoundRecord getCerclesSong() {
		return cerclesSong;
	}


	
	public static void main(String[] args) {
		HashMap<Integer,Integer> mapDistance = new HashMap<Integer,Integer>();
		mapDistance.put(Drummer.KICK, 130);
		mapDistance.put(Drummer.CRASH, 80);
		mapDistance.put(Drummer.FLOOR_TOM, 100);
		mapDistance.put(Drummer.SNARE, 100);
		mapDistance.put(Drummer.HIGH_TOM, 100);
		mapDistance.put(Drummer.MIDDLE_TOM, 300);
		mapDistance.put(Drummer.RIDE, 100);
		mapDistance.put(Drummer.SNARE, 100);

		MidiFileToSong translator = new MidiFileToSong("test.mid",1.2F,0);
		SoundRecord song = translator.getSong();
		System.out.println(song);
		
		CerclesRepresentation cercles = new CerclesRepresentation(song, mapDistance);
		System.out.println(cercles);
	}

}
