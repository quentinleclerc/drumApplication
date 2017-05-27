package midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Scores {


	private static long score_tempo;
	private static long score_decalage;
	private static long score_tot;
	private static long score_max;
	private static HashMap<Integer,Long> scoreNotes = new HashMap<>(); // hashmap contenant les notes et son score
	private static HashMap<Integer,Integer> maxScoreNotes = new HashMap<>(); // hashmap contenant pour chaque note son score max
	private static HashMap<Integer,Integer> noteAvOrRet = new HashMap<>(); // hashmap contenant pour chaque note si le joueur est plutot en avance (pos) ou en retard (neg)
	private static HashMap<Integer,Integer> noteTimesMissed = new HashMap<>(); // hashmap contenant pour chaque note le nombre de fois qu'on l'a rate
	private final static long win_score = 10;
	private static float perf;
	
	private static boolean begin = false;

	public static final int KICK = 35; // pied
	public static final int SNARE = 38; // gauche
	public static final int HIGH_TOM = 50; // millieu gauche
	public static final int MIDDLE_TOM = 43; // millieu droite
	public static final int FLOOR_TOM = 41; // droite
	
	private static SoundRecord original_song;
	private static HashMap<Integer, Long> intervalles_min; // hashmap contenant les intervalles mini par note
	private static HashMap<Integer, Long> precision = new HashMap<Integer, Long>(); // hashmap contenant la precision pour chaque note
	private static HashMap<Integer, ArrayList<Event>> recordByNote = new HashMap<>(); // hashmap contenant un tableau de notes par note
	
	public static void initiliazeSong(SoundRecord original){
	    Scores.setOriginal_song(original);
	    setIntervallesMin();
	    setFenetreMin();
	    setScoresMax();
	    score_decalage = 0;
	    score_tempo = 0;
		System.out.println("recordbynote : " + recordByNote.toString());
		System.out.println("intervalles min : " + intervalles_min.toString());
		System.out.println("precision : " + precision.toString());
	}
	    

	private static void setScoresMax() {
		score_max = original_song.getEvents().size()*2;	
		System.out.println("score max : " +  score_max);
		Iterator<Entry<Integer, ArrayList<Event>>> it = recordByNote.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, ArrayList<Event>> pair = it.next();
			maxScoreNotes.put(pair.getKey(), pair.getValue().size()*2);
	    }
	}


	private static void setFenetreMin() {
		Iterator<Entry<Integer, Long>> it = intervalles_min.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Long> pair = it.next();
	        Long value = pair.getValue();
	        Long window;
	        if (value<100){
	        	 window = value/4;
	        }
	        else if (value<250){
	        	 window = value/6;	        	
	        }
	        else if (value<600){
	        	 window = value/8;	        	
	        }
	        else{
	        	window = (long) 100;
	        }
			precision.put((Integer) pair.getKey(), window);
	    }
		
	}


	private static void setIntervallesMin() {
		ArrayList<Event> events= Scores.getOriginal_song().getEvents();
		intervalles_min = new HashMap<>();
		HashMap<Integer, ArrayList<Event>> notes = new HashMap<>();
		for (int i = 0; i < events.size(); i++) { // remplir la hashmap contenant les notes, et les events associes
			if (notes.containsKey(events.get(i).getNote())){
				ArrayList<Event> notesProv = notes.get(events.get(i).getNote());
				notesProv.add(events.get(i));
				notes.put(events.get(i).getNote(), notesProv);
			}
			else{
				ArrayList<Event> notesProv = new ArrayList<>();
				notesProv.add(events.get(i));
				notes.put(events.get(i).getNote(), notesProv);
			}
		}
		recordByNote = notes;
		Iterator<Entry<Integer, ArrayList<Event>>> it = notes.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, ArrayList<Event>> pair = it.next();
	        SoundRecord temp = new SoundRecord("temp");
			temp.setEvents((ArrayList<Event>) pair.getValue());
			long minInter = temp.getMinInter();
			intervalles_min.put((Integer) pair.getKey(), minInter);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}


	


	public static SoundRecord getOriginal_song() {
		return original_song;
	}


	public static void setOriginal_song(SoundRecord original_song) {
		Scores.original_song = original_song;
	}

	public static String getStats(){
		String result = null;
		result = "Pour la chanson " + original_song.getNom()  + " vous avez obtenu un score de " + score_tot 
				+ " alors que le score max a atteindre etait de " + win_score + ".\n";
		Iterator<Entry<Integer, Long>> it = scoreNotes.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Long> pair = it.next();
	        int note = pair.getKey();
	        float scoreNote = ((float)pair.getValue()/(float)maxScoreNotes.get(note))*100;
	        result += "Pour la note " + note + " vous avez obtenu un score de " + scoreNote + "%\n";
	        if (scoreNote<=50){
	        	result += "Ceci s'explique car vous avez raté " + noteTimesMissed.get(note) + " fois la note";
	        	if (noteAvOrRet.size() != 0 && noteAvOrRet.get(note) < 0){
	        		result += ", et vous etiez majoritairement en retard.\n";
	        	}
	        	else if (noteAvOrRet.size() != 0 && noteAvOrRet.get(note) > 0){
	        		result += ", et vous etiez majoritairement en avanc.\n";	        		
	        	}
	        	else{
	        		result += ".\n";	 	        		
	        	}
	        }
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
		return result;
	}
	
	public static String end_record(){
		float performance = ((float)score_tot/(float)score_max)*100 ;
		perf = performance;
		return "Vous avez obtenu un score de " + 
			    performance + "%";
		
	}
	
	public static SoundRecord adapterNiveau(){
		float performance = perf;
		SoundRecord rec = original_song;
		if (perf <= 20){
			original_song.changeTempo(1.5);
		}
		else if (perf <= 50){
			original_song.changeTempo(1.833 - 1.66*performance);			
		}
		else if (perf <= 60){			
		}
		else if (perf <= 90){
			original_song.changeTempo(2 - 1.66*performance);			
		}
		else{
			original_song.changeTempo(0.5);				
		}
		initiliazeSong(rec);
		return rec;
	}
	
	private static long compare_table(SoundRecord record, SoundRecord played){
        long tempo= score_tempo;
        long decalage = score_decalage;
        if (played.getEvents().size()==0){ 
        	if (begin){ // si on a deja commence et que on recoit une sequence vide
                tempo -= 2;
                decalage -= 2;
        	}
        	// si on a pas encore demarre on fait rien
        }
        else {
            if (record.getEvents().size() != played.getEvents().size() ){
                tempo -= 1;
            }
            else{
                tempo += tempo(record,played);//compare le decalage par rapport au tempo (PGCD)
            }
            decalage += decalage(record,played);//compare le decalage par rapport au morceau initial
        }
        long score =( tempo + decalage)/ 2;
        score_tot = score;
        return score;
    }






    private static int decalage(SoundRecord record, SoundRecord played) {
    	
        ArrayList<Event> recE = record.getEvents();
        ArrayList<Event> playE = played.getEvents();
        int score=0;
        for (int i = 0; i < recE.size(); i++) {
        	Event event = recE.get(i);
        	Boolean found = onTime(event,playE);
            if (found){
                score+=2;
                System.out.println("augmentation de 1 : " + Integer.toString(score));
                int note = event.getNote();
				Object scoreNote = scoreNotes.get(note);
				if ( scoreNote == null){
	                scoreNotes.put(note, (long) 2);					
				}
				else{
	                scoreNotes.put(note, (long) scoreNote+2);
				}
            }
            else{
                score-=1;
                int note = event.getNote();
                Object scoreNote = scoreNotes.get(note);
				if ( scoreNote == null){
	                scoreNotes.put(note, (long) -1);					
				}
				else{
	                scoreNotes.put(note, (long) scoreNote-1);
				}
                System.out.println("diminution de 0.5 : " + Integer.toString(score));
            }
        }
        return score;
    }

    private static Boolean onTime(Event event, ArrayList<Event> playE) {
		Boolean found = false;
		int note = event.getNote();
		int rate =0;
		long prec = precision.get(note);
		long fenMin = event.getTemps() - prec;
		long fenMax= event.getTemps() + prec;
		for (Event evento : playE) {
			if (evento.getNote()==note && evento.getTemps()>=fenMin && evento.getTemps()<=fenMax){
				found =true;					
			}
		}
		if (!found){
			for (Event evento : playE) {
				if (evento.getNote()==note && evento.getTemps()>=fenMin*2 && evento.getTemps()<=fenMin){ //avance
					System.out.println("en avanec");
			        Object oldSc = noteAvOrRet.get(note);
			        if (oldSc == null){
			        	noteAvOrRet.put(note, 1); //il est en avance			        	
			        }else{
			        	noteAvOrRet.put(note, (int)oldSc+1); //il est en avance
			        }
				}
				else if (evento.getNote()==note && evento.getTemps()<=fenMax*2){ //retard
					System.out.println("en retard car " + evento + " est en retard par rapport a " + event);
					Object oldSc = noteAvOrRet.get(note);
					if (oldSc == null){
			        	noteAvOrRet.put(note, 1); //il est en retard			        	
			        }else{
			        	noteAvOrRet.put(note, (int)oldSc-1); 
			        }
				}
				else{ //rate
					rate =1;
				}
				
			}
			if (rate==1){
				Object oldMis = noteTimesMissed.get(note);
		        if (oldMis == null){
		        	noteTimesMissed.put(note, 1); //il a rate une note
		        }else{
			        noteTimesMissed.put(note, (int)oldMis+1); 
		        }
			}
		}
		
		return found;
	}
    
	private static int tempo(SoundRecord record, SoundRecord played) {
        ArrayList<Long> intR = record.getIntervales();
        ArrayList<Long> intL = played.getIntervales();
        int score=0;
        for (int i = 0; i < intR.size(); i++) {
        	int note = record.getEvents().get(i).getNote();
        	System.out.println("note : " + note + " precision : " + precision.get(note) + " note recue : " + intL.get(i));
            if ( intL.get(i) >= (intR.get(i)-precision.get(note)) && intL.get(i) <= (intR.get(i)+precision.get(note))){
                score+=2;
                System.out.println("augmentation de 1 : " + Integer.toString(score));
            }
            else{
                score-=1;
                System.out.println("diminution de 0.5 : " + Integer.toString(score));
            }
        }
        return score;
    }
    
    public static void main(String[] args) {
		SoundRecord EatSleepRaveRepeat = new SoundRecord("EatSleepRaveRepeat");
//	    Event e1 = new Event(500, HIGH_TOM, 100);
//	    Event e2 = new Event(1200, MIDDLE_TOM, 100);
//	    Event e3 = new Event(1500, SNARE, 100);
//	    Event e12 = new Event(1700, SNARE, 100);
//	    Event e4 = new Event(2000, FLOOR_TOM, 100);
//	    Event e5 = new Event(2200, FLOOR_TOM, 100);
//	    Event e6 = new Event(200, KICK, 100);
//	    Event e7 = new Event(400, KICK, 100);
//	    Event e8 = new Event(600, KICK, 100);
//	    Event e9 = new Event(800, KICK, 100);
//	    Event e = new Event(1000, KICK, 100);

	    Event e1 = new Event(200, KICK, 100);
	    Event e2 = new Event(400, FLOOR_TOM, 100);
	    Event e3 = new Event(600, SNARE, 100);
	    Event e4 = new Event(800, HIGH_TOM, 100);
	    Event e5 = new Event(1000, KICK, 100);
	    Event e6 = new Event(1300, KICK, 100);
	    Event e7 = new Event(1600, KICK, 100);
	    Event e8 = new Event(1900, KICK, 100);
	    EatSleepRaveRepeat.addEvent(e1);
	    EatSleepRaveRepeat.addEvent(e2);
	    EatSleepRaveRepeat.addEvent(e3);
	    EatSleepRaveRepeat.addEvent(e4);
	    EatSleepRaveRepeat.addEvent(e5);
	    EatSleepRaveRepeat.addEvent(e6);
	    EatSleepRaveRepeat.addEvent(e7);
	    EatSleepRaveRepeat.addEvent(e8);
//	    EatSleepRaveRepeat.addEvent(e9);
//	    EatSleepRaveRepeat.addEvent(e);
//	    EatSleepRaveRepeat.addEvent(e12);
		Scores.initiliazeSong(EatSleepRaveRepeat);
		

		SoundRecord eat1 = new SoundRecord("eat1");
	    Event a = new Event(200, KICK, 100);
	    Event b = new Event(400, FLOOR_TOM, 100);
	    Event c = new Event(600, SNARE, 100);
	    Event d = new Event(800, HIGH_TOM, 100);
	    Event q = new Event(1000, KICK, 100);
	    eat1.addEvent(a);
	    eat1.addEvent(b);
	    eat1.addEvent(c);
	    eat1.addEvent(d);
	    eat1.addEvent(q);
		SoundRecord eat2 = new SoundRecord("eat2");
	    Event i = new Event(200, KICK, 100);
	    Event f = new Event(400, FLOOR_TOM, 100);
	    Event g = new Event(600, SNARE, 100);
	    Event h = new Event(800, HIGH_TOM, 100);
	    Event w = new Event(1000, KICK, 100);
	    eat2.addEvent(i);
	    eat2.addEvent(f);
	    eat2.addEvent(g);
	    eat2.addEvent(h);
	    eat2.addEvent(w);
	    compare_table(EatSleepRaveRepeat, eat2);
	    System.out.println("score notes : " + scoreNotes);
	    System.out.println("score total : " + score_tot);
	    System.out.println("max score notes : " + maxScoreNotes);
	    System.out.println("note av or ret: " + noteAvOrRet);
	  
		System.out.println(end_record());
		System.out.println(getStats());
	}

}
