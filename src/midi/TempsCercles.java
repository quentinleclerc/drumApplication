package midi;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import player.Drummer;;

public class TempsCercles extends HashMap<Integer,Long>{
	private final HashMap<Integer,Integer> mapDistance;
	private final double vitesse;
	
	public TempsCercles(HashMap<Integer,Integer> mapDistance,float vitesse) {
//		this.mapDistance=mapDistance;
		this.mapDistance=mapDistance;
		this.vitesse=vitesse;
		computeTime();
	}
	
	public void computeTime(){
//		HashMap<Integer,Double> mapTemps = new HashMap<Integer,Double>();
		for (java.util.Map.Entry<Integer, Integer> entry : this.mapDistance.entrySet()){
			this.put(entry.getKey(), (long)( entry.getValue()/this.vitesse));
		}
	}
	

	public static void main(String[] args) {
		TempsCercles temps = new TempsCercles(null,12.0F);
		System.out.println(temps);
	}
}

