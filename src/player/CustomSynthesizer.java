package player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

/**
 * This is a Custom Synthesizer, you can easily load a Soundbank file to the synthesizer and get the Instrument loaded on the synthesizer
 * @author chivunito
 */
public class CustomSynthesizer {
	Instrument[] instruments ;
	final Synthesizer synthesizer;
	final MidiChannel[] channels;
	Soundbank soundbank;

	public CustomSynthesizer() {
		Synthesizer synthe = null;
		try {
			synthe = MidiSystem.getSynthesizer();
		} catch (MidiUnavailableException e) {
			System.out.println("Impossible to get the synthesizer");
			e.printStackTrace();
		}
		this.synthesizer=synthe;
		try {
			this.synthesizer.open();
		} catch (MidiUnavailableException e) {
			System.out.println("Error opening the synthesizer");
			e.printStackTrace();
		}
		this.instruments=synthesizer.getAvailableInstruments();
		this.channels=synthesizer.getChannels();
		this.soundbank=synthesizer.getDefaultSoundbank();
	}

	/**
	 * Bind a new Soundbank to the synthesizer
	 * @param file
	 * */
	public void loadSoundbank(File file){
		//File file = new File(filepath);
		Soundbank sb =null;
		try {
			sb = MidiSystem.getSoundbank(file);
		} catch (InvalidMidiDataException e1) {
			System.out.println("Invalid Soundbank");
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("Invalid Soundbank");
			e1.printStackTrace();
		}
		if (!this.synthesizer.isSoundbankSupported(sb)){
			System.out.println("SoundBank not supported");
		}
		else{
			this.synthesizer.loadAllInstruments(sb);
			this.soundbank=sb;
			this.instruments=this.soundbank.getInstruments();
		}
	}
	
	public MidiChannel[] getMidiChannel(){
		return this.channels;
	}
		
	/**
	 * Rebind the defaultSoundbank
	 */
	public void loadDefaultSoundbank(){
		this.soundbank=this.synthesizer.getDefaultSoundbank();
		this.synthesizer.loadAllInstruments(this.soundbank);
		this.instruments=this.soundbank.getInstruments();
	}

	/**
	 * Print the loaded instrument on the synthetsizer
	 */
	public void printInstrument(){
		for (int i = 0; i < instruments.length; i++) {
			Patch patch = instruments[i].getPatch();
			System.out.println("Instrument["+i+"] : " + instruments[i].getName() +"(Bank[" + patch.getBank() +"], Patch["+ patch.getProgram()+"])");
		}
	}

	/**
	 * Print the binded Soundbank
	 */
	public void printSoundBank(){
		System.out.println(this.soundbank.getName());
		System.out.println(this.soundbank.getDescription());
	}
	
	public void close(){
		this.synthesizer.close();
	}

	public static void main(String[] args) {
		/*
		CustomSynthesizer synthe = new CustomSynthesizer();
		synthe.printInstrument();
		synthe.printSoundBank();
		synthe.loadSoundbank("sdb.sf2");
		synthe.printInstrument();
		 */
		
		
//		Sequencer seq=null;
//	
//		try {
//			seq = MidiSystem.getSequencer();
//		} catch (MidiUnavailableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			seq.setSequence(new FileInputStream( new File("midi.mid")));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidMidiDataException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			seq.open();
//		} catch (MidiUnavailableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		seq.start();
	}
}


