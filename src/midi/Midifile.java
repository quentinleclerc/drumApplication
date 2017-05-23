package midi;

import java.util.ArrayList;


public class Midifile {

	static SoundRecord file2comp = new SoundRecord("coucou");
	public SoundRecord getFile2comp() {
		return file2comp;
	}

	
	public Midifile() {
		Event event0 = new Event(1000, 97, 100);
		Event event1 = new Event(2000,122, 100);
		Event event2 = new Event(3000, 97, 100);
		Event event3 = new Event(4000, 122, 100);
		Event event4 = new Event(5000, 97, 100);
		Event event5 = new Event(6000, 122, 100);
		Event event6 = new Event(7000, 97, 100);
		Event event7 = new Event(8000, 122, 100);
        file2comp.addEvent(event0);
        file2comp.addEvent(event1);
        file2comp.addEvent(event2);
        file2comp.addEvent(event3);
        file2comp.addEvent(event4);
        file2comp.addEvent(event5);
        file2comp.addEvent(event6);
        file2comp.addEvent(event7);
        System.out.println("maj");
	}
	
	
	
	public static void main(String[] args) {
		Midifile mid = new Midifile();
		ArrayList<Long> list = mid.getFile2comp().getIntervales();
		System.out.println("liste inter : " + list + " tempo " + mid.getFile2comp().getTempo());
	}
	

}
