package midi;

public class Maine {
	
	public static final int KICK = 35; // pied
	public static final int SNARE = 38; // gauche
	public static final int HIGH_TOM = 50; // millieu gauche
	public static final int MIDDLE_TOM = 43; // millieu droite
	public static final int FLOOR_TOM = 41; // droite
	
	
	public static void main(String[] args) {
		Scores sc = new Scores();
		
//		SoundRecord EatSleepRaveRepeat = new SoundRecord("EatSleepRaveRepeat");
//
//	    Event e1 = new Event(200, KICK, 100);
//	    Event e2 = new Event(400, FLOOR_TOM, 100);
//	    Event e3 = new Event(600, SNARE, 100);
//	    Event e4 = new Event(800, HIGH_TOM, 100);
//	    Event e5 = new Event(1000, KICK, 100);
////	    Event e6 = new Event(1300, KICK, 100);
////	    Event e7 = new Event(1600, KICK, 100);
////	    Event e8 = new Event(1900, KICK, 100);
//	    EatSleepRaveRepeat.addEvent(e1);
//	    EatSleepRaveRepeat.addEvent(e2);
//	    EatSleepRaveRepeat.addEvent(e3);
//	    EatSleepRaveRepeat.addEvent(e4);
//	    EatSleepRaveRepeat.addEvent(e5);
////	    EatSleepRaveRepeat.addEvent(e6);
////	    EatSleepRaveRepeat.addEvent(e7);
////	    EatSleepRaveRepeat.addEvent(e8);
////	    EatSleepRaveRepeat.addEvent(e9);
////	    EatSleepRaveRepeat.addEvent(e);
////	    EatSleepRaveRepeat.addEvent(e12);
//		sc.initializeSong(EatSleepRaveRepeat,1);
//		
//
//		SoundRecord eat1 = new SoundRecord("eat1");
//	    Event a = new Event(200, KICK, 100);
//	    Event b = new Event(400, FLOOR_TOM, 100);
//	    Event c = new Event(600, SNARE, 100);
//	    Event d = new Event(750, HIGH_TOM, 100);
//	    Event q = new Event(1000, KICK, 100);
//	    eat1.addEvent(a);
//	    eat1.addEvent(b);
//	    eat1.addEvent(c);
//	    eat1.addEvent(d);
//	    eat1.addEvent(q);
//		SoundRecord eat2 = new SoundRecord("eat2");
//	    Event i = new Event(200, KICK, 100);
//	    Event f = new Event(400, FLOOR_TOM, 100);
//	    Event g = new Event(600, SNARE, 100);
//	    Event h = new Event(650, HIGH_TOM, 100);
//	    Event w = new Event(1000, KICK, 100);
//	    eat2.addEvent(i);
//	    eat2.addEvent(f);
//	    eat2.addEvent(g);
//	    eat2.addEvent(h);
//	    eat2.addEvent(w);
////	    sc.compare_table(EatSleepRaveRepeat, EatSleepRaveRepeat);
////	    sc.compare_table(EatSleepRaveRepeat, EatSleepRaveRepeat);
////	    sc.compare_table(EatSleepRaveRepeat, EatSleepRaveRepeat);
////	    sc.compare_table(EatSleepRaveRepeat, EatSleepRaveRepeat);
//	    sc.compare_table(EatSleepRaveRepeat, EatSleepRaveRepeat);
//		System.out.println(sc.end_record());
//	    System.out.println("score notes : " + sc.scoreNotes);
//	    System.out.println("score total : " + sc.score_tot);
//	    System.out.println("score max : " + sc.score_max);
//	    System.out.println("max score notes : " + sc.maxScoreNotes);
//	    System.out.println("note av or ret: " + sc.noteAvOrRet);
//	  
//		System.out.println(sc.getStats());

		SoundRecord example = new SoundRecord("example");
		
		example.add(new Event(200, HIGH_TOM, 100));
		example.add(new Event(400, MIDDLE_TOM, 100));
		example.add(new Event(460, MIDDLE_TOM, 100));
		example.add(new Event(550, SNARE, 100));
		example.add(new Event(700, HIGH_TOM, 100));
		example.add(new Event(900, MIDDLE_TOM, 100));
		example.add(new Event(1000, FLOOR_TOM, 100));
		
		example.add(new Event(1200, KICK, 100));
		example.add(new Event(1260, KICK, 100));
		example.add(new Event(1300, HIGH_TOM, 100));
		example.add(new Event(1500, SNARE, 100));
		example.add(new Event(1700, HIGH_TOM, 100));		
		example.add(new Event(1800, HIGH_TOM, 100));
		example.add(new Event(2000, MIDDLE_TOM, 100));
		example.add(new Event(2040, KICK, 100));
		example.add(new Event(2140, KICK, 100));		
		example.add(new Event(2200, SNARE, 100));
		
		example.add(new Event(2400, HIGH_TOM, 100));
		example.add(new Event(2500, HIGH_TOM, 100));
		example.add(new Event(2600, FLOOR_TOM, 100));		
		example.add(new Event(2850, HIGH_TOM, 100));
		example.add(new Event(3000, SNARE, 100));
		example.add(new Event(3050, HIGH_TOM, 100));
		example.add(new Event(3090, SNARE, 100));
		example.add(new Event(3110, HIGH_TOM, 100));
		example.add(new Event(3300, SNARE, 100));		
		example.add(new Event(3330, HIGH_TOM, 100));
		example.add(new Event(3390, HIGH_TOM, 100));
		example.add(new Event(3400,KICK , 100));
		
		example.add(new Event(3700, HIGH_TOM, 100));
		example.add(new Event(4000, SNARE, 100));		
		example.add(new Event(4200, SNARE, 100));
		example.add(new Event(4250, KICK, 100));
		example.add(new Event(4400, KICK, 100));
		
		example.add(new Event(4700, HIGH_TOM, 100));
		example.add(new Event(5000, FLOOR_TOM, 100));
		System.out.println(sc.initializeSong(example, false));
	}


}
