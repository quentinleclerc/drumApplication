package midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Scores {

	private final static long WIN_SCORE = 10;

	private long score_tempo;
	private long score_decalage;
	private long score_tot;
	private long score_max;
	private float perf;

	private SoundRecord original_song;

	private HashMap<Integer,Long> scoreNotes = new HashMap<>(); // hashmap contenant les notes et son score
	private HashMap<Integer,Integer> maxScoreNotes = new HashMap<>(); // hashmap contenant pour chaque note son score max
	private HashMap<Integer,Integer> noteAvOrRet = new HashMap<>(); // hashmap contenant pour chaque note si le joueur est plutot en avance (pos) ou en retard (neg)
	private HashMap<Integer,Integer> noteTimesMissed = new HashMap<>(); // hashmap contenant pour chaque note le nombre de fois qu'on l'a rate

	private HashMap<Integer, Long> intervalles_min; // hashmap contenant les intervalles mini par note
	private HashMap<Integer, Long> precision = new HashMap<>(); // hashmap contenant la precision pour chaque note
	private HashMap<Integer, SoundRecord> recordByNote = new HashMap<>(); // hashmap contenant un tableau de notes par note

	public void initializeSong(SoundRecord original){
		this.original_song = original;
		this.score_decalage = 0;
		this.score_tempo = 0;

	    setIntervallesMin();
	    setFenetreMin();
	    setScoresMax();

		System.out.println("recordbynote : " + recordByNote.toString());
		System.out.println("intervalles min : " + intervalles_min.toString());
		System.out.println("precision : " + precision.toString());
	}

	private void setScoresMax() {
		score_max = original_song.size()*2;
		System.out.println("score max : " +  score_max);
		Iterator<Entry<Integer, SoundRecord>> it = recordByNote.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, SoundRecord> pair = it.next();
			maxScoreNotes.put(pair.getKey(), pair.getValue().size()*2);
	    }
	}

	private void setFenetreMin() {
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

	private void setIntervallesMin() {
		intervalles_min = new HashMap<>();
		HashMap<Integer, SoundRecord> notes = new HashMap<>();

		for (Event event : this.original_song) { // remplir la hashmap contenant les notes, et les events associes
			if (notes.containsKey(event.getNote())) {
				SoundRecord notesProv = notes.get(event.getNote());
				notesProv.add(event);
				notes.put(event.getNote(), notesProv);
			} else {
				SoundRecord notesProv = new SoundRecord("notesProv");
				notesProv.add(event);
				notes.put(event.getNote(), notesProv);
			}
		}
		recordByNote = notes;
		Iterator<Entry<Integer, SoundRecord>> it = notes.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, SoundRecord> pair = it.next();
			SoundRecord temp = pair.getValue();
			long minInter = temp.getMinInter();
			intervalles_min.put((Integer) pair.getKey(), minInter);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public String getStats(){
		String result = null;
		result = "Pour la chanson " + original_song.getNom()  + " vous avez obtenu un score de " + score_tot 
				+ " alors que le score max a atteindre etait de " + WIN_SCORE + ".\n";
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
	
	public String end_record(){
		float performance = ((float)score_tot/(float)score_max)*100 ;
		perf = performance;
		return "Vous avez obtenu un score de " + 
			    performance + "%";
		
	}
	
	public SoundRecord adapterNiveau(){
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
		initializeSong(rec);
		return rec;
	}
	
	public long compare_table(SoundRecord record, SoundRecord played){
        long tempo = score_tempo;
        long decalage = score_decalage;
        if (played.size() == 0){
			boolean begin = false;
			if (begin){ // si on a deja commence et que on recoit une sequence vide
                tempo -= 2;
                decalage -= 2;
        	}
        	// si on a pas encore demarre on fait rien
        }
        else {
            if (record.size() != played.size() ){
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

    private int decalage(SoundRecord record, SoundRecord played) {
    	
        SoundRecord recE = record;
        SoundRecord playE = played;
        int score=0;
		for (Event event : recE) {
			Boolean found = onTime(event, playE);
			if (found) {
				score += 2;
				System.out.println("augmentation de 1 : " + Integer.toString(score));
				int note = event.getNote();
				Object scoreNote = scoreNotes.get(note);
				if (scoreNote == null) {
					scoreNotes.put(note, (long) 2);
				} else {
					scoreNotes.put(note, (long) scoreNote + 2);
				}
			} else {
				score -= 1;
				int note = event.getNote();
				Object scoreNote = scoreNotes.get(note);
				if (scoreNote == null) {
					scoreNotes.put(note, (long) -1);
				} else {
					scoreNotes.put(note, (long) scoreNote - 1);
				}
				System.out.println("diminution de 0.5 : " + Integer.toString(score));
			}
		}
        return score;
    }

	private Boolean onTime(Event event, SoundRecord playE) {
		Boolean found = false;
		int note = event.getNote();
		int rate =0;
		long prec = precision.get(note);
		long fenMin = event.getTemps() - prec;
		long fenMax= event.getTemps() + prec;
		long fenMin2 = event.getTemps() - 2*prec;
		long fenMax2= event.getTemps() + 2*prec;
		for (Event evento : playE) {
			if (evento.getNote()==note && evento.getTemps()>=fenMin && evento.getTemps()<=fenMax){
				found =true;
			}
		}
		if (!found){
			for (Event evento : playE) {
				if (evento.getNote()==note && evento.getTemps()>=fenMin2 && evento.getTemps()<=fenMin){ //avance
					Object oldSc = noteAvOrRet.get(note);
					if (oldSc == null){
						noteAvOrRet.put(note, 1); //il est en avance
					}else{
						noteAvOrRet.put(note, (int)oldSc+1); //il est en avance
					}
				}
				else if (evento.getNote()==note && evento.getTemps()<=fenMax2 && evento.getTemps()>=fenMax ){ //retard
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
    
	private int tempo(SoundRecord record, SoundRecord played) {
        ArrayList<Long> intR = record.getIntervales();
        ArrayList<Long> intL = played.getIntervales();
        int score=0;
        for (int i = 0; i < intR.size(); i++) {
        	int note = record.get(i).getNote();
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

}
