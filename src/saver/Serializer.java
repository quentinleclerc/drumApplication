package saver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import midi.MidiFileToSong;
import midi.SoundRecord;
import player.PlayerSong;

public class Serializer {
	public Serializer() {
	}

	public void Serialize(SoundRecord song, String name){
		try {
			FileOutputStream fos = new FileOutputStream(name);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(song);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SoundRecord deSerialize(String name){
		SoundRecord song = null;
		try
		{
			FileInputStream fis = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			song = (SoundRecord) ois.readObject();
			ois.close();
			fis.close();
		}catch(IOException ioe){
			ioe.printStackTrace();

		}catch(ClassNotFoundException c){
			System.out.println("Class not found");
			c.printStackTrace();
		}
		return song;
	}


	public static void main(String[] args) {
		Serializer sr = new Serializer();

		MidiFileToSong translator = new MidiFileToSong("test.mid",3F,0);
		SoundRecord song = translator.getSong();
//		System.out.println(song);
		PlayerSong playerSong = new PlayerSong(song);
//		playerSong.playSong();
//		
		
		System.out.println(sr.deSerialize("MyfirstSong"));
//		sr.Serialize(song, "song.txt");
//		sr.DeSerialize("first");
	}
}
