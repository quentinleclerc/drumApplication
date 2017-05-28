package midi;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import player.Drummer;;

public class TempsCercles extends HashMap<Integer,Long>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Map<Integer, Double> mapDistance;
	private final double vitesse;
	
	public TempsCercles(Map<Integer, Double> kickDistance,float vitesse) {
//		this.mapDistance=mapDistance;
		this.mapDistance=kickDistance;
		this.vitesse=vitesse;
		computeTime();
	}
	
	public void computeTime(){
//		HashMap<Integer,Double> mapTemps = new HashMap<Integer,Double>();
		for (java.util.Map.Entry<Integer, Double> entry : this.mapDistance.entrySet()){
			this.put(entry.getKey(), (long)( entry.getValue()/this.vitesse));
		}
	}
	

	public static void main(String[] args) {
		TempsCercles temps = new TempsCercles(null,12.0F);
		System.out.println(temps);
	}
}

