package midi;

import player.Drummer;
import java.util.ArrayList;


public class SoundRecord {

	private ArrayList<Event> events;
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	private String nom;

	public SoundRecord(String nom) {
		this.events = new ArrayList<Event>();
		this.nom = nom;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public long getTempo(){
		ArrayList<Long> inter = getIntervales();
		int max = 0;
		long tempo = 0;
		for (int i = 0; i < inter.size(); i++) {
			int count = 0;
			long loc_tempo = inter.get(i);
			for (Long anInter : inter) {
				long ir = inter.get(i);
				long jr = anInter;
				if (ir == jr) {
					count++;
				}
			}
			if (count > max){
				max = count;
				tempo = loc_tempo;
			}
			else if (count==max){
				max=count;
				if (loc_tempo < tempo){
					tempo=loc_tempo;
				}
			}
		}
		return tempo; // a changer pour trouver le pgcd
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
		
		for (int i = 0; i<this.getEvents().size();i++) {
			if (i==0){
				list.add(i, this.getEvents().get(i).getTemps());
				
			}
			else{
				list.add(i, this.getEvents().get(i).getTemps()-this.getEvents().get(i-1).getTemps());
			}
		}
		return list;
	}
	
	public void clean(){
		events.clear();
		
	}
	
	public void addEvent(Event event){
		this.events.add(event);
	}
	
	public String toString(){
		return events.toString();
	}


	public String getNom() {
		return nom;
	}

	public void play(Drummer drummer) {
		int first = 0;
		int size = 0;
		ArrayList<Long> list = this.getIntervales();
		long temps = list.get(0);
		try {
			Thread.sleep(temps);
			int note = this.getEvents().get(0).getNote();
			int velocity = this.getEvents().get(0).getVelocity();

			drummer.noteOn(note, velocity);

			int i;

			System.out.println("Debut file");

			for (i = 0; i <= list.size()-1; i++) {
				if (first == 0){
					first++;
				}
				else {
					if (i == list.size()){
						temps = list.get(0);
					}
					else {
						temps = list.get(i);
					}

					Thread.sleep(temps - 100);

					System.out.println("Valeur de i = " + i);
					note = this.getEvents().get(i).getNote();
					velocity = this.getEvents().get(i).getVelocity();

					drummer.noteOn(note, velocity);
				}
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void changeTempo(double d) {
		ArrayList<Long> inter = getIntervales();
		for (int i = 0; i < events.size(); i++) {
			if (i==0){
				int temps = (int) (inter.get(i)*d); 
				events.get(i).setTemps(temps);					
			}
			else{
				int temps = (int) (inter.get(i)*d); 
				events.get(i).setTemps(events.get(i-1).getTemps() + temps);	
			}
			System.out.println("nouveaux temps: "+ events);
		}
		
	}

}
