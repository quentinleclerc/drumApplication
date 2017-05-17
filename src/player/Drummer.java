package player;

import javax.sound.midi.MidiChannel;

public class Drummer {
	public static final int KICK =35;
	public static final int SNARE = 38;
	public static final int HITHAT = 42;
	public static final int CRASH = 49;
	public static final int RIDE = 51;
	public static final int HIGH_TOM = 50;
	public static final int MIDDLE_TOM = 43;
	public static final int FLOOR_TOM = 41;
	
	final CustomSynthesizer synthesizer;
	final MidiChannel channel;
	public Drummer(CustomSynthesizer synthesizer) {
		this.synthesizer=synthesizer;
		this.channel=synthesizer.channels[9];
	}
	
	public void noteOn(int note,int velocity){
		channel.noteOn(note, velocity);
	}
}
