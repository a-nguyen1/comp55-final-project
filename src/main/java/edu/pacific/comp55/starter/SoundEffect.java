package edu.pacific.comp55.starter;
import acm.graphics.GLabel;

public class SoundEffect {
	private String name;
	private AudioPlayer player;
	
	public SoundEffect(AudioPlayer p, String n) {
		player = p;
		name = n; //Implemented name being named here.
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void play(AudioPlayer player) {
		// TODO Auto-generated method stub
		player.stopSound("sounds", name + ".mp3"); //Added stop sound as well.
		player.playSound("sounds", name + ".mp3"); //Play sound 
	}
	
	public void playLoop(AudioPlayer player) {
		// TODO Auto-generated method stub
		player.stopSound("sounds", name + ".mp3"); //Added stop sound as well.
		player.playSound("sounds", name + ".mp3", true); //Play sound 
	}
	
	public void setName(String n) {
		name = n;
	}
	public String getName() {
		return name;
	}

	public AudioPlayer getPlayer() {
		return player;
	}

	public void setPlayer(AudioPlayer player) {
		this.player = player;
	}

}