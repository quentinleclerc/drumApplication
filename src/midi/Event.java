package midi;

import java.io.Serializable;

public class Event implements Serializable{


	private long timeMilis;

	private int note;

	private int velocity;

	public Event(long temps, int note, int velocity) {
		timeMilis = temps;
		this.note = note;
		this.velocity = velocity;
	}

	public void setTemps(long temps) {
		timeMilis = temps;
	}

	public long getTemps() {
		return timeMilis;
	}

	public int getNote() {
		return note;
	}

	public int getVelocity(){
		return velocity;
	}

	@Override
	public String toString() {
		return "Event [Temps = " + timeMilis + ", Note = " + note
				+ ", Velocity = " + velocity + "]\n";
	}
	
	

}