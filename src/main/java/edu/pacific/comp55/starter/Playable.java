package edu.pacific.comp55.starter;
import acm.graphics.GLabel;
import edu.pacific.comp55.starter.AudioPlayer;

public interface Playable {
	public void play(AudioPlayer player, GLabel statusLabel); //added these methods.
	public String getName(); //added these methods.
}
