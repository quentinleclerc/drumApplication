package player;

import javax.sound.midi.MidiChannel;
import java.io.File;
import java.net.URL;

public class Drummer {


	public static final int HITHAT = 42;
	public static final int CRASH = 49;
	public static final int RIDE = 51; //
	public static final int KICK = 35; // pied
	public static final int SNARE = 38; // gauche
	public static final int HIGH_TOM = 50; // millieu gauche
	public static final int MIDDLE_TOM = 43; // millieu droite
	public static final int FLOOR_TOM = 41; // droite
	
	private final CustomSynthesizer synthesizer;
	private final MidiChannel channel;

	public Drummer() {
		this.synthesizer = new CustomSynthesizer();
		loadBank("/soundbanks/sdb.sf2");
		this.channel = synthesizer.channels[9];
	}

	private void loadBank(String bank) {
		URL resource = getClass().getResource(bank);
		String fileName = resource.getFile();
		File file = new File(fileName);
		synthesizer.loadSoundbank(file.getAbsoluteFile());
	}


	public void noteOn(int note,int velocity){
		channel.noteOn(note, velocity);
	}
}
