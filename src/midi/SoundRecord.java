package midi;

import java.util.ArrayList;

public class SoundRecord {

	private ArrayList<Event> events;

	private String nom;

	public SoundRecord(String nom) {
		this.events = new ArrayList<Event>();
		this.nom = nom;
	}

	public SoundRecord(String nom, ArrayList<Event> events) {
		this.events = events;
		this.nom = nom;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	public void addEvent(Event event){
		this.events.add(event);
	}

	public String getNom() {
		return nom;
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

	public String toString(){
		return events.toString();
	}

}
