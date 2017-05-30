package midi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Scores {

	private final static long WIN_SCORE = 10;

	private double score_tempo;
	private double score_decalage;
	public double score_tot;
	public double score_max;
	private float perf;
	
	private int nbRep;

	private SoundRecord original_song;

	public HashMap<Integer,Double> scoreNotes = new HashMap<>(); // hashmap contenant les notes et son score
	public HashMap<Integer,Double> maxScoreNotes = new HashMap<>(); // hashmap contenant pour chaque note son score max
	public HashMap<Integer,Integer> noteAvOrRet = new HashMap<>(); // hashmap contenant pour chaque note si le joueur est plutot en avance (pos) ou en retard (neg)
	private HashMap<Integer,Integer> noteTimesMissed = new HashMap<>(); // hashmap contenant pour chaque note le nombre de fois qu'on l'a rate

	private HashMap<Integer, Long> intervalles_min; // hashmap contenant les intervalles mini par note
	private HashMap<Integer, Long> precision = new HashMap<>(); // hashmap contenant la precision pour chaque note
	private HashMap<Integer, SoundRecord> recordByNote = new HashMap<>(); // hashmap contenant un tableau de notes par note

	private boolean looping;

	public ArrayList<Long> initializeSong(SoundRecord original, boolean looping){
		this.looping = looping;
		this.original_song = original;
		this.score_decalage = 0;
		this.score_tempo = 0;
		
		this.nbRep = 0;			
		

	    setIntervallesMin();
	    setFenetreMin();
	    setScoresMax();
	    initAvRet();
	    ArrayList<Long> SongCut = cutSong(50);
		// System.out.println("recordbynote : " + recordByNote.toString());
		// System.out.println("intervalles min : " + intervalles_min.toString());
		// System.out.println("precision : " + precision.toString());

	    return SongCut;
	}

	private ArrayList<Long> cutSong(long minIntervalle) {
		ArrayList<Long> cutted = new ArrayList<>();
		ArrayList<Long> intervalles = original_song.getIntervales();
		Boolean differentNote = false;
		Boolean PrevTime = false;
		Boolean FollowingTime = false;
		Boolean more1Sec = false;
		int note;
		int notes = 0;
		long tempsDeb = 0;
		for (int i = 0; i < original_song.size()-1; i++) {
			notes++;
			note = original_song.get(i).getNote();
			differentNote = (note != original_song.get(i+1).getNote());
			PrevTime = (intervalles.get(i+1)) > intervalles_min.get(note) || intervalles.get(i+1) >= 100;
			FollowingTime = (intervalles.get(i+1)) > intervalles_min.get(original_song.get(i+1).getNote()) || intervalles.get(i+1) >= 100;
			more1Sec = (original_song.get(i+1).getTemps() - tempsDeb) > minIntervalle;
			if (PrevTime && FollowingTime && notes > 4 && more1Sec){
				cutted.add((original_song.get(i+1).getTemps()+original_song.get(i).getTemps())/2);
				notes = 0;
				tempsDeb = (original_song.get(i+1).getTemps()+original_song.get(i).getTemps())/2;
			}
		}
		return cutted;
	}

	private void initAvRet() {
		Iterator<Entry<Integer, Double>> it = scoreNotes.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Double> pair = it.next();
	        int note = pair.getKey();
	        noteAvOrRet.put(note, 0);
	    }
	}

	private void setScoresMax() {
		score_max = original_song.size()*2;
		// System.out.println("score max : " +  score_max);

		Iterator<Entry<Integer, SoundRecord>> it = recordByNote.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, SoundRecord> pair = it.next();
			maxScoreNotes.put(pair.getKey(), (double) (pair.getValue().size()*2));
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
				+ " alors que le score max a atteindre etait de " + score_max + ".\n";
		result += "Vous avez bouclé " + nbRep + " fois sur le même morceau.\n";
		
		Iterator<Entry<Integer, Double>> it = scoreNotes.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Double> pair = it.next();
	        int note = pair.getKey();
	        double scoreNote = (pair.getValue()/maxScoreNotes.get(note))*100;
	        result += "Pour la note " + note + " vous avez obtenu un score de " + scoreNote + "%\n";
	        if (scoreNote<=50){
	        	result += "Ceci s'explique car vous avez raté " + noteTimesMissed.get(note) + " fois la note";
	        	if (noteAvOrRet.size() != 0 && noteAvOrRet.containsKey(note) && noteAvOrRet.get(note) < 0){
	        		result += ", et vous etiez majoritairement en retard.\n";
	        	}
	        	else if (noteAvOrRet.size() != 0 && noteAvOrRet.containsKey(note) && noteAvOrRet.get(note) > 0){
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
		// System.out.println("nb erp : " + nbRep);

		score_max = score_max*(nbRep);
		Iterator<Entry<Integer, Double>> it = maxScoreNotes.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Double> pair = it.next();
	        int note = pair.getKey();
	        maxScoreNotes.put(note, pair.getValue()*nbRep);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
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
		initializeSong(rec, looping);
		return rec;
	}
	
	public double compare_table(SoundRecord record, SoundRecord played){
        double tempo = score_tempo;
        double decalage = score_decalage;
        if (played.size() == 0){
			boolean begin = false;
			if (begin){ // si on a deja commence et que on recoit une sequence vide
                tempo -= 2;
                decalage -= 2;
        	}
        	// si on a pas encore demarre on fait rien
        }
        else {
            this.nbRep++;
            if (record.size() != played.size() ){
                tempo -= 1;
            }
            else{
                tempo += tempo(record,played);//compare le decalage par rapport au tempo (PGCD)
            }
            decalage += decalage(record,played);//compare le decalage par rapport au morceau initial
        }
        double score =(long) (( 0.2*tempo + 1.8*decalage)/ 2);
        score_tot += score;
        // System.out.println("nouveau score tot : " + score_tot);

        return score;
    }

    private double decalage(SoundRecord record, SoundRecord played) {
    	
        SoundRecord recE = record;
        SoundRecord playE = played;
        double score=0;
        // System.out.println("decalage");
		for (Event event : recE) {
			Boolean found = onTime(event, playE);
			if (found) {
				score += 2;
				// System.out.println("augmentation de 1 : " + score);

				int note = event.getNote();
				Double scoreNote = scoreNotes.get(note);
				if (scoreNote == null) {
					scoreNotes.put(note,  2.0);
				} else {
					scoreNotes.put(note,scoreNote + 2);
				}
			} else {
				score -= 0.5;
				int note = event.getNote();
				Double scoreNote = scoreNotes.get(note);
				if (scoreNote == null) {
					scoreNotes.put(note,  -0.5);
				} else {
					scoreNotes.put(note,  scoreNote - 0.5);
				}
				// System.out.println("diminution de 0.5 : " + score);
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
    
	private double tempo(SoundRecord record, SoundRecord played) {
        ArrayList<Long> intR = record.getIntervales();
        ArrayList<Long> intL = played.getIntervales();
        double score=0;
        // System.out.println("tempo");
        for (int i = 0; i < intR.size(); i++) {
        	int note = record.get(i).getNote();
            if ( intL.get(i) >= (intR.get(i)-precision.get(note)) && intL.get(i) <= (intR.get(i)+precision.get(note))){
                score+=2;
                // System.out.println("augmentation de 1 : " + score);
            }
            else{
                score-=0.5;
                // System.out.println("diminution de 0.5 : " + score);
            }
        }
        return score;
    }

}
