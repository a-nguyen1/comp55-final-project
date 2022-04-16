package edu.pacific.comp55.starter;
import acm.graphics.GLabel;

public class SoundEffect implements Playable {
	private String name;
	
	public SoundEffect(String n) {
		name = n; //Implemented name being named here.
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void play(AudioPlayer player, GLabel statusLabel) {
		// TODO Auto-generated method stub
		player.stopSound("sounds", name + ".mp3"); //Added stop sound as well.
		player.playSound("sounds", name + ".mp3"); //Play sound 
		statusLabel.setLabel("playing: " + name); //Modify statusLabel
	}

	@Override
	public String getName() {
		return name;
	}

}
