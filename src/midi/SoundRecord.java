package midi;

import java.io.Serializable;
import java.util.ArrayList;


public class SoundRecord extends  ArrayList<Event> implements Serializable {

	private String name;
		
	public SoundRecord(String name) {
		super();
		this.name = name;
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

	public SoundRecord getSub(long timeDeb, long timeEnd) {
		int i;
		SoundRecord sub = new SoundRecord("Sub_record");
		for (i = 0; i < this.size(); i++) {
			if (this.get(i).getTemps() > timeDeb && this.get(i).getTemps() < timeEnd) {
				sub.add(this.get(i));
			}
		}
		return sub;
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

}
