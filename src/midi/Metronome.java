package midi;

public class Metronome extends SoundRecord {
	private Integer bpm;
	private static final int TIC = 51;
	private static final int VEL = 51;
	public Metronome(String name,int bpm, int note) {
		super(name);
		this.setBpm(bpm);
		Event e;
		for (int i = 0; i < 20; i++) {
			e = new Event((60/bpm)*100, TIC, VEL);
			this.add(e);			
		}
	}
	public Integer getBpm() {
		return bpm;
	}
	public void setBpm(Integer bpm) {
		this.bpm = bpm;
	}

}
