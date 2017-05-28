package midi;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class SoundRecord extends  ArrayList<Event> implements Serializable {

	private String name;
		
	public SoundRecord(String name) {
		super();
		this.name = name;
	}
	
	public SoundRecord(String fileImported, ArrayList<Event> events) {
		// TODO Auto-generated constructor stub
	}

	public SoundRecord() {
		// TODO Auto-generated constructor stub
	}

	public long getMinInter(){
		ArrayList<Long> inter = getIntervales();
		long min = Long.MAX_VALUE;
		for (int i = 0; i < inter.size(); i++) {
			if (inter.get(i)<min) min = inter.get(i);
		}
		return min;
		
	}
	
	public  ArrayList<Long> getIntervales(){
		ArrayList<Long> list = new ArrayList<>();
		
		for (int i = 0; i<this.size();i++) {
			if (i==0){
				list.add(i, this.get(i).getTemps());
				
			}
			else{
				list.add(i, this.get(i).getTemps()-this.get(i-1).getTemps());
			}
		}
		return list;
	}
	
	public void clean(){
		this.clear();
		
	}
	
	public void addEvent(Event event){
		this.add(event);
	}
	
	public String getNom() {
		return name;
	}

	public void changeTempo(double d) {
		ArrayList<Long> inter = getIntervales();
		for (int i = 0; i < this.size(); i++) {
			if (i==0){
				int temps = (int) (inter.get(i)*d);
				this.get(i).setTemps(temps);
			}
			else{
				int temps = (int) (inter.get(i)*d);
				this.get(i).setTemps(this.get(i-1).getTemps() + temps);
			}
			System.out.println("nouveaux temps: "+ this);
		}

	}
  
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		System.out.println("Translator de track ");
//		MidiFileToSong translator = new MidiFileToSong(new File("test.mid"),1.0F,0);
//		SoundRecord song = translator.getSong();
//		System.out.println(song);
//		CerclesRepresentation cr = new CerclesRepresentation(song, mapDistance)
//		translator.setDelay(100.0);
//	}

}
