package midi;

public interface CanalNote {
	public void sendNote(long time,int note,int velocity);
	public void saveSong();
	
	//To remove
	public SoundRecord getSong();
}
